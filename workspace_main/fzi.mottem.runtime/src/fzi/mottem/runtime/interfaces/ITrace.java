package fzi.mottem.runtime.interfaces;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import fzi.mottem.runtime.EEvent;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.TraceDB;
import fzi.mottem.runtime.util.AbstractAsyncExecutable;

public interface ITrace
{

	public enum SetupEvent { WRITE, CALL, RUN, RUN_UNTIL, STOP };

	// Interface functions for trace setup (called in trace{} block).
	// Being delegated to an AbstractTraceController
	
	public void registerAsyncExecutable(AbstractAsyncExecutable abstractAsyncExecutable);

	public void unregisterAsyncExecutable(AbstractAsyncExecutable abstractAsyncExecutable);

	/*
	 * Adds an execution to realtime execution stack of provided executor.
	 * 
	 * Will throw a runtime exception if provided executable element cannot be executed by the
	 * executor or if the provided argument list does not match the executable element.
	 * 
	 * 	implementations that iterate over the complete trace data take the form
	 * ...getValue(...);
	 *  do {
	 *   moveNextDelta(); 
	 *   ... getValue(...);
	 *   }while(!isEndOfTrace());
	 */
	public <T> void addStimulus(String inspectorUID, String executorUID, SetupEvent setupEvent, String elementUID, EItemProperty property, T value, Object... arguments);

	/*
	 * Suspends the current thread until new time.
	 */
	public void suspend(double milliSeconds);

	/*
	 * Suspends the current thread until given event occurs.
	 */
	public void suspendUntil(String customEventID);
	
	/*
	 * Triggers the given custom event
	 */
	public void raiseEvent(String customEvent);

	/*
	 * Starts realtime execution and recording of trace.
	 */
	public void configure(List<String> elementsToCapture);

	/*
	 * Starts realtime execution and recording of trace.
	 */
	public void runAndTrace();

	/*
	 * Stores data to hard storage (.db file) and closes connection
	 */
	void storeAndClose();


	// Getters and Setters

	/*
	 * Registers an evaluator that will check whether execution should continue, or may be stopped
	 * (based on recorded trace data).
	 */
	public void setUntilEvaluator(ITraceEvaluator evaluator);
	
	/*
	 * Provides the evaluator that will check whether execution should continue
	 */
	public ITraceEvaluator getUntilEvaluator();
	
	/*
	 * Returns a database handle with a persistent time-value-database-pointer to the profiling databse
	 */
	public TraceDB getTraceDB();

	/*
	 * Returns the timeout configured for the trace-session (zero means not timeout)
	 */
	public double getTimeoutMillis();


	// Interface functions for trace pointer control (called in analyze{} block)
	
	/*
	 * Resets "current time" pointer for recorded trace data to the beginning and 
	 * removes all variables for which getValue will return the cached last read/written value
	 * do not use this method with continuous traces with interval recording
	 */
	public void rewind();

	/*
	 * Moves "current time" pointer to the next event that has been recorded
	 */
	public void moveNextDelta();
	
	/*
	 * Moves "current time" pointer by the provided offset, if no discrete time value exists,
	 * the "current time" pointer is set to the closest value by rounding down
	 */
	public void moveTime(double milliSeconds);
	
	/*
	 * Pushes the "current time" on internal time stack to be able to restore it later.
	 * Then resets the current time.
	 */
	public void pushTimeStack();
	
	/*
	 * Restores the "current time" from top of internal time stack
	 */
	public void popTimeStack();
	
	
	// Interface functions for evaluation of data and properties (called in analyze{} block)
	
	/*
	 * Return the value of the element at the point of time as indicated by the
	 * time pointer.
	 * 
	 * Will throw a runtime exception if the value cannot be accessed. 
	 */
	public <T> T getValue(String elementUID);
	
	/*
	 * Injects the provided value for the provided element at the point
	 * of time currently indicated by the time pointer.
	 */
	public <T> void injectValue(String elementUID, T value);
	
	/*
	 * Returns the property value of the provided element at the point of time as
	 * indicated by the time pointer.
	 * this will return the cached last read/written value for every variable
	 * 
	 * Will throw an exception if the provided element does not have the desired property.
	 */
	public <T> T getItemPropertyValue(String elementUID, EItemProperty property);

	/*
	 * Returns true if provided event is valid at the time indicated by "current time" pointer for the provided element.
	 * 
	 * Will throw a runtime exception if provided element does not have desired event.
	 */
	public boolean checkEvent(EEvent event, String elementUID);
	
	/*
	 * Returns true if there is no more recorded trace data available after the "current time" pointer
	 * i.e. the "current time" pointer points to the last recorded value, 
	 */
	public boolean isEndOfTrace();
	
	/*
	 * Returns time value (in milliseconds) at which "current time" pointer is currently placed
	 */
	public double getTimestamp();
	
	
	// Misc. functions (calculated properties etc.)
	
	/*
	 * Returns the IPath to trace DB folder
	 */
	public IFolder getTraceDBFolder();

	/*
	 * Returns the IFile for the trace DB
	 */
	public IFile getTraceDBFile();

	/*
	 * Creates a new TraceDB instance for plot with given name
	 */
	TraceDB getPlotTraceDB(String plotName);

	/*
	 * Registers a TraceController for given IInspector using reflection
	 */
	void registerTraceController(String inspectorUID);

}
