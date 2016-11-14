package fzi.mottem.runtime.rtgraph.settingsViews;

import java.util.ArrayList;

import org.csstudio.swt.xygraph.figures.Trace;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.rtgraph.SetupUtilities;
import fzi.mottem.runtime.rtgraph.TraceExchangeLink;
import fzi.mottem.runtime.rtgraph.listeners.IntegerListener;
import fzi.mottem.runtime.rtgraph.views.GraphView;

public class GraphSettingsUI extends Composite {

	public GraphSettingsUI(Composite parent, int style) {
		super(parent, style);
	}

	public GraphSettingsUI(Composite parent, int style, GraphView graphView) {
		this(parent, style);
		this.graphView = graphView;
		initLayout();
		initInnerControls();
		initListeners();
	}

	private GraphView graphView;

	public GraphView getGraphView() {
		return graphView;
	}

	public void setGraphView(GraphView graphView) {
		this.graphView = graphView;
	}

	private TraceExchangeLink current_link;
	private ArrayList<Signal> signals;
	

	int linkIndex;
	int signalIndex;

	Listener sComboListener;
	Listener lComboListener;

	Listener setButtonListener;
	Listener removeButtonListener;
	Button setButton;
	Button removeButton;

	Label tracesLabel;
	Label signalsLabel;
	Combo traces_combo;
	Combo signals_combo;
	
	Label bufferLabel;
	Text bufferSize;

	ArrayList<Trace> traces = new ArrayList<Trace>();

	private int colsNum = 2;
	
	private Button clearButton;
	private Listener clearButtonListener;

	public void refreshSignalsLinks() {
		signals = SetupUtilities.getSignals();
	}

	private void initLayout() {
		this.setLayout(new GridLayout(colsNum, false));

		GridData data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
	}

	private void initInnerControls() {
		initCombos();
		
		setButton = new Button(this, SWT.PUSH);
		setButton.setText("Set");

		removeButton = new Button(this, SWT.PUSH);
		removeButton.setText("Remove Trace");
		
		GridData dataText = new GridData(SWT.FILL, SWT.DEFAULT, false, false);
		dataText.widthHint = 40;
		
		bufferLabel = new Label(this, SWT.NONE);
		bufferLabel.setText("Buffer size:");
		bufferSize = new Text(this, SWT.FILL | SWT.SINGLE | SWT.BORDER);
		bufferSize.addVerifyListener(new IntegerListener());
		bufferSize.setLayoutData(dataText);
		
		clearButton = new Button(this, SWT.PUSH);
		clearButton.setText("Clear Trace");
		
		clearButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));
		setButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));
		
		this.layout(true);
	}

	public void refreshCombos() {

		refreshSignalsLinks();

		String ls = traces_combo.getText();

		traces_combo.removeAll();

		traces_combo.setText("--Trace--");
		
		traces = graphView.getTraces();

		for (int i = 0; i < traces.size(); i++) {
			traces_combo.add(traces.get(i).getName());
			if (ls.equals(traces.get(i).getName())) {
				traces_combo.select(i);
				linkIndex = i;
			}
		}
		
		signals_combo.removeAll();
		
		for (int i = 0; i < signals.size(); i++) {
			signals_combo.add(signals.get(i).getSimpleName());
			/*if (ss.equals(SetupUnit.string_signals.get(i)))
				selection = i;
			signalIndex = i;*/
		}
		signals_combo.setText("---Signal---");
		
		if(traces_combo.indexOf(ls) > 0) {
			focusOn(traces_combo.indexOf(ls));
		}
		
	}

	private void initCombos() {
		
		tracesLabel = new Label(this, SWT.NONE);
		tracesLabel.setText("Trace:");
		traces_combo = new Combo(this, SWT.DROP_DOWN | SWT.HORIZONTAL);

		signalsLabel = new Label(this, SWT.NONE);
		signalsLabel.setText("Signal:");
		signals_combo = new Combo(this, SWT.DROP_DOWN);

		GridData dataCombo = new GridData(SWT.FILL, SWT.DEFAULT, false, false);
		dataCombo.widthHint = 140;

		signals_combo.setLayoutData(dataCombo);
		traces_combo.setLayoutData(dataCombo);

		traces_combo.setText("---Trace---");
		signals_combo.setText("---Signal---");

		refreshCombos();
	}

	private void initListeners() {

		traces_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				linkIndex = traces_combo.getSelectionIndex();

				if (linkIndex != -1) {
					focusOn(linkIndex);
				} else {
					current_link = null;
				}
			}
		});

		signals_combo.addListener(SWT.SELECTED, new Listener() {

			@Override
			public void handleEvent(Event event) {
				signals_combo.removeAll();

				for (int i = 0; i < SetupUtilities.string_signals.size(); i++) {
					signals_combo.add(SetupUtilities.string_signals.get(i));
				}

			}
		});
		
		signals_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				signalIndex = signals_combo.getSelectionIndex();
				if (signalIndex != -1 && current_link != null) {				
					graphView.connectTraceLink(signals.get(signalIndex).getId(), current_link);
				}
			}
		});
		
		setButtonListener = new Listener() {
			int si; // signal index
			int ti; // widget link index

			@Override
			public void handleEvent(Event event) {

				ti = traces_combo.getSelectionIndex();
				si = signals_combo.getSelectionIndex();

				if (ti != -1) {
					if (si != -1) {
						graphView.connectTraceLink(si, traces.get(ti));
					}
					if(bufferSize.getText().length() > 0) {
						graphView.setTraceBufferSize(Integer.parseInt(bufferSize.getText()), traces.get(ti));
					}
				}
				refreshCombos();
				//traces_combo.select(SetupUnit.widgetLinks.indexOf(current_link));
				layout(true);
			}
		};

		removeButtonListener = new Listener() {

			int ti; // widget link index

			@Override
			public void handleEvent(Event event) {
				ti = traces_combo.getSelectionIndex();

				if (ti != -1) {
					graphView.removeTrace(traces.get(ti));
					refreshCombos();
					layout(true);
				}
			}
		};
		
		clearButtonListener = new Listener() {
			int ti; // widget link index
			@Override
			public void handleEvent(Event event) {
				ti = traces_combo.getSelectionIndex();

				if (ti >= 0 && ti < graphView.getTraces().size()) {
					graphView.getTraceExchangeLink(traces.get(ti)).getBuffer().clearTrace();
					//refreshCombos();
					layout(true);
				}
			}	
		};
		
		setButton.addListener(SWT.Selection, setButtonListener);
		removeButton.addListener(SWT.Selection, removeButtonListener);
		clearButton.addListener(SWT.Selection, clearButtonListener);
		
		bufferSize.addVerifyListener(new IntegerListener());
	}

	public void refresh() {
		refreshCombos();

		this.layout(true);
	}

	public ArrayList<Signal> getSignals() {
		return signals;
	}

	public void setSignals(ArrayList<Signal> signals) {
		this.signals = signals;
	}

	/*public void focusOnLink(WidgetConsumerLink link) {
		if (SetupUnit.widgetLinks.contains(link)) {
			focusOn(SetupUnit.widgetLinks.indexOf(link));
		} else {
			throw new IllegalArgumentException();
		}
	}*/

	public void focusOn(int traceIndex) {
		Trace t = traces.get(traceIndex);
		
		String signal = graphView.getSignalSimpleName(t);
		
		current_link = graphView.getTraceExchangeLink(t);
		
		if (signal != null) {
			System.out.println("GraphViewSettings: signal found, focus on index " + traceIndex);
			signals_combo.select(signals_combo.indexOf(signal));
		} else {
			signals_combo.clearSelection();
			signals_combo.setText("---Signal---");
		}
		
	}

	public void setTracessSelectable(boolean selectable) {
		traces_combo.setEnabled(selectable);
	}
}
