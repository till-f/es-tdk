package fzi.mottem.runtime.rti.vector;

import java.util.List;

import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.runtime.TraceDB;
import fzi.mottem.runtime.TraceDB.TraceDBEvent;
import fzi.mottem.runtime.interfaces.ITrace;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import fzi.mottem.runtime.rti.AbstractTraceDriver;

public class VectorTraceDriver extends AbstractTraceDriver
{
	private final VectorAccessDriver _vectorAccessDriver;
	private final TraceDB _traceDB;
	
	private long _startTime = -1;
	
	public VectorTraceDriver(ITrace trace, AbstractAccessDriver inspector)
	{
		super(trace, inspector);
		
		_vectorAccessDriver = (VectorAccessDriver)_accessDriver;
		_traceDB = getTrace().getTraceDB();
	}

	@Override
	public void configure(List<ITestReferenceable> elementToCapture)
	{
		// rt stim not yet suported.
		// this trace driver currently captures everything.
		// --> nothing to configure
	}
	
	@Override
	public void runAndTrace()
	{
		_vectorAccessDriver.registerTraceDriver(this);
	}

	@Override
	public void fillTraceDB()
	{
		_vectorAccessDriver.unregisterTraceDriver();
	}

	public void insertValue(String elementUID, String valueStr)
	{
		if (_startTime < 0)
			_startTime = System.currentTimeMillis();
		
		_traceDB.insertValue(TraceDBEvent.Write, elementUID, valueStr);
		//_traceDB.insertValueMS(Double.valueOf(System.currentTimeMillis() - _startTime), TraceDBEvent.Write, elementUID, valueStr);
	}
}
