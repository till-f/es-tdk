package fzi.mottem.runtime.rtgraph.XML;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the state of a GraphView and its XYGraph and traces. It
 * contains information about the traces, their corresponding Trace Exchange
 * Links, etc. The instances of this class are referenced in an ArrayList in a
 * Profile object, so that they can be serialized in xml data.
 * 
 * @author Kalin Katev
 *
 */
@XmlRootElement(name = "GraphView")

public class GraphViewRepresentation {

	@XmlElementWrapper(name = "traceList")
	@XmlElement(name = "trace")
	public ArrayList<TraceRepresentation> traceRepresentations = new ArrayList<TraceRepresentation>();

	private String path = "C://";
	
	private double minPrimaryXrange = 0;
	private double maxPrimaryXrange = 2000;
	private boolean primaryXLogarithmic = false;
	
	public boolean isPrimaryXLogarithmic() {
		return primaryXLogarithmic;
	}

	public void setPrimaryXLogarithmic(boolean primaryXLogarithmic) {
		this.primaryXLogarithmic = primaryXLogarithmic;
	}

	public double getMinPrimaryXrange() {
		return minPrimaryXrange;
	}

	public void setMinPrimaryXrange(double minPrimaryXrange) {
		this.minPrimaryXrange = minPrimaryXrange;
	}

	public double getMaxPrimaryXrange() {
		return maxPrimaryXrange;
	}

	public void setMaxPrimaryXrange(double maxPrimaryXrange) {
		this.maxPrimaryXrange = maxPrimaryXrange;
	}
/*
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}*/

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
