package fzi.mottem.runtime.rtgraph.settingsViews;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.dataexchanger.Signal.SignalType;
import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink;
import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink.WidgetType;
import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;
import fzi.mottem.runtime.rtgraph.SetupUnit;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.listeners.DoubleDigitListener;
import fzi.mottem.runtime.rtgraph.listeners.IntegerListener;
import fzi.mottem.runtime.rtgraph.views.Dashboard;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;

public class WidgetSettingsUI extends Composite {

	private class SizeButtonListener implements Listener {

		int inc = 10;
		int widthInc = 0;
		int heightInc = 0;
		int li; // widget link index

		public SizeButtonListener(boolean width, boolean plus) {
			if (!plus) {
				inc = -10;
			}
			if (width) {
				widthInc = inc;
			} else {
				heightInc = inc;
			}
		}

		Canvas c;

		@Override
		public void handleEvent(Event event) {
			li = links_combo.getSelectionIndex();
			if (li != -1) {
				c = current_link.getCanvas();
				c.setSize(new Point(c.getSize().x + widthInc, c.getSize().y + heightInc));
				current_link.updateRepresentation(false, true, false);
				setWidgetTexts(current_link);
			}
		}
	}
	
	private class PositionButtonListener implements Listener {

		int inc = 10;
		int widthInc = 0;
		int heightInc = 0;
		int li; // widget link index

		public PositionButtonListener(boolean x, boolean plus) {
			if (!plus) {
				inc = -10;
			}
			if (x) {
				widthInc = inc;
			} else {
				heightInc = inc;
			}
		}

		Canvas c;

		@Override
		public void handleEvent(Event event) {
			li = links_combo.getSelectionIndex();
			if (current_link != null && current_link.getCanvas() != null) {
				c = current_link.getCanvas();
				c.setBounds(c.getBounds().x + widthInc, c.getBounds().y + heightInc, c.getSize().x, c.getSize().y);
				current_link.updateRepresentation(false, true, false);
				setWidgetTexts(current_link);
			}
		}
	}

	// private ArrayList<MarkedFigureExchangeLink> links;
	private AbstractWidgetExchangeLink current_link;
	private int style = SWT.None;

	int linkIndex = -1;
	int signalIndex = -1;

	Text min, max, lo, hi;
	Text x, y, w, h;

	
	AdvancedWidgetSettingsUI advUI;

	List<Text> rangeFields;
	VerifyListener digitListener;

	Listener sComboListener;
	Listener lComboListener;
	Listener tComboListener;

	Listener setButtonListener;
	Listener removeButtonListener;

	Button setDataRangeButton;
	Button removeButton;

	Text textField;
	Label textFieldLabel;
	Button browseImageButton;

	Button widthPlus;
	Button widthMinus;
	Button heightPlus;
	Button heightMinus;

	Button checkLogarithmicScale;

	Group range_group;

	// Combo dashboards_combo;
	Combo links_combo;
	Combo signals_combo;
	Combo types_combo;

	private String format_tooltip = "Uses java.text.DecimalFormat class to format the value labels;\n"
			+ "0	Number	Digit\n " + "#	Number	Digit, zero shows as absent \n."
			+ ".	Number	Decimal separator or monetary decimal separator\n" + "-	Number	Minus sign\n"
			+ ",	Number	Grouping separator\n "
			+ "E	Number	Separates mantissa and exponent in scientific notation. Need not be quoted in prefix or suffix.\n"
			+ "%	Prefix or suffix Multiply by 100 and show as percentage\n"
			+ "An example floating point format with 2 digit precision would be #.#";

	private int colsNum = 2;

	protected Group basicSettings;
	protected Group rangeSettings;
	protected Group positionSettings;
	protected String[] indicator_types = Constants.WIDGET_TYPES;
	protected String[] controller_types = Constants.CONTROLLER_TYPES;
	protected ArrayList<Signal> signals;
	private DashboardComposite dashboard;
	// protected ArrayList<DashboardComposite> dashboards;
	private ArrayList<Signal> out_signals;
	private Text valueFormatText;
	private Listener rangeKeyListener;
	private Button xPlus;
	private Button xMinus;
	private Button yPlus;
	private Button yMinus;
	private Group miscSettings;

	public WidgetSettingsUI(Composite parent, int style) {
		super(parent, style);
		this.style = style;
		if (ViewCoordinator.getDashboardEditors().size() > 0)
			dashboard = ViewCoordinator.getDashboardEditors().get(0);
		refreshSignalsLinks();
		initLayout();
		initInnerControls();
		initListeners();
	}

	public void setAndFocusDashboard(DashboardComposite dashboard) {
		System.out.println("WS: setAndFocusDashboard " + dashboard);
		this.dashboard = dashboard;
		if (this.dashboard.getCurrent_link() != null) {
			current_link = this.dashboard.getCurrent_link();
		} else if (this.dashboard.widgetLinks.size() > 0) {
			focusOnLink(dashboard.getWidgetLinks().get(0));
		} else {
			deFocus();
		}
		refresh();
	}

	public void refreshSignalsLinks() {
		signals = SetupUnit.getSignals();

		out_signals = SetupUnit.getSignals(SignalType.HW_OUTPUT);
	}

	protected void initLayout() {
		this.setLayout(new GridLayout(colsNum, false));

		GridData positionsData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);

		GridData data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		data.verticalAlignment = SWT.FILL;

		basicSettings = new Group(this, style);
		basicSettings.setText("Signal and Type");
		basicSettings.setLayout(new GridLayout(2, false));
		basicSettings.setLayoutData(data);
		
		miscSettings = new Group(this, style);
		miscSettings.setLayout(new GridLayout(colsNum, false));
		miscSettings.setLayoutData(positionsData);
		miscSettings.setText("Misc");
		
		positionSettings = new Group(this, style);
		positionSettings.setLayout(new GridLayout(4, false));
		positionSettings.setLayoutData(positionsData);
		positionSettings.setText("Position");

		rangeSettings = new Group(this, style);
		rangeSettings.setLayout(new GridLayout(2, false));
		rangeSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		rangeSettings.setText("Data Range");

		Label logScale = new Label(rangeSettings, SWT.NONE);
		logScale.setText("Logarithmic:");
		checkLogarithmicScale = new Button(rangeSettings, SWT.CHECK);

		// rangeSettings.setLayoutData(data);
	}

	protected void initSizeButtons() {
		/*Composite wb = new Composite(positionSettings, SWT.NONE);
		Label wl = new Label(wb, SWT.NONE);
		wl.setText("Width");

		wb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		wb.setLayout(new GridLayout(3, false));*/
		/*widthPlus = new Button(wb, SWT.PUSH);
		widthPlus.setText("+");
		widthPlus.addListener(SWT.Selection, new SizeButtonListener(true, true));
		widthMinus = new Button(wb, SWT.PUSH);
		widthMinus.setText("-");
		widthMinus.addListener(SWT.Selection, new SizeButtonListener(true, false));*/

		/*Composite hb = new Composite(positionSettings, SWT.NONE);
		Label hl = new Label(hb, SWT.NONE);
		hl.setText("Height");

		hb.setLayout(new GridLayout(3, false));
		hb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));*/
		/*heightPlus = new Button(hb, SWT.PUSH);
		heightPlus.setText("+");
		heightPlus.addListener(SWT.Selection, new SizeButtonListener(false, true));
		heightMinus = new Button(hb, SWT.PUSH);
		heightMinus.setText("-");
		heightMinus.addListener(SWT.Selection, new SizeButtonListener(false, false));*/
	}

	protected void initInnerControls() {
		initCombos();

		GridData rangeSLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		rangeSLayoutData.widthHint = 40;

		Label numberFormatLabel = new Label(rangeSettings, SWT.NONE);
		numberFormatLabel.setText("Format: ");
		valueFormatText = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		valueFormatText.setLayoutData(rangeSLayoutData);
		valueFormatText.setToolTipText(format_tooltip);
		numberFormatLabel.setToolTipText(format_tooltip);

		Label l1 = new Label(rangeSettings, SWT.NONE);
		l1.setText("Min");
		min = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		min.setLayoutData(rangeSLayoutData);

		Label l2 = new Label(rangeSettings, SWT.NONE);
		l2.setText("lo");
		lo = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		lo.setLayoutData(rangeSLayoutData);

		Label l3 = new Label(rangeSettings, SWT.NONE);
		l3.setText("hi");
		hi = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		hi.setLayoutData(rangeSLayoutData);
		Label l4 = new Label(rangeSettings, SWT.NONE);
		l4.setText("Max");
		max = new Text(rangeSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		max.setLayoutData(rangeSLayoutData);

		setDataRangeButton = new Button(rangeSettings, SWT.PUSH);
		setDataRangeButton.setText("Update");
		setDataRangeButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));

		GridData dataText = new GridData(SWT.FILL, SWT.DEFAULT, false, false);
		dataText.widthHint = 40;
		/*
		 * min.setLayoutData(dataText); max.setLayoutData(dataText);
		 * lo.setLayoutData(dataText); hi.setLayoutData(dataText);
		 */
		
		GridData sizeButtonData = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
		
		Label lx = new Label(positionSettings, SWT.NONE);
		lx.setText("x:");
		x = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		xPlus = new Button(positionSettings, SWT.PUSH);
		xPlus.setText("+");
		xPlus.addListener(SWT.Selection, new PositionButtonListener(true, true));
		xMinus = new Button(positionSettings, SWT.PUSH);
		xMinus.setText("-");
		xMinus.addListener(SWT.Selection, new PositionButtonListener(true, false));
		xPlus.setLayoutData(sizeButtonData);
		xMinus.setLayoutData(sizeButtonData);
		
		
		Label ly = new Label(positionSettings, SWT.NONE);
		ly.setText("y:");
		y = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		yPlus = new Button(positionSettings, SWT.PUSH);
		yPlus.setText("+");
		yPlus.addListener(SWT.Selection, new PositionButtonListener(false, true));
		yMinus = new Button(positionSettings, SWT.PUSH);
		yMinus.setText("-");
		yMinus.addListener(SWT.Selection, new PositionButtonListener(false, false));
		yPlus.setLayoutData(sizeButtonData);
		yMinus.setLayoutData(sizeButtonData);
		
		Label lw = new Label(positionSettings, SWT.NONE);
		lw.setText("w:");
		w = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		widthPlus = new Button(positionSettings, SWT.PUSH);
		widthPlus.setText("+");
		widthPlus.addListener(SWT.Selection, new SizeButtonListener(true, true));
		widthMinus = new Button(positionSettings, SWT.PUSH);
		widthMinus.setText("-");
		widthMinus.addListener(SWT.Selection, new SizeButtonListener(true, false));
		widthPlus.setLayoutData(sizeButtonData);
		widthMinus.setLayoutData(sizeButtonData);
		
		Label lh = new Label(positionSettings, SWT.NONE);
		lh.setText("h:");
		h = new Text(positionSettings, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		heightPlus = new Button(positionSettings, SWT.PUSH);
		heightPlus.setText("+");
		heightPlus.addListener(SWT.Selection, new SizeButtonListener(false, true));
		
		heightMinus = new Button(positionSettings, SWT.PUSH);
		heightMinus.setText("-");
		heightMinus.addListener(SWT.Selection, new SizeButtonListener(false, false));
		heightPlus.setLayoutData(sizeButtonData);
		heightMinus.setLayoutData(sizeButtonData);
		
		x.addVerifyListener(new IntegerListener());
		x.setLayoutData(dataText);
		y.addVerifyListener(new IntegerListener());
		y.setLayoutData(dataText);
		w.addVerifyListener(new IntegerListener());
		w.setLayoutData(dataText);
		h.addVerifyListener(new IntegerListener());
		h.setLayoutData(dataText);

		initSizeButtons();

		browseImageButton = new Button(miscSettings, SWT.PUSH);
		browseImageButton.setText("Browse Image");
		browseImageButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		textFieldLabel = new Label(miscSettings, SWT.None);
		textFieldLabel.setText("Tooltip/Text: ");
		textFieldLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		textField = new Text(miscSettings, SWT.BORDER | SWT.MULTI);

		// textgd.w
		textField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		GC gc = new GC(textField);
		FontMetrics fm = gc.getFontMetrics();

		int height = fm.getHeight();
		gc.dispose();
		textField.setSize(miscSettings.getSize().x - 4, height);

		removeButton = new Button(miscSettings, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));

		this.layout(true);

		refreshCombos();
	}

	protected void initCombos() {

		Label link_label = new Label(basicSettings, SWT.None);
		link_label.setText("Widget:");
		links_combo = new Combo(basicSettings, SWT.DROP_DOWN | SWT.HORIZONTAL);

		Label signal_label = new Label(basicSettings, SWT.None);
		signal_label.setText("Signal:");
		signals_combo = new Combo(basicSettings, SWT.DROP_DOWN);

		Label type_label = new Label(basicSettings, SWT.None);
		type_label.setText("Type:");
		types_combo = new Combo(basicSettings, SWT.DROP_DOWN);

		types_combo.setItems(indicator_types);

		GridData dataCombo = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
		dataCombo.widthHint = 100;

		// dashboards_combo.setLayoutData(dataCombo);
		links_combo.setLayoutData(dataCombo);
		signals_combo.setLayoutData(dataCombo);
		types_combo.setLayoutData(dataCombo);

		// dashboards_combo.setText("-Dashboard-");
		links_combo.setText("---Widget---");
		signals_combo.setText("---Signal---");
		types_combo.setText("---Type---");
	}

	protected void initListeners() {
		
		/*.addListener(SWT.KeyDown, new Listener() {

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
		});*/
		
		Listener enterPositionListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (e.keyCode == SWT.CR) {
					updateWidgetPosition();
				}
			}
		};

		x.addListener(SWT.KeyDown, enterPositionListener);
		y.addListener(SWT.KeyDown, enterPositionListener);
		h.addListener(SWT.KeyDown, enterPositionListener);
		w.addListener(SWT.KeyDown, enterPositionListener);

		valueFormatText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (current_link != null)
					current_link.setValueLabelFormat(valueFormatText.getText());
			}
		});

		digitListener = new DoubleDigitListener();
		rangeKeyListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_RETURN) {
					if (current_link != null)
						updateLinkFigureRange(current_link);
				}
			}
		};

		min.addVerifyListener(digitListener);
		lo.addVerifyListener(digitListener);
		hi.addVerifyListener(digitListener);
		max.addVerifyListener(digitListener);

		min.addListener(SWT.Traverse, rangeKeyListener);
		lo.addListener(SWT.Traverse, rangeKeyListener);
		hi.addListener(SWT.Traverse, rangeKeyListener);
		max.addListener(SWT.Traverse, rangeKeyListener);

		links_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				linkIndex = links_combo.getSelectionIndex();

				if (linkIndex != -1) {
					focusOn(linkIndex);
					checkLogarithmicScale.setSelection(dashboard.getWidgetLinks().get(linkIndex).isLogarithmic());
				} else {
					current_link = null;
				}
			}
		});

		signals_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				signalIndex = signals_combo.getSelectionIndex();

				if (signalIndex != -1) {
					updateLinks();
				}
			}
		});

		types_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (types_combo.getSelectionIndex() != -1) {
					updateLinks();
				}
			}
		});

		setButtonListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				updateLinkFigureRange(current_link);
			}
		};

		setDataRangeButton.addListener(SWT.Selection, setButtonListener);
		removeButtonListener = new Listener() {

			int li; // widget link index

			@Override
			public void handleEvent(Event event) {
				li = links_combo.getSelectionIndex();
				if (li != -1 && current_link != null) {
					removeCurrentLink();
					if (getParent() instanceof Shell) {
						((Shell) getParent()).close();
					}
					SetupUI.deFocusWidget();
				}
			}
		};

		removeButton.addListener(SWT.Selection, removeButtonListener);

		checkLogarithmicScale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button btn = (Button) event.getSource();
				if (links_combo.getSelectionIndex() >= 0) {
					current_link.setLogarithmic(btn.getSelection());
					current_link.getCanvas().layout(true);
				}
			}
		});

		browseImageButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				callImageDialog();
			}
		});

		textField.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (current_link != null)
					current_link.setText(textField.getText());
			}
		});
	}

	private void precheckDoubleRangesFormat() {
		if (min.getText().equals("-"))
			min.setText("-100");
		if (max.getText().equals("-"))
			max.setText("0");
		if (lo.getText().equals("-"))
			lo.setText("-75");
		if (hi.getText().equals("-"))
			hi.setText("-25");
	}

	private void updateLinkFigureRange(AbstractWidgetExchangeLink link) {
		System.out.println("WS: updateLinkFigureRange " + link);
		int li = links_combo.getSelectionIndex();

		precheckDoubleRangesFormat();

		if (li != -1 && current_link != null) {

			if (min.getText().length() > 0 && max.getText().length() > 0) {
				current_link.setFigureRange(Double.parseDouble(min.getText()), Double.parseDouble(max.getText()));
			}

			if (lo.getText().length() > 0) {
				current_link.setFigureLo(Double.parseDouble(lo.getText()));
				current_link.updateRepresentation(true, false, false);
			}

			if (hi.getText().length() > 0) {
				current_link.setFigureHi(Double.parseDouble(hi.getText()));
				current_link.updateRepresentation(true, false, false);
			}
		}
	}

	public void updateLinks() {
		System.out.println("WS: updateLinks ");
		int li = links_combo.getSelectionIndex();
		int si = signals_combo.getSelectionIndex();
		int ti = types_combo.getSelectionIndex();

		if (li != -1) {

			precheckDoubleRangesFormat();

			if (min.getText().length() > 0 && max.getText().length() > 0) {
				current_link.setFigureRange(Double.parseDouble(min.getText()), Double.parseDouble(max.getText()));
			}

			if (lo.getText().length() > 0) {
				current_link.setFigureLo(Double.parseDouble(lo.getText()));
				current_link.updateRepresentation(true, false, false);
			}

			if (hi.getText().length() > 0) {
				current_link.setFigureHi(Double.parseDouble(hi.getText()));
				current_link.updateRepresentation(true, false, false);
			}

			if (si != -1) {

				if (signals.get(si).getId() != current_link.getSignalUID()) {
					if (current_link.getWidgetType() == WidgetType.W_INDICATOR) {
						SetupUnit.connectWidget(signals.get(si), (IndicatorWidgetLink) current_link);
					} else { // case controller link - use only out_signals
						current_link.getRepresentation().setSignalUID(out_signals.get(si).getId());
						current_link.applyRepresentation(true, false, false);
					}
				}
			}

			if (ti != -1) {
				dashboard.setFigureType(li, ti);
				current_link.updateRepresentation(true, false, true);
				enableTextOrImage();
			}

			if (x.getText().length() > 0 && y.getText().length() > 0) {
				current_link.setXY(Integer.parseInt(x.getText()), Integer.parseInt(y.getText()),
						Integer.parseInt(w.getText()), Integer.parseInt(h.getText()));
			}
			current_link.applyRepresentation();
			current_link.refresh();
		}

		links_combo.select(dashboard.getWidgetLinks().indexOf(current_link));
	}

	public void updateWidgetPosition() {
		if (current_link != null) {
			if (x.getText().length() > 0) {
				current_link.getRepresentation().setX(Integer.parseInt(x.getText()));
			}
			if (y.getText().length() > 0) {
				current_link.getRepresentation().setY(Integer.parseInt(y.getText()));
			}
			if (h.getText().length() > 0) {
				current_link.getRepresentation().setWidth(Integer.parseInt(w.getText()));
			}
			if (w.getText().length() > 0) {
				current_link.getRepresentation().setHeight(Integer.parseInt(h.getText()));
			}
			current_link.applyRepresentation(false, false, true);
		}

	}

	public void clear() {
		links_combo.clearSelection();
	}

	public void setPosition(int xpos, int ypos) {
		x.setText(Integer.toString(xpos));
		y.setText(Integer.toString(ypos));
	}

	public void refresh() {
		System.out.println("WS: refresh ");
		refreshCombos();
		setWidgetTexts(current_link);
		this.layout(true);
	}

	public void focusOnLink(AbstractWidgetExchangeLink link) {

		System.out.println("WS: FocusOnLink " + link);
		current_link = link;

		if (current_link.getDashboard() != dashboard) {
			dashboard = current_link.getDashboard();
		}
		dashboard.setCurrentLink(current_link);

		refreshCombos();
		removeButton.setEnabled(true);

		setWidgetTexts(link);
	}

	public void focusOn(int link_index) {
		System.out.println("WS: FocusOn " + link_index);
		current_link = dashboard.getWidgetLinks().get(link_index);
		links_combo.select(link_index);
		focusOnLink(current_link);
	}

	private void updateTypesCombo(AbstractWidgetExchangeLink link) {
		System.out.println("WS: updateTypesCombo " + link);
		types_combo.clearSelection();
		types_combo.removeAll();

		switch (link.getWidgetType()) {
		case W_ABSTRACT:
			break;
		case W_CONTROLLER:
			types_combo.setItems(controller_types);
			break;
		case W_INDICATOR:
			types_combo.setItems(indicator_types);
			break;
		default:
			break;
		}

		types_combo.select(link.getFigureType(link.getFigure()));
	}

	private void updateSignalsCombo(AbstractWidgetExchangeLink link) {
		System.out.println("WS: updateSignalsCombo " + link);
		signals_combo.clearSelection();
		signals_combo.removeAll();

		refreshSignalsLinks();

		int selection = -1;
		Signal s;

		switch (link.getWidgetType()) {
		case W_ABSTRACT:
			break;
		case W_CONTROLLER:
			for (int i = 0; i < out_signals.size(); i++) {
				s = out_signals.get(i);
				signals_combo.add(s.getType().toString().charAt(0) + " " + s.getSimpleName());
				if (link.getSignalUID() == s.getId()) {
					selection = i;
				}
			}

			break;
		case W_INDICATOR:
			for (int i = 0; i < signals.size(); i++) {
				s = signals.get(i);
				signals_combo.add(s.getType().toString().charAt(0) + " " + s.getSimpleName());
				if (link.getSignalUID() == s.getId()) {
					selection = i;
				}
			}

			break;
		default:
			break;
		}

		if (selection > -1) {
			signals_combo.select(selection);
		} else {
			signals_combo.setText("---Signal---");
		}
		layout(true);
	}

	public void refreshCombos() {
		System.out.println("WS: refreshCombos, current link = " + current_link);
		int li = -1;

		links_combo.removeAll();
		links_combo.setText("--Widget--");

		if (dashboard != null && !dashboard.isDisposed()) {
			
			current_link = dashboard.getCurrent_link();
			
			AbstractWidgetExchangeLink link;
			ArrayList<AbstractWidgetExchangeLink> links = dashboard.getWidgetLinks();
			for (int i = 0; i < links.size(); i++) {
				link = links.get(i);
				links_combo.add(i + " " + link.getDescription());
				if (current_link != null && current_link.equals(link)) {
					li = i;
				}
			}
			if (li > -1) {
				links_combo.select(li);
			} else {
				removeButton.setEnabled(false);
			}
		}

		if (current_link != null) {
			updateSignalsCombo(current_link);
			updateTypesCombo(current_link);
			enableTextOrImage();
		}
		
		basicSettings.layout(true);
	}

	public void setWidgetsSelectable(boolean selectable) {
		links_combo.setEnabled(selectable);
	}

	public void setWidgetTexts(AbstractWidgetExchangeLink link) {
		if (link == null)
			return;
		try {
			min.setText(Double.toString(link.getFigure().getMinimum()));
			max.setText(Double.toString(link.getFigure().getMaximum()));
			lo.setText(Double.toString(link.getFigure().getLoLevel()));
			hi.setText(Double.toString(link.getFigure().getHiLevel()));
			x.setText(Integer.toString(link.getCanvas().getBounds().x));
			y.setText(Integer.toString(link.getCanvas().getBounds().y));
			w.setText(Integer.toString(link.getCanvas().getBounds().width));
			h.setText(Integer.toString(link.getCanvas().getBounds().height));
			textField.setText(link.getRepresentation().getText());
			checkLogarithmicScale.setSelection(link.getFigure().isLogScale());
			valueFormatText.setText(link.getFigure().getValueLabelFormat());

		} catch (Exception e) {
			System.err.println("WidgetSettingsUI error: " + e.getMessage());
		}
	}

	public void removeCurrentLink() {
		current_link.delete();
		current_link = null;
		System.out.println("Widget Settings: removed current link : " + current_link);
	}

	public void resetCurrentLink() {
		System.out.println("WS: resetCurrentLink ");
		ArrayList<AbstractWidgetExchangeLink> links = dashboard.getWidgetLinks();
		System.out.println("WS: resetting current link. Size: " + links.size());
		if (links.size() > 0) {
			current_link = links.get(0);
			focusOnLink(current_link);
		} else {
			current_link = null;
			links_combo.clearSelection();
			refreshCombos();
		}
		System.out.println("WS: current link: " + current_link);
	}

	public void deFocus() {
		current_link = null;
		signals_combo.clearSelection();
		signals_combo.setText("---Signal---");
		types_combo.clearSelection();
		types_combo.setText("---Type---");
		links_combo.clearSelection();
		links_combo.removeAll();
		links_combo.setText("---Widget---");
		System.out.println("WS: defocused, current link = " + current_link);
	}

	public void setEnabled(boolean enabled) {

		links_combo.setEnabled(enabled);
		types_combo.setEnabled(enabled);
		signals_combo.setEnabled(enabled);
		widthMinus.setEnabled(enabled);
		widthPlus.setEnabled(enabled);
		heightMinus.setEnabled(enabled);
		heightPlus.setEnabled(enabled);
		removeButton.setEnabled(enabled);
		checkLogarithmicScale.setEnabled(enabled);
		setDataRangeButton.setEnabled(enabled);
		if (enabled) {
			enableTextOrImage();
		} else {
			textField.setEnabled(false);
			browseImageButton.setEnabled(false);
		}

	}

	private void enableTextOrImage() {
		System.out.println("WS: enableTextOrImage ");
		if (current_link != null && current_link.getType() == Constants.WIDGET_IMAGE) {
			textField.setEnabled(false);
			browseImageButton.setEnabled(true);
		} else if (current_link != null) {
			textField.setEnabled(true);
			browseImageButton.setEnabled(false);
		} else {
			textField.setEnabled(false);
			browseImageButton.setEnabled(false);
		}
	}

	private void callImageDialog() {
		/*
		 * Shell shell = new Shell(Display.getCurrent()); FileDialog dialog =
		 * new FileDialog(shell, SWT.OPEN); dialog.setFilterExtensions(new
		 * String[] { "*.*", "*.gif", "*.png", "*.jpeg", "*.bmp", "*.jpg"});
		 * 
		 * dialog.setFilterPath("c:\\");
		 */
		String result = current_link.getDashboard().callImageDialog(Dashboard.SELECT_IMAGE_WIDGET);

		if (result != null) {

			// force image dimension when loading it for the first time
			ImageData imgData = new ImageData(result);
			current_link.getRepresentation().setHeight(imgData.height);
			current_link.getRepresentation().setWidth(imgData.width);

			textField.setText(result);
			current_link.setText(result);
		}
	}

}
