package fzi.mottem.runtime.interfaces;

import org.csstudio.swt.widgets.figures.AbstractMarkedWidgetFigure;

import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;

public interface IMarkedFigureRepresentable extends IRepresentable<WidgetRepresentation>{
	
	public AbstractMarkedWidgetFigure getFigure();
	public void setFigure(AbstractMarkedWidgetFigure figure);
	
	public void applyRepresentation(boolean applySignal, boolean applyFigure, boolean applyCanvas);
	public void updateRepresentation(boolean figureUpdated, boolean canvasUpdated, boolean linkUpdated);
	public void delete();
	
	public int getType();
	public void updateFigure(AbstractMarkedWidgetFigure figure);
	public void setFigureRange(double min, double max);
	
	public void mark(boolean mark);
}
