package fzi.mottem.runtime.rti.vector;

import java.util.HashMap;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspectable;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.INetworkConnector;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.model.datastreammodel.CANMessage;
import fzi.mottem.model.datastreammodel.DataStreamInstance;
import fzi.mottem.model.datastreammodel.MessageSignal;
import fzi.mottem.model.testrigmodel.CANBus;
import fzi.mottem.model.testrigmodel.CANInspectorPort;
import fzi.mottem.model.testrigmodel.CANPort;
import fzi.mottem.model.util.ModelUtils;
import fzi.mottem.ptspec.dsl.common.PTSpecUtils;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.rti.AbstractAccessDriver;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class VectorAccessDriver extends AbstractAccessDriver
{
	private final VectorXLWrapper _xlWrapper;
	
	private VectorTraceDriver _traceDriver;
	
	HashMap<Integer, CANMessage> _canIdToSignal = new HashMap<Integer, CANMessage>();
	
	public VectorAccessDriver(IRuntime runtime, IInspector inspector)
	{
		super(runtime, inspector);
		
		if (inspector instanceof CANInspectorPort)
		{
			_xlWrapper = new VectorXLWrapper(this, ((CANInspectorPort) inspector).getChannelNumber());
			IInspectable inspectable = inspector.getInspectorConnector().getInspectable();
			
			if (!(inspectable instanceof CANBus))
				throw new RuntimeException("VectorXLWrapper not compatible with inspectable of type " + inspectable.getClass().getSimpleName());
			
			CANBus canBus = (CANBus)inspectable;
			
			for (INetworkConnector connector : canBus.getNetworkConnector())
			{
				if (!(connector.getNetworkPort() instanceof CANPort))
					throw new RuntimeException("VectorXLWrapper not compatible with network port of type " + connector.getNetworkPort().getClass().getSimpleName());
				
				CANPort canPort = (CANPort) connector.getNetworkPort();			
				DataStreamInstance dsi = (DataStreamInstance)canPort.getSymbolContainer();
				
				for (CANMessage msg : dsi.getCanMessages())
				{
					_canIdToSignal.put(msg.getCanID(), msg);
				}
			}
		}
		else
		{
			throw new RuntimeException("VectorXLWrapper not compatible with inspector of type " + inspector.getClass().getSimpleName());
		}
	}

	
	// ------------------------------------------------------------
	// AbstractInspector base class
	// ------------------------------------------------------------
	
	@Override
	public void init()
	{
		boolean success = _xlWrapper.connect();
		
		if (!success)
		{
			throw new RuntimeException("VectorXLWrapper failed to connect");
		}
	}

	@Override
	public void cleanup()
	{
		_xlWrapper.disconnect();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(ITestReadable element)
	{
		if (!(element instanceof MessageSignal))
			throw new RuntimeException("VectorXLWrapper not compatible with elements of type " + element.getClass().getSimpleName());
		
		MessageSignal signal = (MessageSignal)element;
		
		if (signal.getCanMessage() == null)
			throw new RuntimeException("VectorXLWrapper expects signals contained in CAN messages; not the case for signal " + signal.getName());
		
		byte[] data = _xlWrapper.getMessageData(signal.getCanMessage().getCanID(), signal.getBitOffset(), signal.getBitLength());	
		
		double dataDouble = ModelUtils.rawByteArrayToDouble(data, signal);
		
		return (T)Double.valueOf(dataDouble);
	}

	@Override
	public <T> void setValue(ITestWriteable element, T value)
	{
		if (_traceDriver != null)
			_traceDriver.insertValue(PTSpecUtils.getElementUID(element), String.valueOf(value));

		if (!(element instanceof MessageSignal))
			throw new RuntimeException("VectorXLWrapper not compatible with elements of type " + element.getClass().getSimpleName());
		
		MessageSignal signal = (MessageSignal)element;
		
		if (signal.getCanMessage() == null)
			throw new RuntimeException("VectorXLWrapper expects signals contained in CAN messages; not the case for signal " + signal.getName());

		byte[] valueRAW = ModelUtils.doubleToRawByteArray(((Number)value).doubleValue(), signal);
		
		_xlWrapper.updateAndSendMessageData(signal.getCanMessage().getCanID(), valueRAW, signal.getBitOffset(), signal.getBitLength(), signal.getCanMessage().getByteCount());
	}

	@Override
	public <T> T getItemPropertyValue(ITestReferenceable element, EItemProperty property)
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public <T> void setItemPropertyValue(ITestReferenceable element, EItemProperty property, T value)
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public <T> T execute(IExecutor executor, ITestCallable executableElement, Object... arguments)
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void run(IExecutor executor)
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void runUntil(IExecutor executor, ITestCallable executableElement)
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void stop(IExecutor executor)
	{
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void breakAt(IExecutor executor, ITestCallable executableElement)
	{
		throw new NotImplementedException();
	}


	// ------------------------------------------------------------
	// For tracing / realtime UI
	// ------------------------------------------------------------

	public void registerTraceDriver(VectorTraceDriver traceDriver)
	{
		_traceDriver = traceDriver;
	}
	
	public void unregisterTraceDriver()
	{
		_traceDriver = null;
	}
	
	public void notifyMessage(int canID, byte[] messageData)
	{
		if (_canIdToSignal.get(canID) == null)
			return;
		
		CANMessage msg = _canIdToSignal.get(canID);
		String messageUID = PTSpecUtils.getElementUID(msg);
		_runtime.notifyUpdate(messageUID, 0);
		
		for (MessageSignal signal : msg.getSignals())
		{
			double value = ModelUtils.rawByteArrayToDouble(messageData, signal);
			
			String signalUID = PTSpecUtils.getElementUID(signal);
			
			if (_traceDriver != null)
				_traceDriver.insertValue(signalUID, String.valueOf(value));

			_runtime.notifyUpdate(signalUID, value);
		}

	}
}


