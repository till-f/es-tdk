package fzi.mottem.runtime.plotting.interfaces;

import java.util.HashMap;

import fzi.mottem.runtime.plotting.PTSpecXYDataTreeMap;

public interface IPTSpecPlot
{
	
	public void addPlotItem(String identifier);
	
	public HashMap<String, PTSpecXYDataTreeMap> getPlotItems();
	
	public String getPlotName();

}
