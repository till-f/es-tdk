package fzi.mottem.runtime.rti;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.interfaces.IRuntime;

public class DummyAccessDriver extends AbstractAccessDriver
{

	public DummyAccessDriver(IRuntime runtime, IInspector inspector)
	{
		super(runtime, inspector);
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void cleanup()
	{
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(ITestReadable element)
	{
		// TODO Auto-generated method stub
		Variable var = (Variable)element;
		
		if (var.getDataType() instanceof DTInteger)
			return (T) Integer.valueOf(42);
		else
			return (T) Double.valueOf(42);			
	}

	@Override
	public <T> void setValue(ITestWriteable element, T value)
	{
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getItemPropertyValue(ITestReferenceable element, EItemProperty property)
	{
		// TODO Auto-generated method stub
		Variable var = (Variable)element;

		if (var.getDataType() instanceof DTInteger)
			return (T) Integer.valueOf(42);
		else
			return (T) Double.valueOf(42);			
	}

	@Override
	public <T> void setItemPropertyValue(ITestReferenceable element, EItemProperty property, T value)
	{
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(IExecutor executor, ITestCallable executableElement, Object... arguments)
	{
		// TODO Auto-generated method stub
		Function f = (Function)executableElement;

		if (f.getDataType() instanceof DTInteger)
			return (T) Integer.valueOf(42);
		else
			return (T) Double.valueOf(42);			
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
