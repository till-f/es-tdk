package fzi.mottem.runtime.rti.interfaces;

import java.util.List;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.interfaces.ITrace;

public interface ITraceDriver
{

	public <T> void addStimulus(double timeStamp, IExecutor executor, ITrace.SetupEvent setupEvent, ITestReferenceable element, EItemProperty property, T value, Object... arguments);

	public void configure(List<ITestReferenceable> elementsToCapture);
	
	public void runAndTrace();
	
	public void fillTraceDB();

}
