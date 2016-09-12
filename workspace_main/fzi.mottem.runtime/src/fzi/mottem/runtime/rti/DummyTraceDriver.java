package fzi.mottem.runtime.rti;

import java.util.List;

import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.runtime.interfaces.ITrace;

public class DummyTraceDriver extends AbstractTraceDriver
{

	public DummyTraceDriver(ITrace trace, AbstractAccessDriver inspector)
	{
		super(trace, inspector);
	}

	@Override
	public void configure(List<ITestReferenceable> elementToCapture)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void runAndTrace()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void fillTraceDB()
	{
		// TODO Auto-generated method stub
	}

}
