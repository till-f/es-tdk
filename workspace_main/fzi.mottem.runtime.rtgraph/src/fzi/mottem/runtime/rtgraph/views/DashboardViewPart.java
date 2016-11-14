package fzi.mottem.runtime.rtgraph.views;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import fzi.mottem.runtime.rtgraph.SetupUtilities;

public class DashboardViewPart extends ViewPart {

	SetupUtilities setup;

	private CircularBufferDataProvider mainTraceProvider;
	private Composite mainHolder;

	private GridLayout mainFill;

	FigureCanvas fc;
	RowData rowWidgetCompositeData;

	Dashboard dashboard;

	final int milliToNano = 1000000;

	public DashboardViewPart() {
		setup = null;
	}

	public CircularBufferDataProvider getMainTrace() {
		return mainTraceProvider;
	}

	public void setMainTraceUpdateDelay(int delay) {
		mainTraceProvider.setUpdateDelay(delay);
	}

	public void setMainTraceBufferSize(int size) {
		mainTraceProvider.setBufferSize(size);
	}

	/**
	 * Initialize the containing environment. By default it is made up from a
	 * main holder, which is a 1-column GridLayout which holds the graph canvas
	 * and the widgets composite container.
	 * 
	 * @param parent
	 *            The parent (higher level container) of the main holder, in
	 *            this case - the viewpart.
	 */
	private void initializeMainHolder(Composite parent) {
		mainFill = new GridLayout(1, true);
		//RowLayout mainFill2 = new RowLayout(SWT.VERTICAL);
		// mainFill2.
		mainHolder = new SashForm(parent, SWT.VERTICAL);
		mainHolder.setLayout(mainFill);
	}

	@Override
	public void createPartControl(final Composite parent) {	
		initializeMainHolder(parent);
		dashboard = new Dashboard(mainHolder, SWT.None);
		
		dashboard.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dashboard.initContainer();
		//dashboard.addObserver(new WidgetViewObserver());
		dashboard.startRunnables();
		//SetupUnit.setUpDashboardView(dashboard); //old
	}

	@Override
	public void setFocus() {
	}
	
	public void setName(String name) {
		setPartName(name);
	}
	
	public Dashboard getView() {
		return dashboard;
	}

	public void redraw() {
		//graphView.layout(true);
		dashboard.layout(true);
	}
}
