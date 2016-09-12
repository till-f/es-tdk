package fzi.mottem.runtime.interfaces;

import org.eclipse.core.resources.IProject;

import fzi.mottem.runtime.EEvent;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import fzi.mottem.runtime.util.AbstractAsyncExecutable;

public interface IRuntime extends IAssertionEvaluator, IReportHandler
{

	/*
	 * Returns the UIDResolver for the Runtime
	 */
	public IUIDResolver getUIDResolver();
	
	/*
	 * returns the eclipse project for which this runtime has been created
	 */
	public IProject getEclipseProject();

	/*
	 * Returns specific AccessDriver for the provided IInspector (Testrig Model element)
	 */
	public AbstractAccessDriver getAccessDriver(String inspectorUID);
	
	/*
	 * This is should be called from ITest.init().
	 * It allows all drivers needed for this test to initialize by calling their
	 * respective init() function.
	 */
	public void init(String... inspectorUIDs);
	
	/*
	 * This is should be called from ITest.cleanup().
	 * It stops all possibly still open async executions.
	 * It allows all drivers needed for this test to cleanup by calling their
	 * respective cleanup() function.
	 */
	public void cleanup();
	
	/*
	 * Must be called by drivers to notify about asynchronous updates
	 * Is also called when Test updates something.
	 */
	void notifyUpdate(String elementUID, double value);

	/*
	 * called by generated code to trigger a custom event
	 */
	public void raiseEvent(String uid);
	
	/*
	 * called by generated code to wait until custom event is triggered
	 */
	public void suspendUntil(String uid);

	/*
	 * called by generated code to wait until pre-defined event is triggered
	 */
	public void suspendUntil(EEvent event, String uid);
	
	
	/*
	 * Register a concurrent execution
	 */
	public void registerAsyncExecutable(AbstractAsyncExecutable asyncExec);

	/*
	 * Un-register a concurrent execution
	 */
	public void unregisterAsyncExecutable(AbstractAsyncExecutable abstractAsyncExecutable);

	/*
	 * Stops all threads that are coupled with the provided concurrent executions.
	 */
	public void cancelAsyncExecution(String... asyncExecUIDs);
	
	/*
	 * Stops all threads that are coupled with any executor existing in the context of the runtime.
	 */
	public void cancelAllAsyncExecutions();
	
	/*
	 * Returns the current time after start of text execution
	 */
	public double getGlobalTime();
	
	
	/*
	 *  Functions delegating to IAccessDriver implementations
	 *  =============================================================================================
	 */
	
	/*
	 * Return the current value of the element.
	 * 
	 * Will throw a runtime exception if the value cannot be accessed. 
	 */
	public <T> T getValue(String inspectorUID, String elementUID);
	
	/*
	 * Sets value of provided element.
	 * 
	 * Will throw a runtime exception if value cannot be set or value type is not compatible to the element type.
	 */
	public <T> void setValue(String inspectorUID, String elementUID, T value);
	
	/*
	 * Returns the property value of the provided element.
	 * 
	 * Will throw an exception if the provided element does not have the desired property.
	 */
	public <T> T getItemPropertyValue(String inspectorUID, String elementUID, EItemProperty property);
	
	/*
	 * Updates the provided property of the provided element.
	 * 
	 * Will throw a runtime exception if the element does not have the desired property
	 * or the value is not compatible.
	 */
	public <T> void setItemPropertyValue(String inspectorUID, String elementUID, EItemProperty property, T value);
	
	/*
	 * Uses the provided executor to execute the provided element (e.g. function).
	 * 
	 * Will throw a runtime exception if provided executor is not capable of executing the
	 * provided element or list of arguments does not correspond to the executable element. 
	 */
	public <T> T execute(String inspectorUID, String executorUID, String executableElementUID, Object... arguments);

	/*
	 * Starts execution of provided executor.
	 */
	public void run(String inspectorUID, String executorUID);

	/*
	 * Starts execution of provided executor after setting a breakpoint at the beginning of
	 * the provided element (e.g. function).
	 * 
	 * Will throw a runtime exception if provided executor is not capable of executing the
	 * provided element. 
	 */
	public void runUntil(String inspectorUID, String executorUID, String executableElementUID);
	
	/*
	 * Stops execution of provided executor.
	 */
	public void stop(String inspectorUID, String executorUID);

	/*
	 * Sets a breakpoint at the beginning of the provided element (e.g. function).
	 * 
	 * Will throw a runtime exception if provided executor is not capable of breaking at the
	 * provided element.
	 */
	public void breakAt(String inspectorUID, String executorUID, String executableElementUID);

}
