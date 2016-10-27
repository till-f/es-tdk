package fzi.mottem.runtime.rtgraph;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import org.csstudio.swt.widgets.figures.AbstractMarkedWidgetFigure;
import org.csstudio.swt.widgets.figures.KnobFigure;
import org.csstudio.swt.widgets.figures.ScaledSliderFigure;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.dataexchanger.Signal.SignalType;
import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink.WidgetType;
import fzi.mottem.runtime.rtgraph.XML.ProfileUtils;
import fzi.mottem.runtime.rtgraph.runnables.WidgetUpdater;
import fzi.mottem.runtime.rtgraph.settingsViews.SetupUI;
import fzi.mottem.runtime.rtgraph.settingsViews.WidgetSettingsUI;
import fzi.mottem.runtime.rtgraph.views.Dashboard;
import fzi.mottem.runtime.rtgraph.views.GraphView;

public class SetupUnit {


	static IWorkspace workspace;
	public static File workspaceDirectory;
	public static String workspace_path;

	// public static ArrayList<WidgetConsumerLink> widgetLinks;
	public static ArrayList<ControllerWidgetLink> controllerLinks;
	public static ArrayList<String> graphViewNames;

	public static ArrayList<GraphView> graphViews;
	private static ArrayList<Signal> signals;
	private static ArrayList<Signal> bi_signals;
	private static ArrayList<Signal> hwout_signals;
	private static ArrayList<Signal> hwin_signals;
	private static ArrayList<Signal> static_signals;

	public static ArrayList<String> string_signals;

	static WidgetSettingsUI widgetSettings;
	// public static Dashboard widgetsView;
	WidgetUpdater widgetUpdater;

	private static boolean testMode = false;

	public static ArrayList<Dashboard> dashboards;

	static {

		try {

			workspace = ResourcesPlugin.getWorkspace();
			workspaceDirectory = workspace.getRoot().getLocation().toFile();
			workspace_path = workspaceDirectory.getAbsolutePath();

			// widgetLinks = new ArrayList<WidgetConsumerLink>();
			controllerLinks = new ArrayList<ControllerWidgetLink>();
			//widgetsViewObserver = new WidgetViewObserver(profile);
			graphViewNames = new ArrayList<String>();

			graphViews = new ArrayList<GraphView>();
			signals = new ArrayList<Signal>();
			bi_signals = new ArrayList<Signal>();
			hwout_signals = new ArrayList<Signal>();
			hwin_signals = new ArrayList<Signal>();
			static_signals = new ArrayList<Signal>();
			string_signals = new ArrayList<String>();

			// ProfileUtils.loadDefaultProfile();
			// ViewCoordinator.showDashboardViewpart();
			ViewCoordinator.showSettingsViewpart();
			// generateGraphViews();
			// generateDashboards();

		} catch (Exception e) {
			System.out.println("SetupUnit: Failure during static initialization: " + e);
			e.printStackTrace();
		}

	}

	private SetupUnit() {
		
	}
	

	public static Dashboard getDashboard(int index) {
		return dashboards.get(index);
	}

	public static void setWidgetsPollingDelay(int widget_polling_delay) {
		for (Dashboard d : dashboards) {
			d.setWidgetPollingDelay(widget_polling_delay);
		}
	}

	// Graph and trace methods --------------------------
	public static void setTracePollingDelay(int trace_polling_delay) {
		for (GraphView g : graphViews) {
			g.setTracePollingDelay(trace_polling_delay);
		}
	}

	public static ArrayList<Signal> getSignals() {
		return signals;
	}

	public static ArrayList<Signal> getSignals(SignalType type) {
		if (type == SignalType.BIDIRECTIONAL) {
			return bi_signals;
		} else if (type == SignalType.HW_OUTPUT) {
			return hwout_signals;
		} else if (type == SignalType.HW_INPUT) {
			return hwin_signals;
		} else {
			return static_signals;
		}
	}

	/**
	 * Reloads the currently functioning GraphViews from the ViewCoordinator
	 */
	public static void reloadGraphViews() {
		graphViews = ViewCoordinator.getGraphViews();
	}

	public static void reloadDashboards() {
		dashboards = ViewCoordinator.getDashboards();
	}

	/**
	 * Calls the DataExchanger and gathers the known Signals from it. The
	 * Signals will be sorted alphabetically by their simple name. The ArrayList
	 * consisting of the Signals' simple names is also updated.
	 */
	public static void refreshSignals() {
		signals = new ArrayList<Signal>(DataExchanger.getSignals());
		string_signals.clear();
		bi_signals.clear();
		hwin_signals.clear();
		hwout_signals.clear();
		static_signals.clear();

		// sort alphabetically
		signals.sort(new Comparator<Signal>() {
			@Override
			public int compare(Signal o1, Signal o2) {
				return o1.getSimpleName().compareTo(o2.getSimpleName());
			}
		});

		for (Signal s : signals) {
			string_signals.add(s.getSimpleName());
			if (s.getType() == SignalType.BIDIRECTIONAL) {
				bi_signals.add(s);
			} else if (s.getType() == SignalType.HW_INPUT) {
				hwin_signals.add(s);
			} else if (s.getType() == SignalType.HW_OUTPUT) {
				hwout_signals.add(s);
			} else {
				static_signals.add(s);
			}
		}
	}

	/**
	 * The SetupUnit will attempt to connect the currently available Widgets /
	 * Widget Exchange Links and Controllers to Signals in the DataExchanger by
	 * using their Signal UIDs
	 */
	public static void autoConnectWidgets() {
		reloadDashboards();

		for (Dashboard d : dashboards) {
			for (AbstractWidgetExchangeLink link : d.getWidgetLinks()) {
				if (link.getWidgetType() == WidgetType.W_INDICATOR)
					DataExchanger.registerConsumer(link.getRepresentation().getSignalUID(), link);
			}
		}
	}

	/**
	 * The SetupUnit will attempt to connect the currently available GraphViews
	 * and their Trace Exchange Links to Signals in the DataExchanger by using
	 * their Signal UIDs
	 */
	public static void autoConnectGraphViews() {
		reloadGraphViews();
		for (int i = 0; i < graphViews.size(); i++) {
			graphViews.get(i).registerLinks();
		}
	}

	public static void connectController(int si, int li) {
		controllerLinks.get(li).setSignal(signals.get(si));
	}

	/**
	 * Will try to register a widget exchange link based to its signal UID
	 * 
	 * @param link
	 *            the MarkedFigureExchangeLink that needs to be registered
	 */
	public static void connectWidget(IndicatorWidgetLink link) {
		DataExchanger.registerConsumer(link.getRepresentation().getSignalUID(), link);
	}

	/*
	 * public static void connectWidget(int signalIndex, int widgetLinkIndex) {
	 * DataExchanger.registerConsumer(signals.get(signalIndex),
	 * widgetLinks.get(widgetLinkIndex));
	 * widgetLinks.get(widgetLinkIndex).setSignalUID(signals.get(signalIndex).
	 * getId()); }
	 */

	public static void connectWidget(Signal signal, IndicatorWidgetLink current_link) {
		DataExchanger.registerConsumer(signal, current_link);
		current_link.setSignalUID(signal.getId());
		current_link.setSignalSimpleName(signal.getSimpleName());
	}

	/**
	 * Will try to register a trace exchange link based to its signal UID
	 * 
	 * @param link
	 *            the TraceExchangeLink that needs to be registered
	 */
	public static void connectTraceLink(TraceExchangeLink link) {
		DataExchanger.registerConsumer(link.getSignalUID(), link);
	}

	/**
	 * Will connect a trace link with a given name to a signal defined by its
	 * index in the SetupUnit.
	 * 
	 * @param signalIndex
	 *            index of the Signal
	 * @param link
	 *            the TraceExchangeLink
	 */
	public static void connectTraceLink(int signalIndex, TraceExchangeLink link) {
		if (DataExchanger.registerConsumer(signals.get(signalIndex), link)) {
			link.setSignalUID(signals.get(signalIndex).getId());
		} else {
			System.out.println("SetupUnit: could not succesfully register link with name " + link.getName()
					+ " for signal at index " + signalIndex);
		}
	}

	/**
	 * Will connect a trace link with a given name to a signal defined by its
	 * unique ID
	 * 
	 * @param signalUID
	 *            unique ID of the signal
	 * @param link
	 *            the TraceExchangeLink
	 */
	public static void connectTraceLink(String signalUID, TraceExchangeLink link) {
		if (DataExchanger.registerConsumer(signalUID, link)) {
			link.setSignalUID(signalUID);
		} else {
			System.out.println("SetupUnit: could not succesfully register link with name " + link.getName()
					+ " for signal UID " + signalUID);
		}
	}

	/**
	 * Presents a new widget settings window
	 */
	

	public static void presentWidgetSettings(AbstractWidgetExchangeLink link) {

		Display display = Display.getCurrent();
		Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setSize(100, 150);
		shell.setLayout(new GridLayout(2, false));
		widgetSettings = new WidgetSettingsUI(shell, SWT.NONE);
		// ------

		// ------
		widgetSettings.refresh();

		widgetSettings.focusOnLink(link);

		widgetSettings.setWidgetsSelectable(false);
		shell.pack();
		shell.open();
		
	}


	private static AbstractMarkedWidgetFigure createControllerFigure(int type) {
		final AbstractMarkedWidgetFigure figure;

		switch (type) {
		case Constants.CONTROLLER_KNOB:
			figure = new KnobFigure();
			figure.setValueLabelFormat("0.#");
			break;
		case Constants.CONTROLLER_SLIDE:
			figure = new ScaledSliderFigure();
			figure.setValueLabelFormat("0.#");
			break;
		default:
			figure = new KnobFigure();
		}
		return figure;
	}

	public static void setControllerType(int link_index, int figure_type) {
		final AbstractMarkedWidgetFigure figure = createControllerFigure(figure_type);
		controllerLinks.get(link_index).updateFigure(figure);
	}

	// Profile save/load methods and manipulation methods

	/**
	 * @deprecated Use {@link ProfileUtils#gatherProfileNames()} instead
	 */
	public static ArrayList<String> gatherProfileNames() {
		return ProfileUtils.gatherProfileNames();
	}
}
