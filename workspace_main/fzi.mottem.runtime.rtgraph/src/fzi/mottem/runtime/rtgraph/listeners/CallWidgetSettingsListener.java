package fzi.mottem.runtime.rtgraph.listeners;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink;
import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;
import fzi.mottem.runtime.rtgraph.SetupUtilities;

public class CallWidgetSettingsListener implements MouseListener {
	
	AbstractWidgetExchangeLink link;
	
	public CallWidgetSettingsListener(IndicatorWidgetLink link) {
		this.link = link;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		SetupUtilities.presentWidgetSettings(link);
		/*if(link.getType() == Constants.WIDGET_TEXT) {
			Shell shell = new Shell(Display.getCurrent());
			shell.setSize(360, 360);
			shell.setLayout(new GridLayout(2, false));
			AdvancedTextSettingsUI advUI = new AdvancedTextSettingsUI(shell, SWT.NONE, link);
			advUI.initLayout(2, 1);
			advUI.initConten();
			shell.open();
		}*/
	}

	@Override
	public void mouseDown(MouseEvent e) {
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

}
