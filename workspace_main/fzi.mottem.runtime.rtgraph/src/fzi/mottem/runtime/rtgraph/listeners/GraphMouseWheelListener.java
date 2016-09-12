package fzi.mottem.runtime.rtgraph.listeners;

import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;

public class GraphMouseWheelListener implements MouseWheelListener {

	XYGraph xyGraph;

	boolean zoomYaxis = true;
	boolean zoomXaxis = true;
	boolean mouseInPlotArea = true;
	double yrange;
	double xrange;
	double yincrement;
	double xincrement;

	public boolean isZoomYaxis() {
		return zoomYaxis;
	}

	public void setZoomYaxis(boolean zoomYaxis) {
		this.zoomYaxis = zoomYaxis;
	}

	public XYGraph getXygraph() {
		return xyGraph;
	}

	public void setXygraph(XYGraph xygraph) {
		this.xyGraph = xygraph;
	}

	public boolean isZoomXaxis() {
		return zoomXaxis;
	}

	public void setZoomXaxis(boolean zoomXaxis) {
		this.zoomXaxis = zoomXaxis;
	}

	public GraphMouseWheelListener(XYGraph xygraph) {
		this.xyGraph = xygraph;
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		int wheelCount = e.count;

		Point p = new Point(e.x, e.y);

		mouseInPlotArea = xyGraph.getPlotArea().containsPoint(p);

		wheelCount = (int) Math.ceil(wheelCount / 3.0f);
		while (wheelCount > 0) {

			if (zoomYaxis) {
				for (Axis y : xyGraph.getYAxisList()) {

					if (y.containsPoint(p) || mouseInPlotArea) {
						yrange = y.getRange().getUpper() - y.getRange().getLower();
						yincrement =  getIncrement(yrange);
						y.setRange(y.getRange().getLower() + yincrement, y.getRange().getUpper() - yincrement, true);
					}

				}
				/*
				 * xyGraph.primaryYAxis.setRange(
				 * xyGraph.primaryYAxis.getRange().getLower() -
				 * Math.abs(xyGraph.primaryYAxis.getRange().getLower() * 0.03),
				 * xyGraph.primaryYAxis.getRange().getUpper() +
				 * Math.abs(xyGraph.primaryYAxis.getRange().getUpper() * ((1 /
				 * 0.97) - 1)), true);
				 */
			}

			if (zoomXaxis && (mouseInPlotArea || !zoomYaxis)) {
				for (Axis x : xyGraph.getXAxisList()) {
					xrange = x.getRange().getUpper() - x.getRange().getLower();
					xincrement =  getIncrement(xrange);
					x.setRange(x.getRange().getLower() + xincrement, x.getRange().getUpper() - xincrement,
							true);
					//x.setRange(x.getRange().getLower() * 0.98, x.getRange().getUpper() * (1 / 0.98), true);
				}
				/*
				 * xyGraph.primaryXAxis.setRange(xyGraph.primaryXAxis.getRange()
				 * .getLower() * 0.98,
				 * xyGraph.primaryXAxis.getRange().getUpper() * (1 / 0.98),
				 * true);
				 */
			}

			wheelCount--;
		}
		while (wheelCount < 0) {

			if (zoomYaxis) {

				for (Axis y : xyGraph.getYAxisList()) {
					if (y.containsPoint(p) || mouseInPlotArea) {
						
						if (y.containsPoint(p) || mouseInPlotArea) {
							yrange = y.getRange().getUpper() - y.getRange().getLower();
							yincrement =  getIncrement(yrange);
							y.setRange(y.getRange().getLower() - yincrement, y.getRange().getUpper() + yincrement,
									true);
						}
					}

				} /*
					 * xyGraph.primaryYAxis.setRange(
					 * xyGraph.primaryYAxis.getRange().getLower() +
					 * Math.abs(xyGraph.primaryYAxis.getRange().getLower() * ((1
					 * / 0.97) - 1)), xyGraph.primaryYAxis.getRange().getUpper()
					 * - Math.abs(xyGraph.primaryYAxis.getRange().getUpper() *
					 * 0.03), true);
					 */

			}

			if (zoomXaxis && (mouseInPlotArea || !zoomYaxis)) {
				for (Axis x : xyGraph.getXAxisList()) {
					xrange = x.getRange().getUpper() - x.getRange().getLower();
					xincrement = getIncrement(xrange);
					x.setRange(x.getRange().getLower() - xincrement, x.getRange().getUpper() + xincrement,
							true);
					//x.setRange(x.getRange().getLower() * (1 / 0.98), x.getRange().getUpper() * 0.98, true);
				}
				/*
				 * xyGraph.primaryXAxis.setRange(xyGraph.primaryXAxis.getRange()
				 * .getLower() * (1 / 0.98),
				 * xyGraph.primaryXAxis.getRange().getUpper() * 0.98, true);
				 */
			}

			wheelCount++;
		}
	}
	
	private double getIncrement(double range) {
		return Math.round(range  * 0.04 * 10.0) / 10.0 ;
	};

}
