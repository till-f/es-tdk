package fzi.mottem.runtime.rtgraph.listeners;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import fzi.mottem.runtime.rtgraph.ControllerWidgetLink;
import fzi.mottem.runtime.rtgraph.SetupUnit;

public class CallControllerSettingsListener  implements MouseListener {
	
	ControllerWidgetLink link;
	
	public CallControllerSettingsListener(ControllerWidgetLink link) {
		this.link = link;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		SetupUnit.presentWidgetSettings(link);
	}

	@Override
	public void mouseDown(MouseEvent e) {
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

}
