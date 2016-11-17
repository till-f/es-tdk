package fzi.mottem.runtime.rtgraph.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.ToolbarArmedXYGraph;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.swt.xygraph.linearscale.Range;
import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.SetupUtilities;
import fzi.mottem.runtime.rtgraph.TraceExchangeLink;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.XML.GraphViewRepresentation;
import fzi.mottem.runtime.rtgraph.XML.TraceRepresentation;
import fzi.mottem.runtime.rtgraph.editors.GraphViewEditor;
import fzi.mottem.runtime.rtgraph.listeners.GraphMouseWheelListener;
import fzi.mottem.runtime.rtgraph.runnables.AutoScaler;
import fzi.mottem.runtime.rtgraph.settingsViews.GraphSettingsUI;

public class GraphView extends ObservableView<GraphViewRepresentation> {

	private XYGraph xyGraph;
	private Canvas graphCanvas;
	private GraphViewRepresentation representation;

	private GridData graphData;
	private LightweightSystem lws;
	Button followButton;
	
	Image running_rabbit = new Image(Display.getCurrent(),
			SetupUtilities.class.getResourceAsStream(Constants.rabbit_run_icon));
	Image sitting_rabbit = new Image(Display.getCurrent(),
			SetupUtilities.class.getResourceAsStream(Constants.rabbit_sit_icon));

	Trace mainTrace;
	private CircularBufferDataProvider mainTraceProvider;
	private HashMap<Trace, TraceExchangeLink> traceMap = new HashMap<Trace, TraceExchangeLink>();
	private LinkedList<Axis> axisYList = new LinkedList<Axis>();

	private boolean follow_trace = false;

	private Menu popupMenu;
	private MenuItem addTraceItem;
	private MenuItem removeTraceItem;
	private MenuItem traceSetupItem;
	private Menu removeTraceMenu;

	GraphMouseWheelListener mouseWheelListener;

	AutoScaler autoscale;

	// Button setupButton;

	private int trace_buffer_size = Constants.DEF_GRAPH_BUFFERSIZE;
	private int trace_pollingDelay = Constants.DEF_TRACE_POLLINTERVAL;
	private int graph_updateDelay = Constants.DEF_GRAPH_UPDATEDELAY; // milliseconds;
	private int graph_autoscale_delay = Constants.DEF_GRAPH_AUTOSCALETIME;
	private Button clearButton;

	private boolean dirty = false;
	private GraphViewEditor editor;
	private MenuItem openSettingsViewItem;

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		if (editor != null)
			editor.fireChange();
	}

	// ++++++++++++++++++++++++++++++ multiple traces

	/**
	 * Add a new trace to the xyGraph.
	 * 
	 * @param name
	 *            Name of the new trace.
	 * @param link
	 *            The Trace Exchange Link that will be set to this trace.
	 */
	public Trace addTrace(String name, TraceExchangeLink link) {
		String traceName = name;
		TraceRepresentation tr = link.getRepresentation();
		
		Axis y = null;
		
		//for(Axis ya : xyGraph.getYAxisList()) {
		//	if(ya.getTitle().equals(tr.getName())) y = ya;
		//}
		
		//if(y == null) {
			y = new Axis(name, true);
		//}
		
		y.setLogScale(tr.isLogarithmic());
		y.setRange(tr.getMin_range(), tr.getMax_range());
		xyGraph.addAxis(y);
		axisYList.add(y);

		Trace trace = new Trace(traceName, xyGraph.primaryXAxis, y, link.getBuffer());
		
		trace.setTraceType(tr.getType());
		trace.setPointStyle(tr.getPointStyle());

		if (tr.getRed() != -1 && tr.getGreen() != -1 && tr.getBlue() != -1) {
			trace.setTraceColor(new Color(Display.getDefault(), tr.getRed(), tr.getGreen(), tr.getBlue()));
		}

		xyGraph.addTrace(trace);

		traceMap.put(trace, link);
		link.startUpdater();
		setChanged();
		setDirty(true);
		fillRemoveTraceItem();
		checkAxes();
		return trace;
	}

	/**
	 * Add a new trace to the xyGraph. A new Trace Exchange Link will be created
	 * and will be mapped to the trace.
	 * 
	 * @param name
	 *            Name of the new trace.
	 */
	public void addTrace(String name) {
		String traceName = name;
		addTrace(traceName, new TraceExchangeLink("TL " + xyGraph.getTitle() + " " + traceName));
	}

	/**
	 * Add a new trace to the xyGraph. A new Trace Exchange Link will be created
	 * and will be mapped to the trace. The new trace's name will be relative to
	 * the current number of traces in the graph.
	 */
	public void addTrace() {
		addTrace("Trace " + traceMap.keySet().size());
	}

	public TraceExchangeLink getTraceExchangeLink(Trace t) {
		return traceMap.get(t);
	}

	/**
	 * Add a new Trace to the GraphView if there is no trace with the given
	 * name. Otherwise the GraphView is not modified. Returns the
	 * TraceExchangeLink associated with the Trace.
	 * 
	 * @param uid
	 *            a Signal's uid (used to find a Trace name)
	 * @return the TraceExchangeLink tied to the Trace
	 */
	public TraceExchangeLink addTraceGetLink(String uid) {
		TraceExchangeLink link = null;
		String name = createSimpleName(uid);
		for (Trace t : traceMap.keySet()) {
			if (t.getName().equals(name)) {
				link = traceMap.get(t);
				break;
			}
		}
		if (link != null) {
			return link;
		} else {
			link = new TraceExchangeLink(name);
			addTrace(name, link);
			return link;
		}
	}

	/**
	 * Adds a new trace only by a signal's UID. This method will add the trace,
	 * initialize a trace exchange link for it and try to get a corresponding
	 * simple name. If the UID is already being used then this view will remain
	 * unchanged.
	 * 
	 * @param uid
	 * @return The trace exchange link connected to the UID
	 */
	public TraceExchangeLink addTraceByUID(String uid, boolean connect) {
		TraceExchangeLink link = null;
		for (TraceExchangeLink tl : traceMap.values()) {
			if (tl.getSignalUID().equals(uid)) {
				link = tl;
				break;
			}
		}
		if (link != null) {
			return link;
		} else {
			String simpleName = createSimpleName(uid);
			link = new TraceExchangeLink(simpleName);
			if (connect)
				DataExchanger.replaceSignal(uid, link);
			link.setSignalUID(uid);
			addTrace(simpleName, link);
			return link;
		}
	}

	private String createSimpleName(String uid) {
		String simpleName;
		Signal s = DataExchanger.getSignal(uid);
		if (s != null) {
			simpleName = s.getSimpleName();

		} else {
			if (uid.length() > 16) {
				simpleName = uid.substring(uid.length() - 16);
			} else {
				simpleName = uid;
			}
		}

		return simpleName;
	}

	/**
	 * Returns the Trace Exchange Links that are tied to this GraphView.
	 * 
	 * @return ArrayList<TraceExchangeLink> where each Trace Exchange Link is
	 *         associated to a trace in the GraphView.
	 */
	public ArrayList<TraceExchangeLink> getTraceExchangeLinks() {
		return new ArrayList<TraceExchangeLink>(traceMap.values());
	}

	/**
	 * * Returns the traces that are used in this GraphView.
	 * 
	 * @return ArrayList<Trace> of the current traces.
	 */
	public ArrayList<Trace> getTraces() {
		return new ArrayList<Trace>(traceMap.keySet());
	}

	public ArrayList<String> getTraceNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Trace t : traceMap.keySet()) {
			names.add(t.getName());
		}
		return names;
	}

	/**
	 * Removes a trace from the GraphView along with its mapped Trace Exchange
	 * Link. The Trace Exchange Link will automatically be dropped from the
	 * DataExchanger.
	 * 
	 * <b>Important: </b> Do NOT use one Trace Exchange Link in multiple traces
	 * / GraphViews.
	 * 
	 * @param t
	 */
	public void removeTrace(Trace t) {
		xyGraph.removeTrace(t);
		
		LinkedList<Axis> yAxes = new LinkedList<Axis>(xyGraph.getYAxisList());
		
		for(Axis y : yAxes) {
			if(y.getTitle().equals(t.getName())) xyGraph.removeAxis(y);
		}
		
		if (traceMap.containsKey(t)) {
			traceMap.get(t).drop();
			traceMap.remove(t);
		}
		setChanged();
		fillRemoveTraceItem();
		checkAxes();
		setDirty(true);
	}

	/**
	 * Connects the Trace Exchange Link that is mapped to a given Trace t to a
	 * Signal
	 * 
	 * @param signalIndex
	 *            index of the Signal as presented in the SetupUnit
	 * @param t
	 *            the trace that is mapped to the Trace Exchange Link
	 */
	public void connectTraceLink(int signalIndex, Trace t) {
		SetupUtilities.connectTraceLink(signalIndex, traceMap.get(t));
		traceMap.get(t).startUpdater();
		setChanged();
		setDirty(true);
	}

	/**
	 * Connects the Trace Exchange Link that is mapped to a given Trace t to a
	 * Signal
	 * 
	 * @param signalUID
	 *            unique ID of the Signals
	 * @param t
	 *            the trace that is mapped to the Trace Exchange Link
	 */
	public void connectTraceLink(String signalUID, TraceExchangeLink tl) {
		SetupUtilities.connectTraceLink(signalUID, tl);
		tl.startUpdater();
		setChanged();
		setDirty(true);
	}

	/**
	 * Connects the Trace Exchange Link that is mapped to a given Trace t to a
	 * Signal
	 * 
	 * @param signalUID
	 *            unique ID of the Signals
	 * @param t
	 *            the trace that is mapped to the Trace Exchange Link
	 */
	public void connectTraceLink(String signalUID, Trace t) {
		SetupUtilities.connectTraceLink(signalUID, traceMap.get(t));
		traceMap.get(t).startUpdater();
		setChanged();
		setDirty(true);
	}

	public void setTraceBufferSize(int bufferSize, Trace t) {
		traceMap.get(t).setBufferSize(bufferSize);
		setChanged();
		setDirty(true);
	}

	public String getSignalSimpleName(Trace t) {
		String name = null;
		if (DataExchanger.getSignal(traceMap.get(t).getSignalUID()) != null) {
			name = DataExchanger.getSignal(traceMap.get(t).getSignalUID()).getSimpleName();
		}
		return name;
	}

	// ----------------------------- multiple traces

	public int getTraceBufferSize() {
		return trace_buffer_size;
	}

	public int getTracePollingDelay() {
		return trace_pollingDelay;
	}

	public void setTracePollingDelay(int trace_pollingDelay) {
		this.trace_pollingDelay = trace_pollingDelay;
		for (TraceExchangeLink link : traceMap.values()) {
			link.getTraceUpdater().setTracePollingDelay(trace_pollingDelay);
		}
		// mainTraceLink.getTraceUpdater().setTracePollingDelay(trace_pollingDelay);
		setChanged();
		setDirty(true);
	}

	public int getGraphUpdateDelay() {
		return graph_updateDelay;
	}

	public int getGraph_autoscale_delay() {
		return graph_autoscale_delay;
	}

	public void setGraph_autoscale_delay(int graph_autoscale_delay) {
		this.graph_autoscale_delay = graph_autoscale_delay;

		setChanged();
		setDirty(true);
	}

	public GraphView(Composite parent, int style) {
		super(parent, style);

		initContainer();
		representation = new GraphViewRepresentation();

		setChanged();
	}

	public GraphView(Composite parent, int style, GraphViewRepresentation representation) {
		super(parent, style);
		initContainer();
		this.representation = representation;
		// setChanged();
		// applyRepresentation(); //not here
		// xyGraph.performAutoScale();
	}

	private void initContainer() {
		GridLayout gl = new GridLayout(1, true);
		setLayout(gl);

		graphData = new GridData(SWT.FILL, SWT.FILL, true, true);
		graphCanvas = new Canvas(this, SWT.BORDER);
		graphCanvas.setLayoutData(graphData);

		lws = new LightweightSystem(graphCanvas);
		
	}

	public void setGraphName(String title) {
		xyGraph.setTitle(title);
		setChanged();
	}

	public String getGraphName() {
		return xyGraph.getTitle();
	}

	public XYGraph getGraph() {
		return xyGraph;
	}

	public void initializeGraph() {

		graph_updateDelay = 20;
		trace_buffer_size = 2048;

		xyGraph = new XYGraph();
		xyGraph.setTitleFont(new Font(getDisplay(), "Arial", 8, SWT.DEFAULT));

		ToolbarArmedXYGraph toolbarArmedXYGraph = new ToolbarArmedXYGraph(xyGraph);

		toolbarArmedXYGraph.addToolbarButton(generateTraceButton("+T"));
		toolbarArmedXYGraph.addToolbarButton(generateGUIButton());
		toolbarArmedXYGraph.addToolbarButton(generateFollowButton());
		toolbarArmedXYGraph.addToolbarButton(generateClearButton());

		lws.setContents(toolbarArmedXYGraph);

		setPrimaryAxes();

		mainTraceProvider = new CircularBufferDataProvider(true);
		mainTraceProvider.setBufferSize(trace_buffer_size);
		mainTraceProvider.setUpdateDelay(graph_updateDelay);

		// mainTraceLink = new TraceExchangeLink(mainTraceProvider);

		autoscale = new AutoScaler(this);
		autoscale.setFollowingTrace(follow_trace);

		mouseWheelListener = new GraphMouseWheelListener(xyGraph);

		graphCanvas.addMouseWheelListener(mouseWheelListener);
		graphCanvas.addListener(SWT.KeyDown, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if ((e.keyCode == SWT.CTRL)) {
					mouseWheelListener.setZoomXaxis(true);
					mouseWheelListener.setZoomYaxis(false);
		
				}
				if ((e.keyCode == SWT.SHIFT)) {
					mouseWheelListener.setZoomXaxis(false);
					mouseWheelListener.setZoomYaxis(true);

				}
			}
		});

		graphCanvas.addListener(SWT.KeyUp, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if ((e.keyCode == SWT.CTRL)) {
					mouseWheelListener.setZoomYaxis(true);

				}
				if ((e.keyCode == SWT.SHIFT)) {
					mouseWheelListener.setZoomXaxis(true);

				}
			}
		});

		mainTraceProvider.setCurrentYData(0, 0);

		Display.getCurrent().timerExec(graph_autoscale_delay, autoscale);

		popupMenu = new Menu(graphCanvas);
		addTraceItem = new MenuItem(popupMenu, SWT.None);
		addTraceItem.setText("Add Trace");
		addTraceItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addTrace();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		removeTraceItem = new MenuItem(popupMenu, SWT.CASCADE);
		removeTraceItem.setText("Remove Trace");
		fillRemoveTraceItem();
		checkAxes();

		new MenuItem(popupMenu, SWT.SEPARATOR);

		traceSetupItem = new MenuItem(popupMenu, SWT.CASCADE);
		traceSetupItem.setText("Traces Setup");
		traceSetupItem.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callTraceSetupGUI();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		graphCanvas.setMenu(popupMenu);
		
		openSettingsViewItem = new MenuItem(popupMenu, SWT.NONE);
		openSettingsViewItem.setText("Open Settings View");
		openSettingsViewItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/settings-icon.png").createImage());
		
		openSettingsViewItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ViewCoordinator.showSettingsViewpart();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void fillRemoveTraceItem() {
		removeTraceMenu = new Menu(popupMenu);

		for (Trace t : traceMap.keySet()) {
			MenuItem mi = new MenuItem(removeTraceMenu, SWT.None);
			mi.setText(t.getName());
			mi.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					removeTrace(t);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}

		removeTraceItem.setMenu(removeTraceMenu);
	}

	private void setPrimaryAxes() {
		xyGraph.primaryXAxis.setTitle("Y");
		xyGraph.primaryXAxis.setTitleFont(new Font(getDisplay(), "Arial", 1, SWT.DEFAULT));
		xyGraph.primaryXAxis.setRange(new Range(-200, 1000));
		xyGraph.primaryXAxis.setDateEnabled(false);
		xyGraph.primaryXAxis.setTimeUnit(Calendar.MILLISECOND);
		xyGraph.primaryYAxis.setRange(new Range(-50, 50));
		xyGraph.primaryXAxis.setRequestFocusEnabled(true);
		xyGraph.primaryXAxis.setFormatPattern("0.#######");
		xyGraph.primaryXAxis.setAutoScaleThreshold(0.5);

		xyGraph.primaryYAxis.setTitle("");
		xyGraph.primaryYAxis.setTitleFont(new Font(getDisplay(), "Arial", 1, SWT.DEFAULT));
		xyGraph.primaryYAxis.setShowMajorGrid(true);
		xyGraph.primaryYAxis.setAutoScaleThreshold(0.2);

		xyGraph.setFocusTraversable(true);
		xyGraph.setRequestFocusEnabled(true);
	}

	private Button generateGUIButton() {
		Button guiButton = new Button("Set");
		guiButton.setStyle(Clickable.STYLE_BUTTON);
		guiButton.setToolTip(new Label("Trace settings"));
		// GraphView gv = this;
		guiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				callTraceSetupGUI();
			}
		});
		return guiButton;
	}

	private void callTraceSetupGUI() {
		Display display = Display.getCurrent();
		Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(100, 150);
		shell.setLayout(new GridLayout(1, false));
		GraphSettingsUI gs = new GraphSettingsUI(shell, SWT.NONE, this);
		gs.refresh();

		shell.pack();
		shell.open();
	}

	private Button generateFollowButton() {

		

		followButton = new Button(sitting_rabbit);
		followButton.setStyle(Clickable.STYLE_BUTTON);

		followButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				follow_trace = !follow_trace;

				autoscale.setFollowingTrace(follow_trace);

				if (follow_trace) {
					((Label) followButton.getChildren().get(0)).setIcon(running_rabbit);
					Display.getCurrent().timerExec(graph_autoscale_delay, autoscale);
				} else {
					((Label) followButton.getChildren().get(0)).setIcon(sitting_rabbit);
				}
				followButton.repaint();
			}

		});
		return followButton;
	}
	
	public void setFollowTraces(boolean follow) {
		//followButton.setSelected(true);//doClick();
		follow_trace = follow;
		if (follow) {
			((Label) followButton.getChildren().get(0)).setIcon(running_rabbit);
			Display.getCurrent().timerExec(graph_autoscale_delay, autoscale);
		} else {
			((Label) followButton.getChildren().get(0)).setIcon(sitting_rabbit);
		}
		followButton.repaint();
	}

	private Button generateTraceButton(String name) {
		Button addTraceButton = new Button(name);
		addTraceButton.setStyle(Clickable.STYLE_BUTTON);
		addTraceButton.setToolTip(new Label("Add a new Trace"));
		addTraceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addTrace();
			}
		});
		return addTraceButton;
	}

	private Clickable generateClearButton() {
		clearButton = new Button("Clr");
		clearButton.setStyle(Clickable.STYLE_BUTTON);
		clearButton.setToolTip(new Label("Clear all traces"));

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (TraceExchangeLink tel : traceMap.values()) {
					tel.getBuffer().clearTrace();
				}
			}
		});
		return clearButton;
	}
/*
	@Override
	public void notifyObservers() {
		for (GraphViewObserver o : observers) {
			o.update(this);
		}
		clearChanged();
	}*/

	public void registerLinks() {
		for (TraceExchangeLink link : traceMap.values()) {
			DataExchanger.replaceSignal(link.getSignalUID(), link);
		}
	}

	@Override
	public void setChanged() {
		hasChanged = true;
	}

	// --------------------- Representation
	@Override
	public void setRepresentation(GraphViewRepresentation representation) {
		System.out.println("GraphView: setting representation...");
		if (representation != null) {
			this.representation = representation;
			applyRepresentation();
		} else {
			System.out.println("GraphView: Chosen representation is null, operation aborted");
		}
	}

	@Override
	public GraphViewRepresentation getRepresentation() {
		return representation;
	}

	@Override
	public void updateRepresentation() {

		representation.traceRepresentations = new ArrayList<TraceRepresentation>();

		for (Trace t : traceMap.keySet()) {
			traceMap.get(t).updateRepresentation();

			TraceRepresentation tr = traceMap.get(t).getRepresentation();
			tr.setTraceName(t.getName());
			tr.setRed(t.getTraceColor().getRed());
			tr.setGreen(t.getTraceColor().getGreen());
			tr.setBlue(t.getTraceColor().getBlue());
			tr.setType(t.getTraceType());
			tr.setPointStyle(t.getPointStyle());

			representation.traceRepresentations.add(tr);
		}

		representation.setPrimaryXLogarithmic(xyGraph.primaryXAxis.isLogScaleEnabled());
		representation.setMaxPrimaryXrange(xyGraph.primaryXAxis.getRange().getUpper());
		representation.setMinPrimaryXrange(xyGraph.primaryXAxis.getRange().getLower());
		

	}

	@Override
	public void applyRepresentation() {
		
		if(editor != null) xyGraph.setTitle(editor.getPartName());
		xyGraph.primaryXAxis.setRange(representation.getMinPrimaryXrange(), representation.getMaxPrimaryXrange());
		xyGraph.primaryXAxis.setLogScale(representation.isPrimaryXLogarithmic());

		for (Trace t : traceMap.keySet()) {
			xyGraph.removeTrace(t);
			traceMap.get(t).drop();
			setChanged();
			setDirty(true);
		}
		
		for (Axis a : axisYList) {
			xyGraph.removeAxis(a);
		}
		axisYList.clear();

		traceMap.clear();

		fillRemoveTraceItem();

		// traceMap.keySet().removeAll(traceMap.keySet());

		for (TraceRepresentation tr : representation.traceRepresentations) {
			System.out.println("Applying trace representation for Trace " + tr.getTraceName());
			Trace t = addTrace(tr.getTraceName(), new TraceExchangeLink(tr));
			t.setTraceType(tr.getType());
			//t.setPointStyle(tr.getPointStyle());
			t.setTraceColor(new Color(Display.getCurrent(), tr.getRed(), tr.getGreen(), tr.getBlue()));
			
		}
	}

	public void addTraces(ArrayList<Signal> selected_in_signals) {
		for (Signal s : selected_in_signals) {
			TraceExchangeLink link = addTraceGetLink(s.getSimpleName());
			link.setSignalUID(s.getId());
			DataExchanger.replaceSignal(s.getId(), link);
			// TODO Test
			link.updateRepresentation();
		}

	}

	public void setEditor(GraphViewEditor graphViewEditor) {
		this.editor = graphViewEditor;
	}
	
	private void checkAxes() {
		boolean enablePrimaryY = !(xyGraph.getYAxisList().size() > 1);
		xyGraph.primaryYAxis.setVisible(enablePrimaryY);
	}
}
