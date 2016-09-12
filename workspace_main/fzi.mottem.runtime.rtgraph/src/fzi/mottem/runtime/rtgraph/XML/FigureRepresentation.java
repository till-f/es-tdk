package fzi.mottem.runtime.rtgraph.XML;

import fzi.mottem.runtime.rtgraph.Constants;

public class FigureRepresentation {
	
	protected String signalUID = "default";

	protected int x = 10;
	protected int y = 16;
	protected int height = Constants.WIDGET_HEIGHT_HINT;
	protected int width = Constants.WIDGET_WIDTH_HINT;
	protected int type = 0;
	protected double rangeMax = 100;
	protected double rangeMin = 0;
	
	
	public String getSignalUID() {
		return signalUID;
	}

	public void setSignalUID(String signalUID) {
		this.signalUID = signalUID;
	}

	/**
	 * Gets the x - position of the canvas
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x - position of the canvas
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y - position of the canvas
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y-position of the canvas
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the canvas height
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the canvas height
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the canvas width
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the canvas width
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the figure type
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the figure type (does not update the figure but only sets and
	 * integer field)
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Get max widget range
	 * 
	 * @return
	 */
	public double getRangeMax() {
		return rangeMax;
	}

	/**
	 * Set max figure range
	 * 
	 * @param rangeMax
	 */
	public void setRangeMax(double rangeMax) {
		this.rangeMax = rangeMax;
	}

	/**
	 * Get minimum widget range
	 * 
	 * @return
	 */
	public double getRangeMin() {
		return rangeMin;
	}

	/**
	 * Set minimum widget range
	 * 
	 * @param rangeMin
	 */
	public void setRangeMin(double rangeMin) {
		this.rangeMin = rangeMin;
	}
}
