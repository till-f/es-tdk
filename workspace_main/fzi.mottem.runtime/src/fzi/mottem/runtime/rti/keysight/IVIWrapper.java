package fzi.mottem.runtime.rti.keysight;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;

public class IVIWrapper
{

	private double results[] = {0};
	private int sampleRateReduceFactor = 1;
	
	private static final String ARCHITECTURE_KEY = "sun.arch.data.model";
	private static final String ARCHITECTURE_32 = "32";
	private static final String ARCHITECTURE_64 = "64";

	private static final String LIB_NAME_64_MAIN = "lib/KeysightIVIWrapperx64";
	private static final String LIB_NAME_64_DEP1 = "lib/AgInfiniiVision_64";
	private static final String LIB_NAME_64_DEP2 = "lib/IviCShared_64";
	private static final String LIB_NAME_32_MAIN = "lib/KeysightIVIWrapper";
	private static final String LIB_NAME_32_DEP1 = "lib/AgInfiniiVision";
	private static final String LIB_NAME_32_DEP2 = "lib/IviCShared";

	static
	{
		String architecture = System.getProperty(ARCHITECTURE_KEY);

		if (ARCHITECTURE_64.equals(architecture))
		{
			System.loadLibrary(LIB_NAME_64_DEP2);
			System.loadLibrary(LIB_NAME_64_DEP1);
			System.loadLibrary(LIB_NAME_64_MAIN);
		}
		else if (ARCHITECTURE_32.equals(architecture))
		{
			System.loadLibrary(LIB_NAME_32_DEP2);
			System.loadLibrary(LIB_NAME_32_DEP1);
			System.loadLibrary(LIB_NAME_32_MAIN);
		}
		else
		{
		    throw new IllegalStateException("Unknown architecture:" + architecture);
		}
	}
	
	public void connect(String connectURL, boolean simulation)
	{
		String agilentConnectURL = "TCPIP0::" + connectURL + "::INSTR";
		
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
		   public Object call() {
				jniConnect(agilentConnectURL, simulation);
				return null;
		   }
		};
		
		Future<Object> future = executor.submit(task);
		try
		{
		   future.get(5, TimeUnit.SECONDS); 
		   System.out.println("IVI CONNECTED");
		} 
		catch (TimeoutException ex)
		{
		   System.out.println("IVI TIMEOUT");
		   future.cancel(true);
		   throw new RuntimeException(ex);
		}
		catch (InterruptedException | ExecutionException ex) 
		{
			ex.printStackTrace();
			future.cancel(true);
			throw new RuntimeException(ex);
		}
	}
	
	public void disconnect()
	{
		jniDisconnect();
	}
	
	public void setSampleRateReduceFactor(int i) {
		this.sampleRateReduceFactor = i;
	}
	
	public void printData(double[] results) {
		System.out.println("RESULTS: " + results.length);
		for (int i = 0; i < results.length; i =+sampleRateReduceFactor) {
			System.out.println(results[i]);
		}
	}
	
	public void display() {
		DataVisualization test = new DataVisualization(results, sampleRateReduceFactor);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);
        f.setSize(600,500);
        f.setLocation(200,200);
        f.setVisible(true);
	}
	
	public double[] getWaveform()
	{
		return jniGetWaveform();
	}
	
	public double[] trigger(double d, boolean b) 
	{
		return jniTrigger(d, b);
	}
	
	public double[] widthTrigger(double lvl, double lo, double hi, int polarity, int condition)
	{
		return jniWidthTrigger(lvl, lo, hi, polarity, condition);
	}
	
	public void setResults(double[] d) {
		results = new double[d.length/sampleRateReduceFactor];
		int j = 0;
		for (int i=0; i < d.length; i += sampleRateReduceFactor) {
			results[j] = d[i];
			j++;
		}
	}
	
	public int getSampleRateReduceFactor() {
		return this.sampleRateReduceFactor;
	}
	
	public double[] getResults() {
		return this.results;
	}
	
	public double getSampleRate() {
		return jniGetSampleRate();
	}
	
	public double getStartTime() {
		return jniGetStartTime();
	}
	
	public double getAquisitionStartTime() {
		return jniGetAquisitionStartTime();
	}
	
	public double[] getScaledWaveform(double scalefactor) {
		return jniGetScaledWaveform(scalefactor);
	}
	
	public long getTriggerDelay() {
		return jniGetTriggerDelay();
	}
	
	public double[] scaledTrigger(double trigger, boolean b, double scalefactor) {
		return jniScaledTrigger(trigger, b, scalefactor);
	}
	
	private native double[] jniGetScaledWaveform(double scalefactor);
	
	private native double[] jniScaledTrigger(double d, boolean b, double scalefactor);
	
	private native double jniGetSampleRate();
	
	private native double jniGetStartTime();
	
	private native double jniGetAquisitionStartTime();
	
	private native void jniConnect(String connectURL, boolean simulation);

	private native void jniDisconnect();
	
	private native long jniGetTriggerDelay();

	private native double[] jniGetWaveform();
	
	private native double[] jniTrigger(double d, boolean b);
	
	private native double[] jniWidthTrigger(double lvl, double lo, double hi,int polarity, int condition);

}
