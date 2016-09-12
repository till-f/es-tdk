package fzi.mottem.runtime.dataexchanger.interfaces;

import java.util.Collection;
import java.util.List;

import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.runtime.dataexchanger.Signal;

public interface IDataExchanger extends IConsumingEntity {
	
	public void setUpSignals(Collection<ITestReadable> readables);
	public void setUpSignal(ITestReadable r);
	
	public void registerConsumer( Signal signal, ITargetDataConsumer c);
	public void removeConsumer(ITargetDataConsumer c);
	
	public Signal getSignal(String signalID);
	public List<Signal> getSignals();
	
	public void setUpSignal(String uid, String simpleName);
	

}
