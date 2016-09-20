package fzi.mottem.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.ISignal;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.codemodel.DTFloatingPoint;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.ptspec.dsl.pTSpec.PTSTestVariableDeclaration;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.interfaces.ITest;
import fzi.mottem.runtime.interfaces.ITrace;
import fzi.mottem.runtime.interfaces.ITraceEvaluator;
import fzi.mottem.runtime.interfaces.IUIDResolver;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import fzi.mottem.runtime.rti.AbstractTraceDriver;
import fzi.mottem.runtime.rti.interfaces.ITraceDriver;
import fzi.mottem.runtime.util.AbstractAsyncExecutable;
import fzi.mottem.runtime.util.ExecutionInterruptedException;


public class Trace implements ITrace
{
	private class RealtimeScheduler
	{
		private class ThreadMonitor
		{
			public boolean IsAwaited = false;
			public boolean IsRunning = true;
			public boolean IsWakeupNotSpurious = false;
			public Double WakeupTime = 0.0;
			public String AwaitedEventUID = null;
		}
		
		private HashMap<Thread, ThreadMonitor> _allThreads = new HashMap<Thread, ThreadMonitor>();
		
		private final Thread _schedulerThread;
		
		public RealtimeScheduler()
		{
			_schedulerThread = new Thread("RealtimeScheduler")
			{
				@Override
				public void run()
				{
					schedule();
				}
			};
		}
		
		private void schedule()
		{
			try
			{
				while (true)
				{
					synchronized(this)
					{
						boolean runnersLeft;
						do
						{
							runnersLeft = false;
							this.wait(100);
							
							if (_allThreads.size() == 0)
								return;
							
							for (ThreadMonitor ti : _allThreads.values())
							{
								if (ti.IsRunning)
								{
									runnersLeft = true;
									break;
								}
							}
						}
						while(runnersLeft);
					}
						
					// NO CHANCE that we get here if other threads are running!

					ThreadMonitor next = null;
					for (ThreadMonitor ti : _allThreads.values())
					{
						if (ti.IsAwaited && ti.WakeupTime >= 0.0)
						{
							if (next == null || ti.WakeupTime < next.WakeupTime)
								next = ti;
						}
					}
					
					if (next != null)
					{
						synchronized(next)
						{
							_timeStamp = next.WakeupTime;
							next.IsAwaited = false;
							next.IsRunning = true;
							next.IsWakeupNotSpurious = true;
							next.notify();
						}
					}
				}
			}
			catch (InterruptedException e)
			{
			}
		}
		
		public void registerThread(Thread t)
		{
			ThreadMonitor ti = new ThreadMonitor();
			ti.IsAwaited = false;
			ti.IsRunning = true;
			ti.IsWakeupNotSpurious = false;
			
			synchronized(this)
			{
				_allThreads.put(t, ti);
			}
		}
		
		public void unregisterThread(Thread t)
		{
			synchronized(this)
			{
				_allThreads.remove(t);
			}
		}

		public void start()
		{
			_schedulerThread.start();
		}
		
		public void suspendCurrentThreadAndSchedule(Double wakeupTime)
		{
			ThreadMonitor ti;
			synchronized(this)
			{
				ti = _allThreads.get(Thread.currentThread());
			}
			
			synchronized(ti)
			{
				synchronized(this)
				{
					ti.IsAwaited = true;
					ti.IsRunning = false;
					ti.IsWakeupNotSpurious = false;
					ti.WakeupTime  = wakeupTime;
					this.notify();
				}
				
				try
				{
					while (!ti.IsWakeupNotSpurious)
						ti.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		public void suspendCurrentThreadAndSchedule(String eventUID)
		{
			ThreadMonitor ti;
			synchronized(this)
			{
				ti = _allThreads.get(Thread.currentThread());
			}
			
			synchronized(ti)
			{
				synchronized(this)
				{
					ti.IsAwaited = true;
					ti.IsRunning = false;
					ti.IsWakeupNotSpurious = false;
					ti.WakeupTime  = -1.0;
					ti.AwaitedEventUID = eventUID;
					this.notify();
				}
				
				try
				{
					while (!ti.IsWakeupNotSpurious)
						ti.wait();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		public void eventRaised(String customEvent, Double timeStamp)
		{
			LinkedList<ThreadMonitor> waitingTIs = new LinkedList<ThreadMonitor>();
			synchronized(this)
			{
				for (ThreadMonitor ti : _allThreads.values())
				{
					if (customEvent.equals(ti.AwaitedEventUID))
					{
						waitingTIs.add(ti);
						break;
					}
				}
			}
			
			for (ThreadMonitor ti : waitingTIs)
			{
				synchronized (ti)
				{
					ti.AwaitedEventUID = null;
					ti.WakeupTime = timeStamp;
				}
			}
		}

		public void exit()
		{
			LinkedList<Thread> allThreadsStillRunning = new LinkedList<Thread>();
			
			synchronized(this)
			{
				_allThreads.remove(Thread.currentThread());
				allThreadsStillRunning.addAll(_allThreads.keySet());
			}

			for (Thread t : allThreadsStillRunning)
			{
				try
				{
					t.join();
				}
				catch (InterruptedException e)
				{
					throw new ExecutionInterruptedException("Interrupted while waiting for AsyncExecutable to finish", e);
				}
			}

			if (_schedulerThread.isAlive())
				_schedulerThread.interrupt();
		}

	}

	private final Hashtable<String, AbstractTraceDriver> _traceControllers = new Hashtable<String, AbstractTraceDriver>();
	
	private final IRuntime _runtime;
	private final IUIDResolver _uidResolver;
	private final ITest _test;
	private final double _timeoutMillis;
	private final String _traceName;
	private final IFile _traceDBFile;
	
	private final TraceDB _traceDB;
	
	private Double _timeStamp = 0.0; 
	
	private ITraceEvaluator _traceEvaluator;
	
	private RealtimeScheduler _realtimeScheduler = new RealtimeScheduler();

	private Stack<Double> _timeStack = new Stack<Double>();
	
	public Trace(IRuntime runtime, ITest test, double timeoutMillis)
	{
		_runtime = runtime;
		_uidResolver = runtime.getUIDResolver();
		_test = test;
		_timeoutMillis = timeoutMillis;
		_traceName = _test.getNextTraceName();
		_traceDBFile = getTraceDBFolder().getFile(_traceName + ".db");
		
		_traceDB = new TraceDB(_traceDBFile, null, null);
		
		_realtimeScheduler.registerThread(Thread.currentThread());
		
		_realtimeScheduler.start();
	}

		
	// Getters and Setters

	@Override
	public void setUntilEvaluator(ITraceEvaluator evaluator)
	{
		_traceEvaluator = evaluator;
	}
	
	@Override
	public ITraceEvaluator getUntilEvaluator()
	{
		return _traceEvaluator;
	}
	
	@Override
	public TraceDB getTraceDB()
	{
		return _traceDB;
	}
	
	@Override
	public double getTimeoutMillis()
	{
		return _timeoutMillis;
	}

	@Override
	public boolean isEndOfTrace()
	{
		try
		{
			return _traceDB.getLastTime() <= _timeStamp;
		}
		catch (NoSuchElementException ex)
		{
			return true;
		}
	}
	
	@Override
	public double getTimestamp()
	{
		return _timeStamp;
	}


	// Interface functions for realtime pre-calculation. (called in realtime{} block)
	
	@Override
	public void registerAsyncExecutable(AbstractAsyncExecutable abstractAsyncExecutable)
	{
		_realtimeScheduler.registerThread(abstractAsyncExecutable.getThread());
	}
	
	@Override
	public void unregisterAsyncExecutable(AbstractAsyncExecutable abstractAsyncExecutable)
	{
		_realtimeScheduler.unregisterThread(abstractAsyncExecutable.getThread());
	}

	@Override
	public <T> void addStimulus(String inspectorUID, String executorUID, SetupEvent setupEvent, String elementUID, EItemProperty property, T value, Object... arguments)
	{
		if (Runtime.SIMULATION_MODE)
			return;
			
		IExecutor executor = (IExecutor)_uidResolver.getElement(executorUID);
		ITestReferenceable element = elementUID != null ? (ITestReferenceable)_uidResolver.getElement(elementUID) : null;
		ITraceDriver tctrl = getTraceController(inspectorUID);
		if (tctrl != null)
		{
			tctrl.addStimulus(_timeStamp, executor, setupEvent, element, property, value, arguments);
		}
		else
		{
			System.err.println("Error: Stimulus lost!");
		}
	}

	@Override
	public void suspend(double milliSecondsToWait)
	{
		Double wakeupTime = _timeStamp + milliSecondsToWait;
		_realtimeScheduler.suspendCurrentThreadAndSchedule(wakeupTime);
	}

	@Override
	public void suspendUntil(String customEvent)
	{
		_realtimeScheduler.suspendCurrentThreadAndSchedule(customEvent);
	}

	@Override
	public void raiseEvent(String customEvent)
	{
		_realtimeScheduler.eventRaised(customEvent, _timeStamp);
	}


	// Interface functions for remaining pre-configuration (captured signals) and capturing

	@Override
	public void configure(List<String> elementsToCapture)
	{
		_realtimeScheduler.exit();
		
		if (Runtime.SIMULATION_MODE)
			return;

		for (Entry<String, AbstractTraceDriver> entry : _traceControllers.entrySet())
		{
			HashSet<ITestReferenceable> tRefsForDriver = entry.getValue().getAccessDriver().getTRefs();
			List<ITestReferenceable> elements = new LinkedList<ITestReferenceable>();
			for (String elementUID : elementsToCapture)
			{
				ITestReferenceable tRef = (ITestReferenceable)_uidResolver.getElement(elementUID);
				if(tRefsForDriver.contains(tRef))
					elements.add(tRef);
			}

			entry.getValue().configure(elements);
		}
	}

	@Override
	public void runAndTrace()
	{
		LinkedList<Thread> tracingThreads = new LinkedList<Thread>();
		for (Entry<String, AbstractTraceDriver> entry : _traceControllers.entrySet())
		{
			final AbstractTraceDriver traceDriver = entry.getValue();
			Thread t = new Thread("TraceDriver_" + traceDriver.getAccessDriver().getInspector().getName())
				{
					@Override
					public void run()
					{
						traceDriver.runAndTrace();
					}
				};
			tracingThreads.add(t);
		}

		long currentTime = System.currentTimeMillis();
		_traceDB.setStartTime(currentTime);

		if (!Runtime.SIMULATION_MODE)
		{
			for (Thread t : tracingThreads)
			{
				t.start();
			}
		}
		
		long endTime = System.currentTimeMillis() + (long)_timeoutMillis;
		
		if (endTime < currentTime)
			throw new RuntimeException("Bad Trace timeout: system counter will wrap");
		
		boolean interrupted = false;
		try
		{
			while (System.currentTimeMillis() < endTime)
			{
				Thread.sleep(20);
				if (_traceEvaluator != null && _traceEvaluator.evaluateIsDone(this))
					break;
			}
		}
		catch (InterruptedException e)
		{
			interrupted = true;
		}
		finally
		{
			for (Thread t : tracingThreads)
			{
				t.interrupt();
			}
			
			if (interrupted)
				throw new ExecutionInterruptedException();
		}

		for (Entry<String, AbstractTraceDriver> entry : _traceControllers.entrySet())
		{
			entry.getValue().fillTraceDB();
		}

		_traceDB.synchronizeToMemory();
		_timeStamp = _traceDB.getTime(0.0);	
	}

	@Override
	public void storeAndClose()
	{
		_traceDB.synchronizeToHardStorage();
	}
	
	
	// Interface functions for trace pointer control (called in analyze{} block)

	@Override
	public void rewind()
	{
		_timeStamp = _traceDB.getTime(0.0);
	}

	@Override
	public void moveNextDelta()
	{
		_timeStamp = _traceDB.getNextDelta(_timeStamp);
	}
	
	@Override
	public void moveTime(double milliSeconds)
	{
		if(milliSeconds < 0)
			return;
		
		Double target = _timeStamp + milliSeconds;
		_timeStamp = target;
	}

	@Override
	public void pushTimeStack()
	{
		_timeStack.push(_timeStamp);
		rewind();
	}
	
	@Override
	public void popTimeStack()
	{
		_timeStamp = _timeStack.pop();
	}
	
	
	// Interface functions for evaluation of data and properties (called in analyze{} block)
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(String elementUID)
	{
		EObject ele = _uidResolver.getElement(elementUID);
		
		String valueString = _traceDB.getLastValue(_timeStamp, elementUID);
		
		if (valueString == null)
			return (T)Double.valueOf(0.0);

		if (ele instanceof Variable)
		{
			DataType dataType = ((Variable) ele).getDataType();			

			if (dataType instanceof DTInteger)
			{
				Long longValue = Long.decode(valueString); // decode represenation, this can handle 0x as well as integer representation

				if (((DTInteger) dataType).getBitSize() == 8)
					return (T)Byte.valueOf(longValue.byteValue());
				else if (((DTInteger) dataType).getBitSize() == 16)
					return (T)Short.valueOf(longValue.shortValue());
				else if (((DTInteger) dataType).getBitSize() == 32)
					return (T)Integer.valueOf(longValue.intValue());
				else if (((DTInteger) dataType).getBitSize() == 64)
					return (T)longValue;
				else
					throw new RuntimeException("Unsupported data type");
			}
			else if (dataType instanceof DTFloatingPoint)
			{
				Long longValue = null;
				if (!valueString.contains("."))
				{
					longValue = Long.decode(valueString); // decode represenation, this can handle 0x as well as integer representation
				}

				if (((DTFloatingPoint) dataType).getExponentBitSize() == 8 &&
					((DTFloatingPoint) dataType).getSignificandBitSize() == 23)
				{
					if (longValue != null)
						return (T)Float.valueOf(Float.intBitsToFloat(longValue.intValue()));
					else
						return (T)Float.valueOf(valueString);
				}
				else if (((DTFloatingPoint) dataType).getExponentBitSize() == 11 &&
						((DTFloatingPoint) dataType).getSignificandBitSize() == 52)
				{
					if (longValue != null)
						return (T)Double.valueOf(Double.longBitsToDouble(longValue));
					else
						return (T)Double.valueOf(valueString);
				}
				else
				{
					throw new RuntimeException("Unsupported data type");
				}
			}
			else
			{
				throw new RuntimeException("Unexpected variable datatype detected when fetching value from Trace DB");
			}
		}
		else if (ele instanceof ISignal)
		{
			// ele is a PTSTestVariableDeclaration. let's assume double. !TODO: use correct datatype in TraceDB
			Double doubleValue = Double.valueOf(valueString);
			return (T)doubleValue;
		}
		else if (ele instanceof PTSTestVariableDeclaration)
		{
			// ele is a PTSTestVariableDeclaration. let's assume double. !TODO: use correct datatype in TraceDB
			Double doubleValue = Double.valueOf(valueString);
			return (T)doubleValue;
		}
		else
		{
			throw new RuntimeException("Unexpected element detected when fetching value from Trace DB");
		}
	}
	
	@Override
	public <T> void injectValue(String elementUID, T value)
	{
		_traceDB.injectValueMS(_timeStamp, elementUID, value.toString());
	}
	
	@Override
	public <T> T getItemPropertyValue(String elementUID, EItemProperty property)
	{
		return getValue(PTSpecUtils.getPropertyUID(elementUID, EItemProperty.getPTSProperty(property)));
	}

	@Override
	public boolean checkEvent(EEvent event, String elementUID)
	{
		// !TODO (improvement): replace this hack; moveNextDelta, moveTime should throw exceptions when end of trace is reached.
		if (isEndOfTrace())
			return true;
		
		return _traceDB.checkEvent(_timeStamp, elementUID, event);
	}
	

	// Misc. functions (calculated properties etc.)
	
	@Override
	public IFolder getTraceDBFolder()
	{
		IFolder folder = _runtime.getEclipseProject().getFolder("tracedb");
		
		if (!folder.exists())
		{
			try
			{
				folder.create(false, true, null);
				folder.refreshLocal(0, null);
			}
			catch(CoreException e)
			{
				throw new RuntimeException("Could not create Trace DB folder");
			}
		}
		
		return folder;
	}

	@Override
	public IFile getTraceDBFile()
	{
		return _traceDBFile;
	}

	@Override
	public TraceDB getPlotTraceDB(String plotName)
	{
		IFile dbFile = getTraceDBFolder().getFile(_traceName + "_" + plotName + ".db");
		IFile dbImageFile = getTraceDBFolder().getFile(_traceName + "_" + plotName + ".db.png");
		return new TraceDB(dbFile, dbImageFile, plotName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void registerTraceController(String inspectorUID)
	{
		AbstractAccessDriver abstractInspector = _runtime.getAccessDriver(inspectorUID);
		
		if (abstractInspector == null)
			return;

		Class<AbstractTraceDriver> traceControllerClass = null;
		Constructor<AbstractTraceDriver> traceControllerConstructor = null;
		AbstractTraceDriver traceController = null;
		String traceControllerClassName = abstractInspector.getInspector().getTraceControllerClass();
		
		if (traceControllerClassName == null)
			return;
		
		try
		{
			traceControllerClass = (Class<AbstractTraceDriver>) Class.forName(traceControllerClassName);
			traceControllerConstructor = traceControllerClass.getConstructor(ITrace.class, AbstractAccessDriver.class);
			traceController = traceControllerConstructor.newInstance(this, abstractInspector);
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException("Could not construct required TraceController class '" + traceControllerClassName + "'");
		}
		
		_traceControllers.put(inspectorUID, traceController);
	}


	// Privates

	private ITraceDriver getTraceController(String inspectorUID)
	{
		if (_traceControllers.containsKey(inspectorUID))
		{
			return _traceControllers.get(inspectorUID);
		}
		else
		{
			return null;
		}
	}



}
