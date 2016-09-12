package fzi.mottem.runtime.rtgraph;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "fzi.mottem.runtime.rtgraph")

public class Constants {
	
	public final static String profiles_path = "/profiles/";
	public final static String gray_icon = "img/gray.jpg";
	public final static String rabbit_run_icon = "img/Running-Rabbit-icon-small.png";
	public final static String rabbit_sit_icon = "img/Rabbit-icon-small.gif";
	
	public final static int ABSTRACT_WIDGET = -1;
	public final static int WIDGET_THERMO = 0;
	public final static int WIDGET_GAUGE = 1;
	public final static int WIDGET_METER = 2;
	public final static int WIDGET_PROGRESS = 3;
	public final static int WIDGET_TANK = 4;
	public final static int WIDGET_TEXT = 5;
	public final static int WIDGET_IMAGE = 6;
	
	public final static int CONTROLLER_KNOB = 0;
	public final static int CONTROLLER_SLIDE = 1;
	
	//TODO Make a simple class for widget integer-type x string-type ordering.
	public final static String[] WIDGET_TYPES = {"Thermometer", "Gauge", "Meter", "ProgressBar", "Tank", "Text", "image"}; //Order must correspond to the integer constants!
	public final static String[] CONTROLLER_TYPES = {"Knob", "Slider"};
	
	public static int WIDGET_HEIGHT_HINT = 200;

	public static int WIDGET_WIDTH_HINT = 200;
	
	public static int DEF_WIDGET_UPDATEDELAY = 50;

	public static int DEF_GRAPH_UPDATEDELAY = 50; //milliseconds
	
	public static int DEF_GRAPH_BUFFERSIZE = 4 * 4096;
	
	public static int DEF_GRAPH_AUTOSCALETIME = 1500; 

	public static int DEF_WIDGET_POLLINTERVAL = 50;
	
	public static int DEF_TRACE_POLLINTERVAL = 5;

	public static int widget_count = 3;
	public static String EXTENSION_GRAPHVIEW = ".graphview";
	public static String EXTENSION_DASHBOARD = ".dashboard";
	public static String EXTENSION_DATABASE = ".db";
	
	
	public static int getWIDGET_HEIGHT_HINT() {
		return WIDGET_HEIGHT_HINT;
	}
	public static void setWIDGET_HEIGHT_HINT(int wIDGET_HEIGHT_HINT) {
		WIDGET_HEIGHT_HINT = wIDGET_HEIGHT_HINT;
	}
	public static int getWIDGET_WIDTH_HINT() {
		return WIDGET_WIDTH_HINT;
	}
	public static void setWIDGET_WIDTH_HINT(int wIDGET_WIDTH_HINT) {
		WIDGET_WIDTH_HINT = wIDGET_WIDTH_HINT;
	}
	public static int getDEF_GRAPH_UPDATEDELAY() {
		return DEF_GRAPH_UPDATEDELAY;
	}
	public static void setDEF_GRAPH_UPDATEDELAY(int dEF_GRAPH_UPDATEDELAY) {
		DEF_GRAPH_UPDATEDELAY = dEF_GRAPH_UPDATEDELAY;
	}
	public static int getDEF_GRAPH_BUFFERSIZE() {
		return DEF_GRAPH_BUFFERSIZE;
	}
	public static void setDEF_GRAPH_BUFFERSIZE(int dEF_GRAPH_BUFFERSIZE) {
		DEF_GRAPH_BUFFERSIZE = dEF_GRAPH_BUFFERSIZE;
	}
	public static int getDEF_GRAPH_AUTOSCALETIME() {
		return DEF_GRAPH_AUTOSCALETIME;
	}
	public static void setDEF_GRAPH_AUTOSCALETIME(int dEF_GRAPH_AUTOSCALETIME) {
		DEF_GRAPH_AUTOSCALETIME = dEF_GRAPH_AUTOSCALETIME;
	}
	public static int getDEF_WIDGET_POLLINTERVAL() {
		return DEF_WIDGET_POLLINTERVAL;
	}
	public static void setDEF_WIDGET_POLLINTERVAL(int dEF_WIDGET_POLLINTERVAL) {
		DEF_WIDGET_POLLINTERVAL = dEF_WIDGET_POLLINTERVAL;
	}
	public static int getDEF_TRACE_POLLINTERVAL() {
		return DEF_TRACE_POLLINTERVAL;
	}
	public static void setDEF_TRACE_POLLINTERVAL(int dEF_TRACE_POLLINTERVAL) {
		DEF_TRACE_POLLINTERVAL = dEF_TRACE_POLLINTERVAL;
	}
	public static int getWidget_count() {
		return widget_count;
	}
	public static void setWidget_count(int widget_count) {
		Constants.widget_count = widget_count;
	}
	
	
	
}
