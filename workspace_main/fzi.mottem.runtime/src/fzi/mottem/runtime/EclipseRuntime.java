package fzi.mottem.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Display;

import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTS_ESEVERITY;
import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.interfaces.IReportSource;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.interfaces.ITest;
import fzi.mottem.runtime.interfaces.IUIDResolver;
import fzi.mottem.runtime.problems.ProblemsView;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import fzi.mottem.runtime.rti.interfaces.IAccessDriver;
import fzi.mottem.runtime.util.AbstractAsyncExecutable;
import fzi.mottem.runtime.util.UIDResolver;

public abstract class EclipseRuntime implements IRuntime
{
	private class EventMonitor
	{
		public boolean IsWakeupSpurious = true;
	}
	
	// this flag disconnects the whole driver (only dummy values are returned).
	// For testing purpose only.
	public static final boolean SIMULATION_MODE = false;

	public static final int ASYNC_EXECUTION_CANCEL_TIMEOUT_MILLIS = 500;
	
	protected final UIDResolver _uidResolver = new UIDResolver();
	protected final IProject _eclipseProject;

	private final HashMap<String, AbstractAsyncExecutable> _asyncExecutables = new HashMap<String, AbstractAsyncExecutable>();
	private final HashMap<String, LinkedList<EventMonitor>> _awaitedEvents = new HashMap<String, LinkedList<EventMonitor>>();
	private final HashMap<String, AbstractAccessDriver> _accessDrivers = new HashMap<String, AbstractAccessDriver>();
	private final HashMap<ITestReferenceable, LinkedList<IAccessDriver>> _driversByTRef = new HashMap<ITestReferenceable, LinkedList<IAccessDriver>>();
	private final DataConsumer _consumer;

	private long _startTime = 0;

	public EclipseRuntime(IProject eclipseProject)
	{
		_eclipseProject = eclipseProject;
		_consumer = new DataConsumer(_uidResolver);
	}

	@Override
	public IUIDResolver getUIDResolver()
	{
		return _uidResolver;
	}

	@Override
	public IProject getEclipseProject()
	{
		return _eclipseProject;
	}

	@Override
	public double getGlobalTime()
	{
		return System.currentTimeMillis() - _startTime;
	}
	
	@Override
	public AbstractAccessDriver getAccessDriver(String inspectorUID)
	{
		AbstractAccessDriver accessDriver = _accessDrivers.get(inspectorUID);
		if (accessDriver != null)
			return accessDriver;

		System.out.println("Warning: lazy creation of inspector (test did not register properly)");
		accessDriver = createAbstractAccessDriverInstance(inspectorUID);
		
		if (!SIMULATION_MODE)
			accessDriver.init();
		
		return accessDriver;
	}

	
	// Creation of driver implementations
	
	@Override
	public void init(String... inspectorUIDs)
	{
		_accessDrivers.clear();
		_driversByTRef.clear();

		for (String inspectorUID : inspectorUIDs)
		{
			AbstractAccessDriver accessDriver = createAbstractAccessDriverInstance(inspectorUID);
			
			if (!SIMULATION_MODE)
				accessDriver.init();
		}

		// Configure consumer that receives updates from UI about associated access driver
		// must be done using UI thread, as "getSignalsProducedByUI()" can only be called by that thread
		Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
				for (String signalUID : ViewCoordinator.getSignalsProducedByUI())
				{
					ITestReferenceable tRef = (ITestReferenceable)_uidResolver.getElement(signalUID);
					if (_driversByTRef.containsKey(tRef))
					{
						if (_driversByTRef.get(tRef).size() > 1)
							System.err.println("Multiple inspectors to update signal " + tRef.getName() + " produced by UI, first selected");
							
						IAccessDriver accessDriver = _driversByTRef.get(tRef).get(0);
						_consumer.setAccessDriverForTRef(tRef, accessDriver);
						DataExchanger.registerConsumer(signalUID, _consumer);
					}
					else
					{
						System.err.println("No inspector to update signal " + signalUID + " produced by UI");
					}
				}
		    }
		});

		_startTime = System.currentTimeMillis();
	}
	
	@Override
	public void cleanup()
	{
		cancelAllAsyncExecutions();
		
		DataExchanger.removeConsumer(_consumer);

		if (SIMULATION_MODE)
			return;
		
		for (Entry<String, AbstractAccessDriver> entry : _accessDrivers.entrySet())
		{
			entry.getValue().cleanup();
		}
	}
	
	@SuppressWarnings("unchecked")
	private AbstractAccessDriver createAbstractAccessDriverInstance(String inspectorUID)
	{
		IInspector inspector = (IInspector)_uidResolver.getElement(inspectorUID);
		
		if (inspector == null)
		{
			System.err.println("No inspector with UID '" + inspectorUID + "'");
			return null;
		}
		
		Class<AbstractAccessDriver> accessDriverClass = null;
		Constructor<AbstractAccessDriver> accessDriverConstructor = null;
		AbstractAccessDriver accessDriver = null;
		try
		{
			accessDriverClass = (Class<AbstractAccessDriver>) Class.forName(inspector.getRuntimeInspectorClass());
			accessDriverConstructor = accessDriverClass.getConstructor(IRuntime.class, IInspector.class);
			accessDriver = accessDriverConstructor.newInstance(this, inspector);
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			System.err.println("Could not construct required RuntimeInspector class '" + inspector.getRuntimeInspectorClass() + "'");
			e.printStackTrace();
		}
		
		_accessDrivers.put(inspectorUID, accessDriver);

		LinkedList<ITestReferenceable> list = ModelUtils.getAvailableTRefs(accessDriver.getInspector());

		// create a mappings tRef -> accessDriver for each tRef
		// and accessDriver -> tRefs
		HashSet<ITestReferenceable> set = new HashSet<ITestReferenceable>();
		if (list != null)
		{
			for(ITestReferenceable tRef : list)
			{
				set.add(tRef);
				
				if (_driversByTRef.containsKey(tRef))
				{
					_driversByTRef.get(tRef).add(accessDriver);
				}
				else
				{
					LinkedList<IAccessDriver> accessDriversForTRef = new LinkedList<IAccessDriver>();
					accessDriversForTRef.add(accessDriver);
					_driversByTRef.put(tRef, accessDriversForTRef);
				}
			}
		}

		accessDriver.setTRefs(set);

		return accessDriver;
	}

	
	// Logging
	
	@Override
	public void assertionFailed(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, String ptsCode, int severity)
	{
		assertionFailed(test, sourceEcoreURI, offset, length, lineNr, ptsCode, severity, null);
	}

	@Override
	public void assertionFailed(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, String ptsCode, int severity, String message)
	{
		assertionFailed(test, sourceEcoreURI, offset, length, lineNr, ptsCode, severity, message, null);
	}

	@Override
	public void assertionFailed(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, String ptsCode, int severity, String message, Object context)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date();
		
		if (message == null)
			message = "";
		
		FailedAssertion fa = new FailedAssertion(dateFormat.format(date),sourceEcoreURI, offset, length, lineNr, ptsCode, PTS_ESEVERITY.get(severity), message, context);

		if (test instanceof ITest) 
			((ITest)test).addFailedAssertion(fa);

		printPTSpecLog(PTS_ESEVERITY.get(severity), sourceEcoreURI, offset, lineNr, length, message);
	}

	@Override
	public void report(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, int severity, String message)
	{
		report(test, sourceEcoreURI, offset, length, lineNr, severity, message, null);
	}

	@Override
	public void report(IReportSource test, String sourceEcoreURI, int offset, int length, int lineNr, int severity, String message, Object context)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date();

		if (message == null)
			message = "";
		
		ReportMessage rm = new ReportMessage(dateFormat.format(date), sourceEcoreURI, offset, length, lineNr, PTS_ESEVERITY.get(severity), message, context);
		
		if (test instanceof ITest)
			((ITest)test).addReportMessage(rm);

		printPTSpecLog(PTS_ESEVERITY.get(severity), sourceEcoreURI, offset, length, lineNr, message);
	}

	public void printPTSpecLog(PTS_ESEVERITY severity, String sourceEcoreURI, int offset, int length, int lineNr, String message)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date();
		ProblemsView.addProblem(severity, dateFormat.format(date),sourceEcoreURI, offset, length, lineNr, message);
		//PTSpecConsole.getInstance().println_background(str);
	}
	

	// Update notification
	
	@Override
	public void notifyUpdate(String elementUID, double value)
	{
		fzi.mottem.runtime.dataexchanger.Signal de_signal = DataExchanger.getSignal(elementUID);
		
		if (de_signal != null)
			DataExchanger.consume(new DEMessage(de_signal, value, System.currentTimeMillis() - _startTime));
	}


	// Thread control
	
	@Override
	public void raiseEvent(String uid)
	{
		LinkedList<EventMonitor> ems = null;
		synchronized (_awaitedEvents)
		{
			ems = _awaitedEvents.get(uid);
			_awaitedEvents.remove(uid);
		}
		
		if (ems != null)
		{
			for (EventMonitor em : ems)
			{
				synchronized (em)
				{
					em.IsWakeupSpurious = false;
					em.notify();
				}
			}
		}
	}

	@Override
	public void suspendUntil(String uid)
	{
		EventMonitor em = new EventMonitor();
		synchronized (_awaitedEvents)
		{
			if (!_awaitedEvents.containsKey(uid))
			{
				LinkedList<EventMonitor> ems = new LinkedList<EventMonitor>();
				ems.add(em);
				_awaitedEvents.put(uid, ems);
			}
			else
			{
				LinkedList<EventMonitor> ems = _awaitedEvents.get(uid);
				ems.add(em);
			}
		}
		
		synchronized (em)
		{
			try
			{
				while (em.IsWakeupSpurious)
					em.wait();
			}
			catch (InterruptedException e)
			{
			}
		}
	}

	@Override
	public void suspendUntil(EEvent event, String uid)
	{
		suspendUntil(uid + "_" + event);
	}
	
	@Override
	public void registerAsyncExecutable(AbstractAsyncExecutable asyncExec)
	{
		// TODO Auto-generated method stub
		synchronized (_asyncExecutables)
		{
			if (_asyncExecutables.containsKey(asyncExec.getUID()))
				throw new RuntimeException("Cannot start another async. execution with ID '" + asyncExec.getUID() + "'");
			
			_asyncExecutables.put(asyncExec.getUID(), asyncExec);
		}
		
	}
	
	@Override
	public void unregisterAsyncExecutable(AbstractAsyncExecutable asyncExec)
	{
		synchronized (_asyncExecutables)
		{
			_asyncExecutables.remove(asyncExec.getUID());
		}
	}

	@Override
	public void cancelAsyncExecution(String... asyncExecUIDs)
	{
		LinkedList<AbstractAsyncExecutable> asyncExecutables = new LinkedList<AbstractAsyncExecutable>();

		for (String asyncExecUID : asyncExecUIDs)
		{
			AbstractAsyncExecutable asyncExecutalbe;
			synchronized (_asyncExecutables)
			{
				if (_asyncExecutables.containsKey(asyncExecUID))
					asyncExecutalbe = _asyncExecutables.get(asyncExecUID);
				else
					continue;
			}
			asyncExecutables.add(asyncExecutalbe);
		}
		
		AbstractAsyncExecutable asyncExecutablesArray[] = new AbstractAsyncExecutable[asyncExecutables.size()];
		
		doCancelAsyncExecutions(asyncExecutables.toArray(asyncExecutablesArray));
	}

	@Override
	public void cancelAllAsyncExecutions()
	{
		AbstractAsyncExecutable asyncExecutables[];
		
		synchronized (_asyncExecutables)
		{
			asyncExecutables = new AbstractAsyncExecutable[_asyncExecutables.values().size()];
			_asyncExecutables.values().toArray(asyncExecutables);
		}
		
		doCancelAsyncExecutions(asyncExecutables);
	}
	
	@SuppressWarnings("deprecation")
	private void doCancelAsyncExecutions(AbstractAsyncExecutable asyncExecutables[])
	{
		for (AbstractAsyncExecutable asyncExecutable : asyncExecutables)
		{
			asyncExecutable.stop();
		}
		
		for (AbstractAsyncExecutable asyncExecutable : asyncExecutables)
		{
			if (asyncExecutable.getThread() != null)
			{
				try
				{
					Thread t = asyncExecutable.getThread();
					t.join(ASYNC_EXECUTION_CANCEL_TIMEOUT_MILLIS);
					
					if (t.isAlive())
					{
						System.err.println("Request for cancelation of async execution timed out; forced stop");
						
						// use brutal force to stop async execution
						t.stop();
						break;
					}
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException("Interrupted while waiting for async execution to stop");
				}
			}
		}
	}
	
}
