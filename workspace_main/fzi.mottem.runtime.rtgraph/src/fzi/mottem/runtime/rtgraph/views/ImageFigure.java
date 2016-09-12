package fzi.mottem.runtime.rtgraph.views;

import org.csstudio.swt.widgets.figures.AbstractLinearMarkedFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Image;

public class ImageFigure extends AbstractLinearMarkedFigure {
	
	private Label valueLabel;
	
	private Image image;
	
	public ImageFigure() {
		super();
		
		ToolbarLayout layout2 = new ToolbarLayout();
		   
        layout2.setHorizontal(false);
        layout2.setSpacing(3);
        layout2.setStretchMinorAxis(false);       
        
		setLayoutManager(layout2);
		
		valueLabel = new Label();	
		
		add(valueLabel);	
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public void setImage(Image image) {
		//setOpaque(true);
		this.image = image;
		valueLabel.setIcon(image);
		//this.setSize(image.getImageData().x, image.getImageData().y);
	}
	
	
	
	
}
