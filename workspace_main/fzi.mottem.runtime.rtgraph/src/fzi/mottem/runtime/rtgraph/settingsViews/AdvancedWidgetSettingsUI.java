package fzi.mottem.runtime.rtgraph.settingsViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;

public abstract class AdvancedWidgetSettingsUI extends Composite {
	
	protected IndicatorWidgetLink link;
	
	public IndicatorWidgetLink getLink() {
		return link;
	}

	public void setLink(IndicatorWidgetLink link) {
		this.link = link;
	}

	public AdvancedWidgetSettingsUI(Composite parent, int style, IndicatorWidgetLink link) {
		super(parent, style);
		this.link = link;
		// TODO Auto-generated constructor stub
	}
	public AdvancedWidgetSettingsUI(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public void initLayout(int colSpan, int rowSpan) {
		GridLayout layout = new GridLayout(2, false);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true, colSpan, rowSpan);
		this.setLayout(layout);
		this.setLayoutData(data);	
	}
	
	public abstract void initConten();
	
}
