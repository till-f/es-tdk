package fzi.mottem.runtime.plotting;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import fzi.mottem.runtime.dataexchanger.DEMessage;

public class PTSpecXYDataTreeMap
{
	
	private final String _caption;
	
	private final TreeMap<Double, Double> _plotPoints = new TreeMap<Double, Double>();
	
	private Double lastX = null;
	private Double lastY = null;
	
	public PTSpecXYDataTreeMap(String caption)
	{
		_caption = caption;
	}
	
	public String getCaption()
	{
		return _caption;
	}
	
	public Set<Double> getX()
	{
		return _plotPoints.keySet();
	}
	
	public Double getY(Double X)
	{
		return _plotPoints.get(X);
	}

	public void addPoint(Double x, Double y)
	{
		addPoint(x, y, false);
	}

	private void addPoint(Double x, Double y, boolean sporadic)
	{
		if (lastY != null && lastY.equals(y))
		{
			// if the same y-value is being added multiple times skip it
			// and remember last time it would have been added.
			lastX = x;
			return;
		}
		
		if (sporadic && lastX != null)
		{
			// if y-value(s) have been skipped (see above) add the
			// value one more time before a different y-value is added
			// (this keeps horizontal connections in graph)
			_plotPoints.put(lastX, lastY);
			lastX = null;
		}

		_plotPoints.put(x, y);
		lastY = y;
	}

	public List<DEMessage> getDEMessages()
	{
		LinkedList<DEMessage> msgs = new LinkedList<DEMessage>();
		
		// !TODO improve iteration
		for(Double ts : _plotPoints.keySet())
		{
			msgs.add(new DEMessage(null, _plotPoints.get(ts), ts.longValue()));
		}

		return msgs;
	}

}
