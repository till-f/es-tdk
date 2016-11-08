package fzi.mottem.runtime;

import org.eclipse.core.resources.IProject;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.runtime.rti.interfaces.IAccessDriver;

public class Runtime extends EclipseRuntime
{
	
	public Runtime(IProject eclipseProject)
	{
		super(eclipseProject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(String inspectorUID, String elementUID)
	{
		if (SIMULATION_MODE)
			return (T)Double.valueOf(0.0);

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);
		
		T value = accessDriver.getValue((ITestReadable)_uidResolver.getElement(elementUID));

		if (value instanceof Number)
		{
			this.notifyUpdate(elementUID, ((Number)value).doubleValue());
		}

		return value;
	}

	@Override
	public <T> void setValue(String inspectorUID, String elementUID, T value)
	{
		if (value instanceof Number)
		{
			this.notifyUpdate(elementUID, ((Number)value).doubleValue());
		}
		
		if (SIMULATION_MODE)
			return;

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);
		
		accessDriver.setValue((ITestWriteable)_uidResolver.getElement(elementUID), value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getItemPropertyValue(String inspectorUID, String elementUID, EItemProperty property)
	{
		if (SIMULATION_MODE)
			return (T)Double.valueOf(0.0);

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);
		
		return accessDriver.getItemPropertyValue((ITestReferenceable)_uidResolver.getElement(elementUID), property);
	}
	
	@Override
	public <T> void setItemPropertyValue(String inspectorUID, String elementUID, EItemProperty property, T value)
	{
		if (SIMULATION_MODE)
			return;

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);
		
		accessDriver.setItemPropertyValue((ITestReferenceable)_uidResolver.getElement(elementUID), property, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(String inspectorUID, String executorUID, String executableElementUID, Object... arguments)
	{
		if (SIMULATION_MODE)
			return (T)Double.valueOf(0.0);

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);
		
		return accessDriver.execute((IExecutor)_uidResolver.getElement(executorUID), (ITestCallable)_uidResolver.getElement(executableElementUID), arguments);
	}

	@Override
	public void run(String inspectorUID, String executorUID)
	{
		if (SIMULATION_MODE)
			return;

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);

		accessDriver.run((IExecutor)_uidResolver.getElement(executorUID));
	}

	@Override
	public void runUntil(String inspectorUID, String executorUID, String executableElementUID)
	{
		if (SIMULATION_MODE)
			return;

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);

		accessDriver.runUntil((IExecutor)_uidResolver.getElement(executorUID), (ITestCallable)_uidResolver.getElement(executableElementUID));
	}
	
	@Override
	public void stop(String inspectorUID, String executorUID)
	{
		if (SIMULATION_MODE)
			return;

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);

		accessDriver.stop((IExecutor)_uidResolver.getElement(executorUID));
	}

	@Override
	public void breakAt(String inspectorUID, String executorUID, String executableElementUID)
	{
		if (SIMULATION_MODE)
			return;

		IAccessDriver accessDriver = getAccessDriver(inspectorUID);

		accessDriver.breakAt((IExecutor)_uidResolver.getElement(executorUID), (ITestCallable)_uidResolver.getElement(executableElementUID));
	}
	
}
