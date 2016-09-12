package fzi.mottem.runtime.dataexchanger;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import fzi.mottem.runtime.dataexchanger.interfaces.ITargetDataConsumer;

//for testing purposes only
public class Dummy implements Runnable {

	Thread t;

	private String name = "Dummy";
	long time;
	long startTime;
	int scaleFactor = 1;
	int[] timesMS = { 500000000, 300000000, 80000000, 50000000 };
	private boolean running = true;
	
	ArrayList<DEMessage> signals;

	double value;
	double value2;
	int i = 1;
	int j = 1;
	int max = 1;

	public int getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(int scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public void setTimes(int[] times) {

		this.timesMS = times;

		for (int k = 0; k < timesMS.length; k++) {
			if (max < timesMS[k])
				max = timesMS[k];
		}

	}

	private void setSignals() {
		signals = new ArrayList<DEMessage>();
		/*signals.add(new DEMessage(this.hashCode(), 0, name));
		signals.add(new DEMessage(this.hashCode(), 1, name));
		signals.add(new DEMessage(this.hashCode(), 2, name));
		signals.add(new DEMessage(this.hashCode(), 3, name));*/
	}

	public Dummy(String name) {
		this.name = name;
		startTime = System.nanoTime() / 1000000;
		setSignals();
		
		for (int k = 0; k < timesMS.length; k++) {
			if (max < timesMS[k])
				max = timesMS[k];
		}
	}

	public String getName() {
		return name;
	}

	public void addConsumer(ITargetDataConsumer c) {
	}

	public void start() {
		System.out.println("Dummy - Starting " + name);
		if (t == null) {
			t = new Thread(this, name);
			t.start();
		}
	}

	public void run() {
		running = true; 
		System.out.println(name + ": run ()");
		for (int k = 0; k < timesMS.length; k++) {
			if (max < timesMS[k])
				max = timesMS[k];
		}
	
		 Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	if(running) {
	            		//System.out.println("Dummy: Sending to consumer...");
	            		value = Math.sin((double)i/10)*4 + 4;
		            	time = (System.nanoTime() / 1000000) - startTime;
		               // c.consume(signals.get(0), value , time);
		                i++;
	            	}          	
	            }
	        }, 100, 100);
	        
	        Timer timer2 = new Timer();
	        timer2.schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	if(running) {
	            		//System.out.println("Dummy: Sending to consumer...");
	            		value2 = Math.sin((double)j/10) + 1;
		            	time = (System.nanoTime() / 1000000) - startTime;
		                //c.consume(signals.get(1), value2 , time);
		                j++;
	            	}          	
	            }
	        }, 300, 300);

	}
}
