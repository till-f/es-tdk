package fzi.mottem.runtime.rtgraph.views;

import org.csstudio.swt.widgets.figures.AbstractLinearMarkedFigure;
import org.csstudio.swt.xygraph.util.XYGraphMediaFactory;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class TextFigure extends AbstractLinearMarkedFigure {
	
	private final Font DEFAULT_LABEL_FONT = XYGraphMediaFactory.getInstance().getFont(
			new FontData("Arial", 11, SWT.NORMAL));
	
	private Label valueLabel;
	private Label textLabel;
	private boolean emptyText;
	
	public TextFigure() {
		super();
		
		//
		setTransparent(true);
		setOpaque(false);
		
		valueLabel = new Label();	
		valueLabel.setFont(DEFAULT_LABEL_FONT);
		textLabel = new Label();	
		textLabel.setFont(DEFAULT_LABEL_FONT);
				
		ToolbarLayout layout2 = new ToolbarLayout();
   
        layout2.setHorizontal(false);
        layout2.setSpacing(3);
        layout2.setStretchMinorAxis(false);       
        
		setLayoutManager(layout2);
	
		add(textLabel);
		add(valueLabel);	
	}
	
	@Override
	public void setValue(double value) {
		super.setValue(value);
		if(emptyText) {
			textLabel.setText(getValueText());
		} else {
			valueLabel.setText(getValueText());
		}			
	}
	
	public void setText(String text) {
		textLabel.setText(text);
		if(text.length() == 0) {
			valueLabel.setText("");
			emptyText = true;
		} else {
			emptyText = false;
		}
	}
	public void setTextFont(Font f) {
		textLabel.setFont(f);
	}
	
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setAlpha(100);
	}

	public void setValueFont(Font fvalue) {
		valueLabel.setFont(fvalue);
	}

}
