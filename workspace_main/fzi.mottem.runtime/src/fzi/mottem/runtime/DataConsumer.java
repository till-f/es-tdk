package fzi.mottem.runtime;

import java.util.Hashtable;
import java.util.LinkedHashMap;
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
	
	private final LinkedHashMap<ITestReferenceable, DEMessage> _stimuli = new LinkedHashMap<ITestReferenceable, DEMessage>();
	private final Object _stimuliRunnerLock = new Object();
	private final Thread _stimuliRunnerThread;


	public DataConsumer(UIDResolver uidResolver)
	{
		_uidResolver = uidResolver;
		
		Runnable backgroundTask = new Runnable()
			{
				@Override
				public void run()
				{
					asyncStimuliRunnerEntry();
				}
			};
		
		_stimuliRunnerThread = new Thread(backgroundTask, "Async Stimuli Runner");
		_stimuliRunnerThread.start();
	}
	
	@Override
	public void consume(DEMessage message)
	{
		String signalUID = message.getSignalID();
		ITestReferenceable tRef = (ITestReferenceable)_uidResolver.getElement(signalUID);

		synchronized (_stimuliRunnerLock)
		{
			_stimuli.put(tRef, message);
			_stimuliRunnerLock.notify();
		}
	}
	
	@Override
	public void consumeBurst(List<DEMessage> messages)
	{
		// not yet supported
	}

	@Override
	public void drop()
	{
		_stimuliRunnerThread.interrupt();
	}

	public void setAccessDriverForTRef(ITestReferenceable tRef, IAccessDriver accessDriver)
	{
		_driversByTRef.put(tRef, accessDriver);
	}
	
	private void asyncStimuliRunnerEntry()
	{
		while (true)
		{
			DEMessage message;
			ITestReferenceable nextTRef;
			
			synchronized (_stimuliRunnerLock)
			{
				if (_stimuli.keySet().isEmpty())
				{
					try
					{
						_stimuliRunnerLock.wait(); // should release _stimuliRunnerLock when waiting (really?)
					}
					catch (InterruptedException e)
					{
						return;
					}
				}

				nextTRef = _stimuli.keySet().iterator().next();
				message = _stimuli.get(nextTRef);
				_stimuli.remove(nextTRef);
			}

			Double value = message.getValue();
			
			IAccessDriver accessDriver = _driversByTRef.get(nextTRef);
			
			if (nextTRef instanceof ITestWriteable)
			{
				accessDriver.setValue((ITestWriteable)nextTRef, value);
			}
			
			if (_stimuliRunnerThread.isInterrupted())
				return;
		}
	}

}
