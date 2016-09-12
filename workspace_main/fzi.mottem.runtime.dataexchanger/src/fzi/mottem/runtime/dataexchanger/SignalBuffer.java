package fzi.mottem.runtime.dataexchanger;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public class SignalBuffer {
	
	DoubleBuffer valueBuffer;
	IntBuffer timestampBuffer;
	boolean write_time = true;
	int type;
	
	private boolean write_locked = false;
	
	private boolean read_locked = false;
	
	public boolean writeLocked() {
		return write_locked;
	}
	public boolean readLocked() {
		return read_locked;
	}
	
	
	public SignalBuffer(int type) {
		this.type = type;
		valueBuffer = DoubleBuffer.allocate(Constants.DEFAULT_CAPACITY);
		timestampBuffer = IntBuffer.allocate(Constants.DEFAULT_CAPACITY);
	}
	
	public SignalBuffer(int type, boolean write_time) {
		this.type = type;
		valueBuffer = DoubleBuffer.allocate(Constants.DEFAULT_CAPACITY);
		timestampBuffer = IntBuffer.allocate(Constants.DEFAULT_CAPACITY);
		this.write_time = write_time;
	}
	
	public synchronized void addValue(double value){
		valueBuffer.put(value);
	}
	public synchronized void addValue(double value, int timestamp){
		valueBuffer.put(value);
		timestampBuffer.put(timestamp);
	}
//	public void addTimestamp(int timestamp) {
//		timestampBuffer.put(timestamp);
//	}
	
	public DoubleBuffer getValueBuffer() {
		return valueBuffer;
	}
	public IntBuffer getTimestampBuffer() {
		return timestampBuffer;
	}
	
}
