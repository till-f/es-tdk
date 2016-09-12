package fzi.mottem.runtime.rti.keysight;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.model.testrigmodel.AgilentInspector;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.rti.AbstractAccessDriver;

public class IVIAccessDriver extends AbstractAccessDriver
{
	private final IVIWrapper _iviWrapper;
	
	public IVIWrapper getIVIWrapper()
	{
		return _iviWrapper;
	}
	
	public IVIAccessDriver(IRuntime runtime, IInspector inspector)
	{
		super(runtime, inspector);
		
		_iviWrapper = new IVIWrapper();
	}

	@Override
	public void init()
	{
		String connectURL = ((AgilentInspector)(_inspector)).getAgilentContainer().getConnectURL();
		
		boolean simulation = false; //or do sth here..
		
		_iviWrapper.connect(connectURL, simulation);
	}

	@Override
	public void cleanup()
	{
		_iviWrapper.disconnect();
	}

	@Override
	public <T> T getValue(ITestReadable element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setValue(ITestWriteable element, T value)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public <T> T getItemPropertyValue(ITestReferenceable element, EItemProperty property)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setItemPropertyValue(ITestReferenceable element, EItemProperty property, T value)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public <T> T execute(IExecutor executor, ITestCallable executableElement, Object... arguments)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(IExecutor executor)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void runUntil(IExecutor executor, ITestCallable executableElement)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void stop(IExecutor executor)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void breakAt(IExecutor executor, ITestCallable executableElement)
	{
		// TODO Auto-generated method stub
	}

}
