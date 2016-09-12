package fzi.mottem.runtime.rtgraph.XML;

import javax.xml.bind.annotation.XmlRootElement;

import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink.WidgetType;

/**
 * This class represents the state of a widget exchange link and its canvas and
 * figure aggregates. It contains information about the figure ranges, the
 * canvas position, the figure type, etc. The instances of this class are
 * referenced in an ArrayList in a Profile object, so that they can be
 * serialized in xml data.
 * 
 * @author Kalin Katev
 *
 */
@XmlRootElement(name = "Figure")
public class WidgetRepresentation extends FigureRepresentation {

	protected double rangeLow = 25;
	protected double rangeHigh = 75;
	protected boolean isLogarithmic = false;
	protected String numberFormat = "";
	
	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	public boolean isLogarithmic() {
		return isLogarithmic;
	}

	public void setLogarithmic(boolean isLogarithmic) {
		this.isLogarithmic = isLogarithmic;
	}

	protected WidgetType widgetType = WidgetType.W_ABSTRACT;
	String text = "";
	
	
	public WidgetType getWidgetType() {
		return widgetType;
	}

	public void setWidgetType(WidgetType widgetType) {
		this.widgetType = widgetType;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Get high widget range boundary
	 * 
	 * @return
	 */
	public double getRangeHigh() {
		return rangeHigh;
	}

	/**
	 * Set high widget range boudary
	 * 
	 * @param rangeHigh
	 */
	public void setRangeHigh(double rangeHigh) {
		this.rangeHigh = rangeHigh;
	}

	/**
	 * Get low widget range boundary
	 * 
	 * @return
	 */
	public double getRangeLow() {
		return rangeLow;
	}

	/**
	 * Set low widget range boundary
	 * 
	 * @param rangeLow
	 */
	public void setRangeLow(double rangeLow) {
		this.rangeLow = rangeLow;
	}

	/**
	 * Empty constructor
	 */
	public WidgetRepresentation() {

	}

}
