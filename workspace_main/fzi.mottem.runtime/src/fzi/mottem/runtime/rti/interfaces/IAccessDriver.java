package fzi.mottem.runtime.rti.interfaces;

import java.util.HashSet;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.runtime.EItemProperty;

public interface IAccessDriver
{
	/*
	 * Sets ITestReferenceables that can be accessed by this driver as configured in TRM.
	 * Is called by Runtime after construction.
	 */
	public void setTRefs(HashSet<ITestReferenceable> tRefsForDriver);

	/*
	 * Returns all ITestReferenceables that can be accessed by this driver as configured in TRM.
	 */
	public HashSet<ITestReferenceable> getTRefs();
	
	/*
	 * Gets called at the beginning of every testcase execution.
	 * Allows initialization if needed.
	 */
	public void init();
	
	/*
	 * Gets called at the end of every testcase execution.
	 * Allows cleanup if needed.
	 */
	public void cleanup();
	
	
	/*
	 * Return the current value of the element. Depending on the context
	 * the value source is either the recorded trace data or test environment
	 * (embedded software, bus, network, ...).
	 * 
	 * Will throw a runtime exception if the value cannot be accessed. 
	 */
	public <T> T getValue(ITestReadable element);
	
	/*
	 * Sets value of provided element.
	 * 
	 * Will throw a runtime exception if value cannot be set or value type is not compatible to the element type.
	 */
	public <T> void setValue(ITestWriteable element, T value);
	
	/*
	 * Returns the property value of the provided element.
	 * 
	 * Will throw an exception if the provided element does not have the desired property.
	 */
	public <T> T getItemPropertyValue(ITestReferenceable element, EItemProperty property);

	/*
	 * Updates the provided property of the provided element.
	 * 
	 * Will throw a runtime exception if the element does not have the desired property
	 * or the value is not compatible.
	 */
	public <T> void setItemPropertyValue(ITestReferenceable element, EItemProperty property, T value);
	
	
	/*
	 * Uses the provided executor to execute the provided element (e.g. function).
	 * 
	 * Will throw a runtime exception if provided executor is not capable of executing the
	 * provided element or list of arguments does not correspond to the executable element. 
	 */
	public <T> T execute(IExecutor executor, ITestCallable executableElement, Object... arguments);

	/*
	 * Starts execution of provided executor.
	 */
	public void run(IExecutor executor);

	/*
	 * Starts execution of provided executor after setting a breakpoint at the beginning of
	 * the provided element (e.g. function).
	 * 
	 * Will throw a runtime exception if provided executor is not capable of executing the
	 * provided element. 
	 */
	public void runUntil(IExecutor executor, ITestCallable executableElement);
	
	/*
	 * Stops execution of provided executor.
	 */
	public void stop(IExecutor executor);

	/*
	 * Sets a breakpoint at the beginning of the provided element (e.g. function).
	 * 
	 * Will throw a runtime exception if provided executor is not capable of breaking at the
	 * provided element.
	 */
	public void breakAt(IExecutor executor, ITestCallable executableElement);
	
}
