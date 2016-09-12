package fzi.mottem.runtime.rti.isystem;

import java.util.Hashtable;
import java.util.List;

import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.runtime.interfaces.ITrace;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import fzi.mottem.runtime.rti.AbstractTraceDriver;
import fzi.util.eclipse.IntegrationUtils;
import si.isystem.connect.CDebugFacade;
import si.isystem.connect.CExecutionController;

public class ISYSTEMTraceDriver extends AbstractTraceDriver
{
	public final Hashtable<String, ITestReferenceable> ISYSTEMNameToElementMap = new Hashtable<String, ITestReferenceable>();
	
	private final ISYSTEMXmlToDB _xmlToDB;

	private final ISYSTEMAccessDriver _isystemAccessDriver;
	
	private ISYSTEMProfiler _profiler;

	public ISYSTEMTraceDriver(ITrace trace, AbstractAccessDriver accessDriver)
	{
		super(trace, accessDriver);
		
		_isystemAccessDriver = (ISYSTEMAccessDriver)_accessDriver; 
		_xmlToDB = new ISYSTEMXmlToDB(this);
	}

	public ITrace getTrace()
	{
		return _trace;
	}

	public String getTraceXMLFile()
	{
		return IntegrationUtils.getSystemPathForWorkspaceRelativePath(_trace.getTraceDBFolder().getFile("isystemTrace.xml").getFullPath()).toOSString();
	}
	
	public String getTraceTRDFile()
	{
		return IntegrationUtils.getSystemPathForWorkspaceRelativePath(_trace.getTraceDBFolder().getFile("isystemTrace.trd").getFullPath()).toOSString();
	}
	
	@Override
	public void configure(List<ITestReferenceable> elementsToCapture)
	{
		// ignores the time for the event, as timed stimulation is not supported by the isystem debugger.
		for (InternalSetupEvent event : _setupEvents)
        {
            switch (event.getSetupEvent())
            {
    			case CALL:
    				_isystemAccessDriver.prepareExecutionStart(event.getExecutor(), (ITestCallable) event.getElement());
    				break;

    			case RUN:
    				// nothing to prepare
    				break;

    			case RUN_UNTIL:
    				_isystemAccessDriver.prepareExecutionStop(event.getExecutor(), (ITestCallable) event.getElement());
    				break;

    			case STOP:
    				// this state doesn't make sense for ISYSTEMTraceDriver...
    				throw new RuntimeException("Illegal SetupEvent STOP for ISYSTEMAccessDriver");

    			case WRITE:
    				// this state doesn't make sense for ISYSTEMTraceDriver...
    				throw new RuntimeException("Illegal SetupEvent WRITE for ISYSTEMAccessDriver");
            }
        }

		_profiler = new ISYSTEMProfiler(this, _isystemAccessDriver.getPrimaryConnectionManager());
		
		for(ITestReferenceable element : elementsToCapture)
        {
			String elementName = _isystemAccessDriver.getISYSTEMName(element);
			ISYSTEMNameToElementMap.put(elementName, element);
			
        	if(element instanceof Function)
            {
        		_profiler.addFunction(elementName);
            }
            else if(element instanceof Variable)
            {
            	_profiler.addVariable(elementName);
                Object value = _isystemAccessDriver.getValue((Variable)element);
                _trace.injectValue(elementName, value);
            }
        }
		
		_profiler.run();
	}

	@Override
	public void runAndTrace()
	{
		// just start all cores
		for (ProcessorCore core : _isystemAccessDriver.getProcessor().getProcessorCores())
		{
			CExecutionController execCtrl = _isystemAccessDriver.getCoreCtrlSet(core).getExecutionController();
			execCtrl.run();
		}
	}
	
	@Override
	public void fillTraceDB()
	{
		for (ProcessorCore core : _isystemAccessDriver.getProcessor().getProcessorCores())
		{
			// stop all executors (this will also stop threads)
			CExecutionController execCtrl = _isystemAccessDriver.getCoreCtrlSet(core).getExecutionController();
			execCtrl.stop();

			// cleanup breakpoints
			CDebugFacade debugFacade = _isystemAccessDriver.getCoreCtrlSet(core).getDebugFacade();
			debugFacade.getBreakpointController().deleteAll();
		}

		boolean dataExported = _profiler.exportData();

		if (dataExported)
			_xmlToDB.run();
	}

}
