package fzi.mottem.runtime.rti;

import java.util.HashSet;

import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.rti.interfaces.IAccessDriver;

public abstract class AbstractAccessDriver implements IAccessDriver
{
	protected final IInspector _inspector;
	protected final IRuntime _runtime;
	
	protected HashSet<ITestReferenceable> _tRefs;

	public AbstractAccessDriver(IRuntime runtime, IInspector inspector)
	{
		_inspector = inspector;
		_runtime = runtime;
	}
	
	public IInspector getInspector()
	{
		return _inspector;
	}

	public HashSet<ITestReferenceable> getTRefs()
	{
		return _tRefs;
	}

	public void setTRefs(HashSet<ITestReferenceable> tRefs)
	{
		_tRefs = tRefs;
	}
}
