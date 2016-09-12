package fzi.mottem.runtime.rtgraph.settingsViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal.SignalType;
import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.ControllerWidgetLink;
import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;
import fzi.mottem.runtime.rtgraph.SetupUnit;
import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;
import fzi.mottem.runtime.rtgraph.listeners.DoubleDigitListener;
import fzi.mottem.runtime.rtgraph.listeners.IntegerListener;


public class ControllerSettingsUI extends WidgetSettingsUI {

	// private ArrayList<MarkedFigureExchangeLink> links;
	private ControllerWidgetLink current_link;
	
	
	protected String types[] = Constants.CONTROLLER_TYPES;	

	private Button add_consumer_button;

	public ControllerSettingsUI(Composite parent, int style) {
		super(parent, style);
		types_combo.setItems(types);
	}

	public void refreshSignalsLinks() {
		signals = SetupUnit.getSignals(SignalType.HW_INPUT);
	}

	protected void initInnerControls() {
		initCombos();

		setDataRangeButton = new Button(basicSettings, SWT.PUSH);
		setDataRangeButton.setText("Set");

		Label l1 = new Label(rangeSettings, SWT.NONE);
		l1.setText("Min");
		min = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);

		Label l2 = new Label(rangeSettings, SWT.NONE);
		l2.setText("lo");
		lo = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);

		Label l3 = new Label(rangeSettings, SWT.NONE);
		l3.setText("hi");
		hi = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);

		Label l4 = new Label(rangeSettings, SWT.NONE);
		l4.setText("Max");
		max = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);

		removeButton = new Button(rangeSettings, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));

		setDataRangeButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));

		GridData dataText = new GridData(SWT.FILL, SWT.DEFAULT, false, false);
		dataText.widthHint = 40;
		min.setLayoutData(dataText);
		max.setLayoutData(dataText);
		lo.setLayoutData(dataText);
		hi.setLayoutData(dataText);

		Label lx = new Label(positionSettings, SWT.NONE);
		lx.setText("x:");
		x = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		Label ly = new Label(positionSettings, SWT.NONE);
		ly.setText("y:");
		y = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		Label lw = new Label(positionSettings, SWT.NONE);
		lw.setText("w:");
		w = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		Label lh = new Label(positionSettings, SWT.NONE);
		lh.setText("h:");
		h = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);

		x.addVerifyListener(new IntegerListener());
		x.setLayoutData(dataText);
		y.addVerifyListener(new IntegerListener());
		y.setLayoutData(dataText);
		w.addVerifyListener(new IntegerListener());
		w.setLayoutData(dataText);
		h.addVerifyListener(new IntegerListener());
		h.setLayoutData(dataText);

		add_consumer_button = new Button(this, SWT.PUSH);
		add_consumer_button.setText("Add Widget");
		
		this.layout(true);
	}

	public void refreshCombos() {
		
		String ss = "";
		
		if(signals_combo.getSelectionIndex() > -1) {
			ss = signals.get(signals_combo.getSelectionIndex()).getId();
		}
		
		refreshSignalsLinks();

		links_combo.removeAll();
		signals_combo.removeAll();

		links_combo.setText("--Widget--");
		signals_combo.setText("---Signal---");

		// TODO add private function for link combo initialization
		for (int i = 0; i < SetupUnit.controllerLinks.size(); i++) {

			links_combo.add(i + " " + SetupUnit.controllerLinks.get(i).getFigure().getClass().getSimpleName());
			if (current_link == SetupUnit.controllerLinks.get(i)) {
				links_combo.select(i);
			}
		}

		for (int i = 0; i < signals.size(); i++) {
			signals_combo.add(signals.get(i).getSimpleName());
			if (ss.equals(signals.get(i).getId()))
				signals_combo.select(i);
			signalIndex = i;
		}

		basicSettings.layout(true);
	}
	
	protected void initListeners() {

		digitListener = new DoubleDigitListener();

		min.addVerifyListener(digitListener);
		lo.addVerifyListener(digitListener);
		hi.addVerifyListener(digitListener);
		max.addVerifyListener(digitListener);

		links_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				linkIndex = links_combo.getSelectionIndex();

				if (linkIndex != -1) {
					focusOn(linkIndex);
					checkLogarithmicScale.setSelection(SetupUnit.controllerLinks.get(linkIndex).isLogarithmic());
				} else {
					current_link = null;
				}
			}
		});

		setButtonListener = new Listener() {
			int si; // signal index
			int li; // widget link index
			int ti; // type index

			@Override
			public void handleEvent(Event event) {

				li = links_combo.getSelectionIndex();
				si = signals_combo.getSelectionIndex();
				ti = types_combo.getSelectionIndex();

				if (li != -1) {

					if (min.getText().length() > 0 && max.getText().length() > 0)
						current_link.setFigureRange(Double.parseDouble(min.getText()),
								Double.parseDouble(max.getText()));

					if (lo.getText().length() > 0)
						current_link.setFigureLo(Double.parseDouble(lo.getText()));
					if (hi.getText().length() > 0)
						current_link.setFigureHi(Double.parseDouble(hi.getText()));
					if (si != -1) { // if signal and widget are
									// selected, then connect them
						SetupUnit.connectController(si, li);
					}

					if (ti != -1) {
						SetupUnit.setControllerType(li, ti);
					}

					if (x.getText().length() > 0 && y.getText().length() > 0) {
						current_link.setXY(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()),
								Integer.parseInt(w.getText()), Integer.parseInt(h.getText()));
					}
					current_link.refresh();
				}

				refreshCombos();
				links_combo.select(SetupUnit.controllerLinks.indexOf(current_link));
				layout(true);
			}
		};

		setDataRangeButton.addListener(SWT.Selection, setButtonListener);

		removeButtonListener = new Listener() {

			int li; // widget link index

			@Override
			public void handleEvent(Event event) {
				li = links_combo.getSelectionIndex();

				if (li != -1) {
					SetupUnit.disconnectControllerLink(li);
					refreshCombos();
					layout(true);
				}
			}
		};

		removeButton.addListener(SWT.Selection, removeButtonListener);
		
		add_consumer_button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if(types_combo.getSelectionIndex() > -1) {
					WidgetRepresentation newFigureRep = new WidgetRepresentation();
					if (x.getText().length() > 0) {
						newFigureRep.setX(Integer.parseInt(x.getText()));
					}
					if (y.getText().length() > 0) {
						newFigureRep.setY(Integer.parseInt(y.getText()));
					}
					if (w.getText().length() > 0) {
						newFigureRep.setWidth(Integer.parseInt(w.getText()));
					}
					if (h.getText().length() > 0) {
						newFigureRep.setHeight(Integer.parseInt(h.getText()));
					}
					newFigureRep.setType(types_combo.getSelectionIndex());
					current_link.getDashboard().addControllerWidget(newFigureRep);
					refresh();
				}
			}
		});
		
		checkLogarithmicScale.addSelectionListener(new SelectionAdapter() {
	        @Override
	        public void widgetSelected(SelectionEvent event) {
	            Button btn = (Button) event.getSource();
	            if(links_combo.getSelectionIndex() >= 0) {
	            	SetupUnit.controllerLinks.get(links_combo.getSelectionIndex()).setLogarithmic(btn.getSelection());
	            	SetupUnit.controllerLinks.get(links_combo.getSelectionIndex()).getCanvas().layout(true);
            	}
	        }
	    });
		
	}

	public void refresh() {
		refreshCombos();
		this.layout(true);
	}

	public void focusOnLink(IndicatorWidgetLink link) {
		if (SetupUnit.controllerLinks.contains(link)) {
			focusOn(SetupUnit.controllerLinks.indexOf(link));
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void focusOn(int link_index) {
		System.out.println("WidgetSettings: focus on index " + link_index);
		current_link = SetupUnit.controllerLinks.get(link_index);

		links_combo.select(link_index);

		if (DataExchanger.getSignal(current_link.getRepresentation().getSignalUID()) != null) {
			String signal_simple_name = DataExchanger.getSignal(current_link.getRepresentation().getSignalUID())
					.getSimpleName();
			if (signal_simple_name != null) {
				signals_combo.select(signals_combo.indexOf(signal_simple_name));
			}
		}

		int type = current_link.getRepresentation().getType();

		if (type >= 0 && type < Constants.WIDGET_TYPES.length) {
			types_combo.select(current_link.getType());
		}
		setWidgetTexts();
	}

	public void setWidgetsSelectable(boolean selectable) {
		links_combo.setEnabled(selectable);
	}

	public void setWidgetTexts() {
		if (current_link == null)
			return;
		
		try {
			/*min.setText(Double.toString(current_link.getMin()));
			max.setText(Double.toString(current_link.getMax()));
			lo.setText(Double.toString(current_link.getLo()));
			hi.setText(Double.toString(current_link.getHi()));*/
			x.setText(Integer.toString(current_link.getCanvas().getBounds().x));
			y.setText(Integer.toString(current_link.getCanvas().getBounds().y));
			w.setText(Integer.toString(current_link.getCanvas().getBounds().width));
			h.setText(Integer.toString(current_link.getCanvas().getBounds().height));

		} catch (Exception e) {
			System.err.println("widget link error: " + e.getMessage());
		}
	}
}
