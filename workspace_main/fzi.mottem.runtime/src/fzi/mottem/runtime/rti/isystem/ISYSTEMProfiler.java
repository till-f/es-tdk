/**
 * 
 */
package fzi.mottem.runtime.rti.isystem;

import si.isystem.connect.CProfilerController2;
import si.isystem.connect.CProfilerExportConfig;
import si.isystem.connect.ConnectionMgr;

/**
 * @author deuchler
 * 
 * a wrapper to run the winIdea profiler  
 * 
         usage of profiler based on profiler2Example.py
		 http://www.isystem.com/files/isystem.connect/api/profiler2_example_8py-example.html
		 reading the xml file based on profilerData.py example
		 http://www.isystem.com/files/isystem.connect/api/profiler_data_8py-example.html
 * 
 */
public class ISYSTEMProfiler
{
	private final ISYSTEMTraceDriver _traceDriver;
	private final CProfilerController2 _profilerController2;
	private final int _triggerIdx;
	
	private boolean _profileCode = false;
	private boolean _profileData = false;
	
	public ISYSTEMProfiler(ISYSTEMTraceDriver traceDriver, ConnectionMgr connectionMgr)
	{
		_traceDriver = traceDriver;
		
		_profilerController2 = new CProfilerController2(connectionMgr, traceDriver.getTraceTRDFile(), "w");

		_profilerController2.removeTrigger("ptspecTrigger");
		_triggerIdx = _profilerController2.createTrigger("ptspecTrigger");
	}	
	
	/**
	 * profiling may not work if the program has not already advanced to the main method (use runUntil)
	 */
	public void run()
	{
		if (!_profileCode && !_profileData)
			return;
		
		// make the created trigger the active one
		_profilerController2.select(_triggerIdx);

		// profile code and data
		_profilerController2.setProfilingSections(_triggerIdx, _profileCode, _profileData, false, false);

		_profilerController2.waitUntilLoaded();
		_profilerController2.start();
	}
	
	public boolean exportData()
	{
		if (!_profileCode && !_profileData)
			return false;

		CProfilerExportConfig exportCfg = new CProfilerExportConfig();
		
		exportCfg.setFileName(_traceDriver.getTraceXMLFile());
		exportCfg.setAreaScope(CProfilerExportConfig.EAreaScope.EAreaAll); 
		exportCfg.setAreaExportSections(true, true, true, false); 
		exportCfg.setSaveTimeline(true);
		exportCfg.setSaveStatistics(true);

		_profilerController2.waitUntilLoaded();
		_profilerController2.exportData(exportCfg);
		
		return true;
	}
	
	public void addFunction(String name)
	{
		_profilerController2.addFunction(_triggerIdx, name);
		_profileCode = true;
	}
	
	public void addVariable(String name)
	{
		_profilerController2.addVariable(_triggerIdx, name, CProfilerController2.EDataAreaType.EDATRegular);
		_profileData = true;
	}
}
