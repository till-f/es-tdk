package fzi.mottem.runtime.rti.vector;

import java.util.Hashtable;

import fzi.util.BitMagic;

public class VectorXLWrapper
{

	private static final String ARCHITECTURE_KEY = "sun.arch.data.model";
	private static final String ARCHITECTURE_32 = "32";
	private static final String ARCHITECTURE_64 = "64";

	private static final String LIB_NAME_64_MAIN = "lib/VectorXLWrapperx64";
	private static final String LIB_NAME_64_DEP1 = "lib/vxlapi64";
	private static final String LIB_NAME_32_MAIN = "lib/VectorXLWrapper";
	private static final String LIB_NAME_32_DEP1 = "lib/vxlapi";

	static
	{
		String architecture = System.getProperty(ARCHITECTURE_KEY);

		if (ARCHITECTURE_64.equals(architecture))
		{
			System.loadLibrary(LIB_NAME_64_DEP1);
			System.loadLibrary(LIB_NAME_64_MAIN);
		}
		else if (ARCHITECTURE_32.equals(architecture))
		{
			System.loadLibrary(LIB_NAME_32_DEP1);
			System.loadLibrary(LIB_NAME_32_MAIN);
		}
		else
		{
		    throw new IllegalStateException("Unknown architecture:" + architecture);
		}
	}
	
	private VectorAccessDriver _inspector;
	
	private int _portHandle = -1;
	public int getPortHandle() { return _portHandle; }
	public void setPortHandle(int handle) { _portHandle = handle; }

	private int _channel = 0;
	public int getChannel() { return _channel; }
	public void setChannel(int number) { _channel = number; }

	private int _cancelRXRequested = 0;
	public int getCancelRXRequest() { return _cancelRXRequested; }

	private Thread _rxThread = null;
	private Hashtable<Integer, byte[]> _lastMessagesByID = new Hashtable<Integer, byte[]>();
	private Object _lastMessagesWriteLock = new Object(); 
	
	public VectorXLWrapper(VectorAccessDriver inspector, int cMask)
	{
		_channel = cMask;
		_inspector = inspector;
		
	}
	
	public boolean connect()
	{
		if (_rxThread != null && _rxThread.isAlive())
			throw new RuntimeException("CAN RX thread is running; disconnect first");
		
		_cancelRXRequested = 0;
		
		_portHandle = jniConnect();
		
		if (_portHandle < 0)
			return false;
		
		Runnable rxRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				jniStartRxThread(_portHandle);
			}
		};
		
		
		_rxThread = new Thread(rxRunnable);
		_rxThread.setName("CANRXThread_Port" + _portHandle);
		_rxThread.start();
		
		return true;
	}
	
	public void disconnect()
	{
		_cancelRXRequested = 1;
		
		jniDisconnect(_portHandle);
	}
	
	public byte[] getMessageData(int id, int startBit, int bitLength)
	{
		if (_lastMessagesByID.containsKey(id))
		{
			return BitMagic.getBits(_lastMessagesByID.get(id), startBit, bitLength);
		}
		else
		{
			return new byte[BitMagic.getByteCount(bitLength)];
		}
	}
	
	public void updateMessageData(int id, byte[] newData, int startBit, int bitLength)
	{
		synchronized(_lastMessagesWriteLock)
		{
			byte[] originalData = getMessageData(id, 0, 64);

			BitMagic.mergeInto(originalData, newData, startBit, bitLength);
			
			_lastMessagesByID.put(id, originalData);
			
		}
	}

	public void updateAndSendMessageData(int id, byte[] newData, int startBit, int bitLength, int bytesToSend)
	{
		synchronized(_lastMessagesWriteLock)
		{
			updateMessageData(id, newData, startBit, bitLength);
			
			byte[] messageData = getMessageData(id, 0, bytesToSend * 8);
			
			jniSendMessage(_portHandle, _channel, id, messageData);
		}
	}
	
	/*
	 * called by receive thread when message is received
	 */
	private void notifyMessage(int id, byte[] messageData)
	{
		updateMessageData(id, messageData, 0, messageData.length * 8);
		_inspector.notifyMessage(id, messageData);
	}

	private native int jniConnect();

	private native void jniDisconnect(int portHandle);

	private native void jniSendMessage(int portHandle, int channelNr, int id, byte[] data);

	private native void jniStartRxThread(int portHandle);

}