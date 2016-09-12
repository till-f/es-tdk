package fzi.mottem.runtime.rti.keil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;

import fzi.mottem.model.baseelements.IExecutor;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.ITestCallable;
import fzi.mottem.model.baseelements.ITestReadable;
import fzi.mottem.model.baseelements.ITestReferenceable;
import fzi.mottem.model.baseelements.ITestWriteable;
import fzi.mottem.model.codemodel.DTInteger;
import fzi.mottem.model.codemodel.DataType;
import fzi.mottem.model.codemodel.Function;
import fzi.mottem.model.codemodel.Variable;
import fzi.mottem.runtime.EItemProperty;
import fzi.mottem.runtime.interfaces.IRuntime;
import fzi.mottem.runtime.rti.AbstractAccessDriver;

public class KeilAccessDriver extends AbstractAccessDriver {

	private UVSocketJniJava _uvSockWrapper;
	 private ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream( 0 );
     private DataOutputStream dataOutputStream = new DataOutputStream( arrayOutputStream );

     
	public KeilAccessDriver(IRuntime runtime, IInspector inspector)
	{
		super(runtime, inspector);

		_uvSockWrapper = new UVSocketJniJava();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init()
	{
		_uvSockWrapper.init();
		_uvSockWrapper.showUVISION();

		_uvSockWrapper.enterDebugMode();
	
		_uvSockWrapper.reset();
		
		try {
			Thread.sleep(4000);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TODO: delete all breakpoints
		//_uvSockWrapper.killBreakpoints(); // not working
	}

	@Override
	public void cleanup()
	{
		_uvSockWrapper.exitDebugMode();
		_uvSockWrapper.closeConnection();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(ITestReadable element)
	{

		Variable var = (Variable) element;
		int sizeOfBytes = 0;
		byte[] bytesArray = null ;
		DTInteger intType = null;
		if (element instanceof Variable) {
			long address = ((Variable) element).getBinaryLocation()	.getAddress();
			DataType t = var.getDataType();

			intType = (DTInteger) t;
			sizeOfBytes = (intType.getBitSize())/8;
			
			bytesArray = new byte[sizeOfBytes];
			bytesArray = _uvSockWrapper.getValue(address, sizeOfBytes);

			StringBuilder sb2 = new StringBuilder();
			for (byte b : _uvSockWrapper.getValue(address, 4)) {
				sb2.append(String.format("%02X ", b));
			}
			
			System.out.println("Data: " + sb2.toString());
		}
		Integer x= new BigInteger(bytesArray).intValue();
		
		return (T) x;
	}

	@Override
	public <T> void setValue(ITestWriteable element, T value)
	{
		Variable var = (Variable) element;
		DTInteger intType = (DTInteger) var.getDataType();
		long addr = var.getBinaryLocation().getAddress();

		try {
			dataOutputStream.writeInt((int) value);

			byte[] id = arrayOutputStream.toByteArray();

			reverse(id);


			_uvSockWrapper.setValue(addr, id, intType.getBitSize());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> T getItemPropertyValue(ITestReferenceable element,
			EItemProperty property)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setItemPropertyValue(ITestReferenceable element,
			EItemProperty property, T value)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T execute(IExecutor executor, ITestCallable executableElement,
			Object... arguments)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(IExecutor executor)
	{

		_uvSockWrapper.run();

	}

	@Override
	public void runUntil(IExecutor executor, ITestCallable executableElement)
	{
		if (executableElement instanceof Function) {
			long address = ((Function) executableElement).getBinaryLocation()
					.getAddress();
			_uvSockWrapper.runUntil(address);
		} else if (executableElement instanceof Variable) {
			long address = ((Variable) executableElement).getBinaryLocation()
					.getAddress();
			_uvSockWrapper.runUntil(address);
		}
	}

	@Override
	public void stop(IExecutor executor)
	{
		_uvSockWrapper.stop();
	}

	@Override
	public void breakAt(IExecutor executor, ITestCallable executableElement)
	{
		long address;
		if (executableElement instanceof Function) {
			address = ((Function) executableElement).getBinaryLocation()
					.getAddress();
			_uvSockWrapper.setBP(address);
		} else if (executableElement instanceof Variable) {
			address = ((Variable) executableElement).getBinaryLocation()
					.getAddress();
			_uvSockWrapper.setBP(address);
		}
	}
	

	public static void reverse(byte[] id) {
	    for (int left = 0, right = id.length - 1; left < right; left++, right--) {
	        // swap the values at the left and right indices
	        byte temp = id[left];
	        id[left]  = id[right];
	        id[right] = temp;
	    }
	}

}
