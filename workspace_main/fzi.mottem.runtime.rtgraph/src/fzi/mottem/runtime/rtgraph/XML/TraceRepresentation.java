package fzi.mottem.runtime.rtgraph.XML;

import javax.xml.bind.annotation.XmlRootElement;

import org.csstudio.swt.xygraph.figures.Trace.PointStyle;
import org.csstudio.swt.xygraph.figures.Trace.TraceType;

/**
 * This class represents the state of a trace and its exchange link. It contains
 * information about the current link name, its arranged Signal UID and relevant
 * CircularBufferDataPRovider options like the buffer size. The instances of
 * this class are referenced in an ArrayList in a Profile object, so that they
 * can be serialized in xml data.
 * 
 * @author Kalin Katev
 *
 */
@XmlRootElement(name = "Trace")
public class TraceRepresentation {

	private String signalUID = "default";
	private String name = "default";
	private String traceName = "default";
	private int bufferSize = 4 * 4096;
	private int pollingDelay = 20;
	private int red = -1;
	private int green = -1;
	private int blue = -1;
	private TraceType type = TraceType.SOLID_LINE;
	private PointStyle pointStyle = PointStyle.POINT;
	
	
	private boolean logarithmic = false;
	
	private double min_range = 0;
	private double max_range = 100;
	
	public String getSignalUID() {
		return signalUID;
	}

	public void setSignalUID(String signalUID) {
		this.signalUID = signalUID;
	}

	/**
	 * Gets the buffer size of the Exchange Link's data provider
	 * 
	 * @return
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * Sets the buffer size of the Exchange Link's data provider
	 * 
	 * @param bufferSize
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * Gets the name of the TraceExchangeLink
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the TraceExchangeLink
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Empty constructor
	 */
	public TraceRepresentation() {

	}

	public String getTraceName() {
		return traceName;
	}

	public void setTraceName(String traceName) {
		this.traceName = traceName;
	}

	public int getPollingDelay() {
		return pollingDelay;
	}

	public void setPollingDelay(int pollingDelay) {
		this.pollingDelay = pollingDelay;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public TraceType getType() {
		return type;
	}

	public void setType(TraceType type) {
		this.type = type;
	}

	public PointStyle getPointStyle() {
		return pointStyle;
	}

	public void setPointStyle(PointStyle pstyle) {
		this.pointStyle = pstyle;
	}
	
	public boolean isLogarithmic() {
		return logarithmic;
	}

	public void setLogarithmic(boolean logarithmic) {
		this.logarithmic = logarithmic;
	}

	public double getMin_range() {
		return min_range;
	}

	public void setMin_range(double min_range) {
		this.min_range = min_range;
	}

	public double getMax_range() {
		return max_range;
	}

	public void setMax_range(double max_range) {
		this.max_range = max_range;
	}

}
