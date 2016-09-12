package fzi.mottem.runtime.rtgraph;

import java.text.DecimalFormat;

import org.csstudio.swt.widgets.datadefinition.IManualValueChangeListener;
import org.csstudio.swt.widgets.figures.AbstractMarkedWidgetFigure;
import org.csstudio.swt.widgets.figures.KnobFigure;
import org.csstudio.swt.widgets.figures.ScaledSliderFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;

import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.dataexchanger.Signal.SignalType;
import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;

public class ControllerWidgetLink extends AbstractWidgetExchangeLink {
	
	protected String name;

	protected double range_min = 0;
	protected double range_max = 100;

	public ControllerWidgetLink(AbstractMarkedWidgetFigure figure, DashboardComposite widgetsContainer) {
		this.dashboard = widgetsContainer;
		this.figure = figure;
		this.widgetType = WidgetType.W_CONTROLLER;
		
		addFigureListener(figure);
		
		this.name = figure.getClass().getSimpleName();
		
		canvas = new Canvas(widgetsContainer, SWT.BORDER);

		lws = new LightweightSystem(canvas);
		lws.setContents(figure);
		
		representation = new WidgetRepresentation();
		representation.setWidgetType(widgetType);
		
		updateRepresentation(true, true, true);
	}
	
	public ControllerWidgetLink(AbstractMarkedWidgetFigure figure, DashboardComposite widgetsContainer,
			WidgetRepresentation representation) {
		this(figure, widgetsContainer);
		
		this.representation = representation;
		representation.setWidgetType(widgetType);
		
		applyRepresentation(true, true, true);
	}
	
	@Override
	public int getFigureType(AbstractMarkedWidgetFigure figure) {
		int type = 0;
		if(figure == null) {
			return -1;
		}
		if(figure.getClass() == KnobFigure.class) {
			type = Constants.CONTROLLER_KNOB;
			
		} else if(figure.getClass() == ScaledSliderFigure.class) {
			type = Constants.CONTROLLER_SLIDE;
		}
		return type;
	}
	
	
	/**
	 * 
	 * This method should be called when the user wants to dispose of the whole
	 * controller widget.
	 */
	public void delete() {
		canvas.dispose();
		lws = null;
		if (figure != null) {
			figure.erase();
		}
		figure = null;
		if (!dashboard.isDisposed() && dashboard != null) {
			dashboard.layout(true);
		}
	}

	@Override
	public void applyRepresentation(boolean applySignal, boolean applyFigure, boolean applyCanvas) {
		if(applySignal) {
			signal = DataExchanger.getSignal(representation.getSignalUID());
			if(signal != null) {
				this.signalUID = signal.getId();
				this.simpleName = signal.getSimpleName();
			}
		}
		if (applyFigure) {
			figure.setRange(representation.getRangeMin(), representation.getRangeMax());
			setFigureHi(representation.getRangeHigh());
			setFigureLo(representation.getRangeLow());
			figure.setLogScale(representation.isLogarithmic());
			figure.setValueLabelFormat(representation.getNumberFormat());
			setText(representation.getText());
		}
		if (applyCanvas) {
			canvas.setBounds(representation.getX(), representation.getY(), representation.getWidth(),
					representation.getHeight());
		}
		
		dashboard.layout(true);
		
		if(representation.getText() == null || representation.getText().length() < 1) {
			canvas.setToolTipText(simpleName + "\n" + signalUID);
		} else {
			canvas.setToolTipText(representation.getText());
		}
	}

	public void applyRepresentation() {
		applyRepresentation(true, true, true);
	}
	
	
	private void addFigureListener(AbstractMarkedWidgetFigure figure) {
		
		switch(getFigureType(figure)) {
		case Constants.CONTROLLER_KNOB: 
			((KnobFigure)figure).addManualValueChangeListener(new IManualValueChangeListener() {
				@Override
				public void manualValueChanged(double newValue) {
					if(signal != null) {
						DataExchanger.consume(new DEMessage(signal, newValue, 0));
					}				
				}
			});
			break;
		case Constants.CONTROLLER_SLIDE:
			((ScaledSliderFigure)figure).addManualValueChangeListener(new IManualValueChangeListener() {
				@Override
				public void manualValueChanged(double newValue) {
					if(signal != null) {
						DataExchanger.consume(new DEMessage(signal, newValue, 0));
					}				
				}
			});
			break;
		default: break;
		}	
	}
	
	@Override
	public void updateFigure(AbstractMarkedWidgetFigure figure) {
		if (!this.figure.getClass().equals(figure.getClass())) {
			
			this.figure = figure;
			setFigureRange(range_min, range_max);
			
			lws.setContents(figure);
			addFigureListener(this.figure);
			
			applyRepresentation(true, true, false);
			dashboard.layout(true);
		}
	}

	public void setFigureLo(double low) {
		figure.setLoLevel(low);
		figure.setLoloLevel((low + figure.getRange().getLower()) / 2);
	}

	public void setFigureHi(double high) {
		figure.setHiLevel(high);
		figure.setHihiLevel((high + figure.getRange().getUpper()) / 2);
	}

	public void setXY(int x, int y, int width, int height) {
		canvas.setBounds(x, y, width, height);
		updateRepresentation(false, true, false);
		dashboard.layout(true);
	}
	
	public void refresh() {
		figure.repaint();
	}
	
	public void setLogarithmic(boolean logarithmic_scale) {
		figure.setLogScale(logarithmic_scale);
	}

	public void setSignal(Signal signal) {
		if(signal.getType() == SignalType.HW_OUTPUT) {
			this.signal = signal;
		} else {
			System.out.println("Controller can only connect to hardware input signals!");
		}	
	}
	
	public Signal getSignal() {
		return this.signal;
	}

	@Override
	public void setText(String text) {
		
			this.text = text;
			representation.setText(text);
			//updateRepresentation(true, false, false);
			if(text.length() > 0) {
				canvas.setToolTipText(text);
				
			} else {
				canvas.setToolTipText(this.simpleName + "\n" + signalUID);
			}
	
	}
	
	@Override
	public void setValueLabelFormat(String text2) {
		if(!text2.equals(figure.getValueLabelFormat())) {
			figure.setValueLabelFormat(text2);
			DecimalFormat df = new DecimalFormat(text2);
			int input_granularity = df.getMaximumFractionDigits();
			double fraction = Math.pow(10, -input_granularity);
			if(figure instanceof KnobFigure) {
				((KnobFigure)figure).setIncrement(fraction);
			} else if (figure instanceof ScaledSliderFigure) {
				((ScaledSliderFigure)figure).setPageIncrement(fraction);
			}
			updateRepresentation(true, false, false);
		}
	}
}
