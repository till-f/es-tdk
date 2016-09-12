package fzi.mottem.runtime.rti.keil;

public class UVSocketJniJava 
{
	
	static
	{
		System.loadLibrary("lib/UVSC");
		System.loadLibrary("lib/uvSockJNI");
	}
	
	/****************** Declare native methods ***********************/
	
	public native int load();
	
	public native int reset();
	
	public native void killBreakpoints();
	
	public native int init();

	public native void closeConnection();

	public native int showUVISION();

	public native int enterDebugMode();

	public native void exitDebugMode();

	public native String setBreakpoint(String addr);

	public native void run();

	public native void stop();

	public native void call(int addrStart, int addrStop);

	public native void runUntil(long addr);

	public native byte[] getValue(long addr, int size);

	public native void setValue(long addr, byte[] value, int size);

	public void setBP(long address)
	{
		setBreakpoint(String.valueOf(address));
	}

}
