/**
 * 
 */
package fzi.mottem.runtime.interfaces;

import si.isystem.connect.CProfilerController2;
import si.isystem.connect.CTestCaseController;

/**
 * @author deuchler
 *
 * winIDEA specific data exchange between fzi.mottem.runtime.Trace and fzi.mottem.runtime.Runtime
 *
 */
public interface ITraceProvider 
{
	CProfilerController2 getProfilerController2();
	
	/**
	 * creates a new CTestCaseController for functionName
	 * all subsequent calls to this method will result in the destruction of the last returned CTestCaseController's 
	 * associated unmanaged objects
	 * 
	 * @param functionName
	 * @return
	 */
	CTestCaseController getTestCaseController(String functionName);
}
