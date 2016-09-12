package fzi.mottem.runtime.rtgraph.runnables;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import fzi.mottem.runtime.interfaces.IViewComponentUpdater;
import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink;
import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink.WidgetType;
import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;

public class WidgetUpdater implements IViewComponentUpdater {
	
	ArrayList<AbstractWidgetExchangeLink> widgetLinks;
	public ArrayList<AbstractWidgetExchangeLink> getWidgetLinks() {
		return widgetLinks;
	}

	public void setWidgetLinks(ArrayList<AbstractWidgetExchangeLink> widgetLinks) {
		this.widgetLinks = widgetLinks;
	}

	private int widgetPollInterval;
	
	public int getWidgetPollInterval() {
		return widgetPollInterval;
	}

	public void setWidgetPollInterval(int widgetPollInterval) {
		this.widgetPollInterval = widgetPollInterval;
	}

	public WidgetUpdater(ArrayList<AbstractWidgetExchangeLink> widgetLinks, int pollInterval) {
		this.widgetLinks = widgetLinks;
		this.widgetPollInterval = pollInterval;
	}
	
	@Override
	public void run() {
		update();
		Display.getCurrent().timerExec(widgetPollInterval, this);
	}

	@Override
	public void update() {
		for (AbstractWidgetExchangeLink link : widgetLinks) {
			if(link.getWidgetType() == WidgetType.W_INDICATOR) {
				if (link.updated) {
					((IndicatorWidgetLink)link).updateFigure();
				}
			}
			
		}
	}

}
