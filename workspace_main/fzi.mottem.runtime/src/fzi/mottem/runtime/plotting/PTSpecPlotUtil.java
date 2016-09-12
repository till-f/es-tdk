package fzi.mottem.runtime.plotting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.AbstractPlot;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.DrawablePanel;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Orientation;
import fzi.mottem.runtime.plotting.interfaces.IPTSpecPlot;
import fzi.mottem.runtime.rtgraph.TraceExchangeLink;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.editors.GraphViewEditor;
import fzi.mottem.runtime.rtgraph.views.GraphView;
import fzi.util.eclipse.IntegrationUtils;

public class PTSpecPlotUtil {

	public final static Color COLOR_MAP[] = { Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE,
			Color.MAGENTA };

	public static void showGralPlot(IPTSpecPlot plot) {
		// get the plot (visible line(s) and area(s))
		AbstractPlot gralPlot = toGralPlot(plot);
		// create Java container
		DrawablePanel panel = new InteractivePanel(gralPlot);
		// create Window
		JFrame frame = new JFrame(plot.getPlotName());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setSize(new Dimension(800, 600));
		frame.setVisible(true);
	}

	public static void showTabbedPlot(String pathToPlotDB) {
		IPath path = Path.fromPortableString(pathToPlotDB);

		String absolutePath = IntegrationUtils.getSystemPathForWorkspaceRelativePath(path).toString();

		System.out.println("PLOT: " + absolutePath);
		
		GraphViewEditor editor = ViewCoordinator
				.openGraphViewEditor(Path.fromPortableString(absolutePath).toFile());
		
		editor.applyDB(new Path(absolutePath).toFile(), false, null);
	}

	@Deprecated
	public static void showTabbedPlot(IPTSpecPlot plot) {
		GraphView view = ViewCoordinator.showGraphViewpart(plot.getPlotName());

		for (String traceName : plot.getPlotItems().keySet()) {
			TraceExchangeLink trLink = view.addTraceGetLink(traceName);

			trLink.consumeBurst(plot.getPlotItems().get(traceName).getDEMessages());
		}
	}

	public static AbstractPlot toGralPlot(IPTSpecPlot ptsPlot) {
		XYPlot gralPlot = new XYPlot();

		int plotNumber = 0;
		for (String identifier : ptsPlot.getPlotItems().keySet()) {
			PTSpecXYDataTreeMap dataTreeMap = ptsPlot.getPlotItems().get(identifier);

			@SuppressWarnings("unchecked")
			DataTable dataTable = new DataTable(Double.class, Double.class);

			for (Double x : dataTreeMap.getX()) {
				dataTable.add(x, dataTreeMap.getY(x));
			}

			DataSeries dataSeries = new DataSeries(dataTreeMap.getCaption(), dataTable);

			gralPlot.add(dataSeries);

			LineRenderer lineRenderer = new DefaultLineRenderer2D();
			lineRenderer.setColor(COLOR_MAP[(plotNumber % COLOR_MAP.length)]);
			gralPlot.setLineRenderer(dataSeries, lineRenderer);

			PointRenderer pointRenderer = new DefaultPointRenderer2D();
			Rectangle rect = new Rectangle(2, 2);
			pointRenderer.setShape(rect);
			gralPlot.setPointRenderer(dataSeries, pointRenderer);

			gralPlot.setLegendVisible(true);
			gralPlot.getLegend().setOrientation(Orientation.HORIZONTAL);
			gralPlot.getLegend().setAlignmentY(1.0);

			plotNumber++;
		}

		return gralPlot;
	}

}
