package fzi.mottem.runtime.rtgraph.settingsViews;

import org.csstudio.swt.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;
import fzi.mottem.runtime.rtgraph.listeners.IntegerListener;

public class AdvancedTextSettingsUI extends AdvancedWidgetSettingsUI {
	
	protected Label labelSize;
	protected Label labelAutoScaleText;
	protected Text textSize;
	protected Text textLink;
	protected Button buttonAutoScale;
	protected Button setButton;
	Listener setButtonListener;
	Listener checkAutoScaleListener;
	
	public AdvancedTextSettingsUI(Composite parent, int style, IndicatorWidgetLink link) {
		super(parent, style, link);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initConten() {
		
		Composite col1 = new Composite(this,SWT.NONE); //for size, autoscale
		col1.setLayout(new GridLayout(2, false));
		col1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		
		Composite col2 = new Composite(this,SWT.NONE); //for set btn
		col2.setLayout(new GridLayout(2, false));
		col2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		
		
		labelSize = new Label(col1, SWT.NONE);
		labelSize.setText("Text size:");
		textSize = new Text(col1, SWT.BORDER);
		textSize.addVerifyListener(new IntegerListener());
		/*
		labelAutoScaleText = new Label(col1, SWT.NONE);
		labelAutoScaleText.setText("Autoscale:");
		buttonAutoScale = new Button(col1, SWT.CHECK);
		
		checkAutoScaleListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				//Font f = link.getFigure().getFont();
				//f.getFontData();
			}
		};
		*/
		setButton = new Button(col2, SWT.PUSH);
		setButton.setText("Set");
		setButtonListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				
				link.setText(textLink.getText());	
				
				if(textSize.getText().length() > 0) {
					Font f = XYGraphMediaFactory.getInstance().getFont(
							new FontData("Arial", Integer.valueOf(textSize.getText()), SWT.NORMAL));
					Font fvalue = XYGraphMediaFactory.getInstance().getFont(
							new FontData("Arial", Integer.valueOf(textSize.getText()), SWT.BOLD));
					link.setTextFont(f);
					link.setValueFont(fvalue);
				}			
			}
		};
		setButton.addListener(SWT.Selection, setButtonListener);
			
		textLink = new Text(this, SWT.BORDER | SWT.MULTI);
		textLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		textLink.setText(link.getText());
	}	
}
