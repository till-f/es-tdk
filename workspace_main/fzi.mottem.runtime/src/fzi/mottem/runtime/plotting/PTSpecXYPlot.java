package fzi.mottem.runtime.plotting;

import java.util.HashMap;

import fzi.mottem.runtime.plotting.interfaces.IPTSpecPlot;

public class PTSpecXYPlot implements IPTSpecPlot
{
	private final HashMap<String, PTSpecXYDataTreeMap> _plotTreeMaps = new HashMap<String, PTSpecXYDataTreeMap>();
	
	private final String _plotName;
	
	public PTSpecXYPlot(String plotName)
	{
		_plotName = plotName;
	}
	
	@Override
	public void addPlotItem(String identifier)
	{
		_plotTreeMaps.put(identifier, new PTSpecXYDataTreeMap(identifier));
	}
	
	@Override
	public HashMap<String, PTSpecXYDataTreeMap> getPlotItems()
	{
		return _plotTreeMaps;
	}
	
	public void addPoint(String identifier, Double x, Double y)
	{
		_plotTreeMaps.get(identifier).addPoint(x, y);
	}

	@Override
	public String getPlotName()
	{
		return _plotName;
	}

}
