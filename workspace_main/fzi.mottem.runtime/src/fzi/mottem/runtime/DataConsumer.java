package fzi.mottem.runtime;

import java.util.Hashtable;
import java.util.List;

import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.interfaces.ITargetDataConsumer;
import fzi.mottem.runtime.rti.interfaces.IAccessDriver;
import fzi.mottem.runtime.util.UIDResolver;

public class DataConsumer implements ITargetDataConsumer 
{
	private final UIDResolver _uidResolver;
	
	private Hashtable<ITestReferenceable, IAccessDriver> _driversByTRef = new Hashtable<ITestReferenceable, IAccessDriver>();
	
	public DataConsumer(UIDResolver uidResolver)
	{
		_uidResolver = uidResolver;
	}

	@Override
	public void consume(DEMessage message)
	{
		Double value = message.getValue();
		String signalUID = message.getSignalID();
		
		ITestReferenceable tRef = (ITestReferenceable)_uidResolver.getElement(signalUID);
		
		IAccessDriver accessDrier = _driversByTRef.get(tRef);
		
		if (tRef instanceof ITestWriteable)
		{
			accessDrier.setValue((ITestWriteable)tRef, value);
		}
	}

	@Override
	public void consumeBurst(List<DEMessage> messages)
	{
		// not supported
	}

	@Override
	public void drop()
	{
		// TODO what do I have to implement here?
	}

	public void setAccessDriverForTRef(ITestReferenceable tRef, IAccessDriver accessDriver)
	{
		_driversByTRef.put(tRef, accessDriver);
	}

}
