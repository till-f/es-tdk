package fzi.mottem.runtime.rtgraph;

import org.csstudio.swt.widgets.figures.AbstractMarkedWidgetFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Canvas;

import fzi.mottem.runtime.dataexchanger.ExchangeLink;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.interfaces.IMarkedFigureRepresentable;
import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;

public abstract class AbstractWidgetExchangeLink extends ExchangeLink implements IMarkedFigureRepresentable {

	public enum WidgetType {
		W_ABSTRACT("Abstract"), W_INDICATOR("Ind"), W_CONTROLLER("Ctrl");

		public static String[] stringValues() {
			String[] result = new String[values().length];
			int i = 0;
			for (WidgetType t : values()) {
				result[i++] = t.toString();
			}
			return result;
		}

		private String name;

		private WidgetType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	protected WidgetType widgetType = WidgetType.W_ABSTRACT;
	protected WidgetRepresentation representation;
	protected AbstractMarkedWidgetFigure figure;
	protected String text = "";
	protected Canvas canvas;
	protected String signalUID = "-uid-";
	protected String simpleName = "-signal-";
	protected DashboardComposite dashboard;
	protected double range_min;
	protected double range_max;
	protected LightweightSystem lws;
	protected Signal signal;

	public DashboardComposite getDashboard() {
		return dashboard;
	}

	public void setDashboard(DashboardComposite dashboard) {
		this.dashboard = dashboard;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * Returns the current Representation data of the Link/Figure pair
	 * 
	 * @return the Representation object
	 */
	@Override
	public WidgetRepresentation getRepresentation() {
		// updateRepresentation(true, true, true);
		return representation;
	}

	/**
	 * Sets the representation options for this ExchangeLink / Figure pair and
	 * applies them on the canvas and figure.
	 * 
	 * @param representation
	 *            The representation object, usually a new instance or coming
	 *            from the StupUnit and current Profile
	 */
	@Override
	public void setRepresentation(WidgetRepresentation representation) {
		if (representation != null) {
			this.representation = representation;
			applyRepresentation();
		} else {
			System.out.println("Widget Consumer: Chosen representation is null, operation aborted");
		}
	}

	@Override
	public void applyRepresentation() {
		applyRepresentation(true, true, true);
	}

	@Override
	public void updateRepresentation() {
		updateRepresentation(true, true, true);

	}

	@Override
	public AbstractMarkedWidgetFigure getFigure() {
		return figure;
	}

	@Override
	public void setFigure(AbstractMarkedWidgetFigure figure) {
		this.figure = figure;
	}

	@Override
	public abstract void applyRepresentation(boolean applySignal, boolean applyFigure, boolean applyCanvas);

	@Override
	public void updateRepresentation(boolean figureUpdated, boolean canvasUpdated, boolean linkUpdated) {
		
		if (figureUpdated) {
			representation.setRangeHigh(figure.getHiLevel()); // implied Hihi
			representation.setRangeLow(figure.getLoLevel());
			representation.setRangeMax(figure.getRange().getUpper());
			representation.setRangeMin(figure.getRange().getLower());
			representation.setType(getFigureType(this.figure));
			representation.setText(text);
			representation.setLogarithmic(figure.isLogScale());
			representation.setNumberFormat(figure.getValueLabelFormat());
		}
		if (canvasUpdated) {
			
			representation.setWidth(canvas.getSize().x);
			representation.setHeight(canvas.getSize().y);

			representation.setX(canvas.getLocation().x);
			representation.setY(canvas.getLocation().y);
		}
		if (linkUpdated) {
			representation.setSignalUID(signalUID);
		}
		dashboard.setDirty(true);
	}

	public String getDescription() {
		return (widgetType.name + " " + this.figure.getClass().getSimpleName());
	}

	public int getFigureType(AbstractMarkedWidgetFigure figure2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}

	@Override
	public int getType() {
		return widgetType.ordinal();
	}

	public WidgetType getWidgetType() {
		return widgetType;
	}

	@Override
	public void updateFigure(AbstractMarkedWidgetFigure figure) {
		if (!this.figure.getClass().equals(figure.getClass())) {
			
			this.figure = figure;
			setFigureRange(range_min, range_max);
			
			lws.setContents(figure);
			applyRepresentation(true, true, false);
			dashboard.layout(true);
		}
	}

	@Override
	public void setFigureRange(double min, double max) {
		range_min = min;
		range_max = max;
		figure.setRange(min, max);
	}

	@Override
	public void mark(boolean mark) {
	}

	public boolean isLogarithmic() {
		return figure.isLogScale();
	}

	public abstract Signal getSignal();

	public String getSignalUID() {
		return signalUID;
	}

	public void setLogarithmic(boolean selection) {
		figure.setLogScale(selection);
	}

	public abstract void setFigureLo(double parseDouble);

	public abstract void setFigureHi(double parseDouble);

	public abstract void setXY(int x, int y, int width, int height);

	public abstract void refresh();

	public abstract void setText(String text);

	public void setValueLabelFormat(String text2) {
		if(!text2.equals(figure.getValueLabelFormat())) {
			figure.setValueLabelFormat(text2);
			updateRepresentation(true, false, false);
		}
	}
}
