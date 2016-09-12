package fzi.mottem.runtime.rtgraph.runnables;

import org.eclipse.swt.widgets.Display;

import fzi.mottem.runtime.interfaces.IViewComponentUpdater;
import fzi.mottem.runtime.rtgraph.TraceExchangeLink;

public class TraceUpdater implements IViewComponentUpdater {
	
	private int tracePollingDelay = 10;
	private TraceExchangeLink traceLink;
	
	public int getTracePollingDelay() {
		return tracePollingDelay;
	}

	public void setTracePollingDelay(int tracePollingDelay) {
		this.tracePollingDelay = tracePollingDelay;
	}

	public void setTraceLink(TraceExchangeLink traceLink) {
		this.traceLink = traceLink;
	}

	public TraceUpdater(TraceExchangeLink traceLink) {
		this.traceLink = traceLink;
	}
	
	@Override
	public void run() {
		if (traceLink.updated || traceLink.burstUpdate) {
			update();
		}
		Display.getCurrent().timerExec(tracePollingDelay, this);
	}
	
	@Override
	public void update() {
		traceLink.updateBuffer();
	}
	
}
