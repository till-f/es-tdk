package fzi.mottem.runtime.rtgraph;

import java.util.ArrayList;
import java.util.List;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.swt.widgets.Display;

import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.ExchangeLink;
import fzi.mottem.runtime.rtgraph.XML.TraceRepresentation;
import fzi.mottem.runtime.rtgraph.runnables.TraceUpdater;

public class TraceExchangeLink extends ExchangeLink {

	String signalUID = null;
	CircularBufferDataProvider traceDataProvider;
	List<DEMessage> messages = new ArrayList<DEMessage>();
	DEMessage lastMsg = new DEMessage(null, 0, 0);
	TraceUpdater traceUpdater;
	public boolean burstUpdate;
	private TraceRepresentation representation = new TraceRepresentation();
	private int bufferSize = Constants.DEF_GRAPH_BUFFERSIZE;
	

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
		traceDataProvider.setBufferSize(bufferSize);
	}

	public TraceUpdater getTraceUpdater() {
		return traceUpdater;
	}

	public void startUpdater() {
		Display.getCurrent().timerExec(0, traceUpdater);
	}

	public CircularBufferDataProvider getBuffer() {
		return traceDataProvider;
	}

	public TraceExchangeLink(String name) {
		this.traceDataProvider = new CircularBufferDataProvider(true);
		this.name = name;
		traceUpdater = new TraceUpdater(this);
		traceDataProvider.setBufferSize(bufferSize);
	}

	public TraceExchangeLink(CircularBufferDataProvider circularBuffer) {
		this.traceDataProvider = circularBuffer;
		traceUpdater = new TraceUpdater(this);
	}

	/**
	 * Creates a new Trace Exchange Link instance. The instance properties will
	 * be directly read from the trace representation argument and the
	 * constructor will attempt to connect the object to the Data Exchanger
	 * based on the Signal UID in the representation
	 * 
	 * @param traceRepresentation
	 *            the object which represents the properties of this Trace
	 *            Exchange Link
	 */
	public TraceExchangeLink(TraceRepresentation traceRepresentation) {
		this.representation = traceRepresentation;
		this.traceDataProvider = new CircularBufferDataProvider(true);
		this.name = traceRepresentation.getName();
		this.signalUID = traceRepresentation.getSignalUID();
		traceUpdater = new TraceUpdater(this);
		traceDataProvider.setBufferSize(traceRepresentation.getBufferSize());
		DataExchanger.replaceSignal(traceRepresentation.getSignalUID(), this);
	}

	public void updateBuffer() {
		if (updated) {
			update(lastMsg);
			updated = false;
		}

		if (burstUpdate) {
			System.out.println("	Burst update:");
			// this.traceDataProvider.triggerUpdate();
			for (DEMessage msg : messages) {
				/*
				 * System.out.println("	msg: " + msg.getSignal().getSimpleName()
				 * + " " + msg.getValue() + " " + msg.getTimestamp());
				 */
				update(msg);
			}
			burstUpdate = false;
			
		}
	}

	private void update(DEMessage message) {
		traceDataProvider.setCurrentYData(message.getValue(), message.getTimestamp());
	}

	@Override
	public void consume(DEMessage message) {
		//signalUID = message.getSignalID();
		lastMsg = message;
		updated = true;
	}

	@Override
	public void consumeBurst(List<DEMessage> messages) {
		burstUpdate = true;
		this.messages = messages;
		System.out.println(TraceExchangeLink.class.getSimpleName() + " : consumeBurst()");
		System.out.println("	Message count: " + messages.size());
	}

	public String getSignalUID() {
		return signalUID;
	}

	public void setSignalUID(String uid) {
		this.signalUID = uid;
	}

	public TraceRepresentation getRepresentation() {
		return representation;
	}
	
	public void setRepresentation(TraceRepresentation representation) {
		this.representation = representation;
		applyRepresentation();
	}
	
	public void applyRepresentation() {
		this.name = representation.getName();
		this.signalUID = representation.getSignalUID();
		traceUpdater.setTracePollingDelay(representation.getPollingDelay());
		traceDataProvider.setBufferSize(representation.getBufferSize());
		DataExchanger.replaceSignal(representation.getSignalUID(), this);
	}
	
	public void updateRepresentation() {
		representation.setPollingDelay(traceUpdater.getTracePollingDelay());
		representation.setBufferSize(bufferSize);
		representation.setSignalUID(signalUID);
		representation.setName(this.name);
	}
}
