package fzi.mottem.runtime.rtgraph.XML;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fzi.mottem.runtime.rtgraph.Constants;

@XmlRootElement(name = "Dashboard")
public class DashboardRepresentation {

	// read the representations and create widgets
	@XmlElementWrapper(name = "widgetList")
	@XmlElement(name = "widget")
	public ArrayList<WidgetRepresentation> figureRepresentations = new ArrayList<WidgetRepresentation>();
	
	public DashboardRepresentation() {
	}

	//private String name = "";

	//TODO separate fields for every GraphView in the GVRepresentation!
	// -------- WidgetsView
	@XmlElement
	public int widget_height_hint = Constants.WIDGET_HEIGHT_HINT;

	@XmlElement
	public int widget_width_hint = Constants.WIDGET_WIDTH_HINT;

	@XmlElement
	public int widget_polling_delay = Constants.DEF_WIDGET_UPDATEDELAY;
	
	@XmlElement
	public String background_path = "C:\\";
	@XmlElement
	public boolean scale_background_x = false;
	@XmlElement
	public boolean scale_background_y = false;
	@XmlElement
	public int gridSize = 16;
	
	private String path = "C://";

	public String getPath() {
		return path;
	}
	
	public void setPath(String representationOSPath) {
		path = representationOSPath;
	}
	/*
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
*/
	// -------- Setup/UI
}

