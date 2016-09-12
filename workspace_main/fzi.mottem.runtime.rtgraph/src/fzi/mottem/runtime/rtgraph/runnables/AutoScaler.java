package fzi.mottem.runtime.rtgraph.runnables;

import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.swt.xygraph.linearscale.Range;
import org.csstudio.swt.xygraph.undo.ZoomType;
import org.eclipse.swt.widgets.Display;

import fzi.mottem.runtime.interfaces.IViewComponentUpdater;
import fzi.mottem.runtime.rtgraph.views.GraphView;

public class AutoScaler implements IViewComponentUpdater {

	private GraphView graphView;
	boolean followingTrace = true;
	private int autoscaleDelay = 500;

	Range dataRange;
	double axisRange;

	public AutoScaler(GraphView graphView) {
		this.graphView = graphView;
	}

	public int getAutoscaleDelay() {
		return autoscaleDelay;
	}

	public void setAutoscaleDelay(int autoscaleDelay) {
		this.autoscaleDelay = autoscaleDelay;
	}

	public boolean isFollowingTrace() {
		return followingTrace;
	}

	public void setFollowingTrace(boolean followTrace) {
		System.out.println("AutoScaler: Following trace: " + followTrace);
		this.followingTrace = followTrace;
	}

	@Override
	public void run() {
		// System.out.println("xyGraph zoom type: " +
		// graphView.getGraph().getZoomType());
		// System.out.println("Follow trace: " + followingTrace);
		if (followingTrace && (graphView.getGraph().getZoomType() == ZoomType.NONE
				|| graphView.getGraph().getZoomType() == ZoomType.PANNING)) {
			update();
			// System.out.println("Updating at " + autoscaleDelay + " ms");
			Display.getCurrent().timerExec(autoscaleDelay, this);
		} else if (followingTrace && (graphView.getGraph().getZoomType() != ZoomType.NONE
				|| graphView.getGraph().getZoomType() != ZoomType.PANNING)) {
			followingTrace = false;
			graphView.setFollowTraces(false);
		}
	}

	@Override
	public void update() {
		autoscaleDelay = (int) (Math.abs(graphView.getGraph().primaryXAxis.getRange().getUpper())
				- Math.abs(graphView.getGraph().primaryXAxis.getRange().getLower())) / 3;

		if (autoscaleDelay > 1000)
			autoscaleDelay = 1000;
		if (autoscaleDelay < 1) {
			System.out.println(
					graphView.getGraph().getTitle() + " autoscaler ecountered negative autoscale delay, resetting...");
			autoscaleDelay = 100;
		}

		axisRangeBasedUpdate();

	}

	private void axisRangeBasedUpdate() {
		for (Axis axis : graphView.getGraph().getXAxisList()) {

			dataRange = axis.getTraceDataRange();
			if (dataRange != null) {
				axis.setRange(dataRange.getUpper() - (axis.getRange().getUpper() - axis.getRange().getLower()) / 2,
						dataRange.getUpper() + (axis.getRange().getUpper() - axis.getRange().getLower()) / 2, true);
				axis.repaint();
			}
		}
		for (Axis axis : graphView.getGraph().getYAxisList()) {
			dataRange = axis.getTraceDataRange();
			if (dataRange != null) {
				axis.setRange(dataRange.getLower() - Math.abs(dataRange.getLower() / 16),
						dataRange.getUpper() + Math.abs(dataRange.getUpper() / 16), true);
				axis.repaint();
			}
		}
	}

	private void latestValueBasedUpdate() {

		XYGraph graph = graphView.getGraph();
		
		for (Axis axis : graph.getXAxisList()) {
			
			dataRange = axis.getTraceDataRange();
			axisRange = axis.getRange().getUpper() - axis.getRange().getLower();
			
			
			
			if (dataRange != null) {
				
				
				
				axis.setRange(dataRange.getUpper() - (axis.getRange().getUpper() - axis.getRange().getLower()) / 2,
						dataRange.getUpper() + (axis.getRange().getUpper() - axis.getRange().getLower()) / 2, true);
				axis.repaint();
			}
		}

		for (Axis axis : graph.getYAxisList()) {
			dataRange = axis.getTraceDataRange();
			
			if (dataRange != null) {
				axis.setRange(dataRange.getLower() - Math.abs(dataRange.getLower() / 16),
						dataRange.getUpper() + Math.abs(dataRange.getUpper() / 16), true);
				axis.repaint();
			}
		}
	}

}
