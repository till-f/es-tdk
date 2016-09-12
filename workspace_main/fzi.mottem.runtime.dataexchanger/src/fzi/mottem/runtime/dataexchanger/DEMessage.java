package fzi.mottem.runtime.dataexchanger;

/**
 * A lightweight class for saving Signal data consisting of three fields - 
 * a type, a given integer source id and a signalID . 
 * @author Kalin Katev
 */
public class DEMessage {
	
	private String signalID = "Unnamed";
	
	private Signal signal;
	private double value;
	private long timestamp = 0;
	
	private int source;
	private int type;
	
	public String getSignalID() {
		return signalID;
	}

	public void setSignal_ID(String ID) {
		this.signalID = ID;
	}
	
	public void setSignal_ID(int id) {
		this.type = id;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String toString() {	
		return signalID + " - " + type;	
	}
	
	// --------- new implementation -----------
	
	/**
	 * Creates a new DEMessage instance with the given parameters. 
	 * This object is used to transmit information to the DataExchanger.
	 * @param signal
	 * @param value
	 * @param timestamp
	 */
	public DEMessage(Signal signal, double value, long timestamp) {
		this.signal = signal;
		this.value = value;
		this.timestamp = timestamp;
		if(signal != null) {
			this.signalID = signal.getId();
		}	
	}
	/**
	 * Creates a new DEMessage instance with the given parameters and time stamp = 0.
	 * This object is used to transmit information to the DataExchanger.
	 * @param signal
	 * @param value
	 */
	public DEMessage(Signal signal, double value) {
		this.signal = signal;
		this.value = value;
		this.timestamp = 0;
		if(signal != null) {
			this.signalID = signal.getId();
		}	
	}
	
	public Signal getSignal() {
		return signal;
	}

	public void setSignal(Signal signal) {
		this.signal = signal;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	
}
