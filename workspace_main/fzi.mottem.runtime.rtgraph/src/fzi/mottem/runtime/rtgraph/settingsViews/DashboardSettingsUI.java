package fzi.mottem.runtime.rtgraph.settingsViews;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;
import fzi.mottem.runtime.rtgraph.listeners.IntegerListener;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;

public class DashboardSettingsUI extends Composite {

	Label dashboards_label;
	Combo dashboards_combo;

	protected ArrayList<DashboardComposite> dashboards;
	private DashboardComposite dashboard;

	Group background_group;
	Label stretch_bg_x_label;
	Button stretch_bg_x_btn;
	Label stretch_bg_y_label;
	Button stretch_bg_y_btn;
	Button browse_bg;

	Label polling_label;
	Text polling_text;

	Label name_label;
	Text name_text;

	Button add_consumer_button;
	Button add_controller_button;
	private Button set_button;

	public DashboardSettingsUI(Composite parent, int style) {
		super(parent, style);
		// initContainerLayout();
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent e) {
				refresh(false);
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void initContainerLayout() {
		setLayout(new GridLayout(2, false));
		
		
		dashboards = new ArrayList<DashboardComposite>();

		dashboards_label = new Label(this, SWT.None);
		dashboards_label.setText("Dashboard");
		dashboards_combo = new Combo(this, SWT.DROP_DOWN);
		dashboards_combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		polling_label = new Label(this, SWT.None);
		polling_label.setText("Poll interval (ms)");
		polling_text = new Text(this, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		polling_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		name_label = new Label(this, SWT.None);
		name_label.setText("Dashboard name:");
		name_text = new Text(this, SWT.CENTER | SWT.SINGLE | SWT.BORDER);
		name_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		set_button = new Button(this, SWT.PUSH);
		set_button.setText("Update");
		set_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		background_group = new Group(this, SWT.None);
		background_group.setLayout(new GridLayout(2, false));
		background_group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		background_group.setText("Background");

		Composite c1 = new Composite(background_group, SWT.None);
		c1.setLayout(new GridLayout(2, false));
		c1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		stretch_bg_x_label = new Label(c1, SWT.None);
		stretch_bg_x_label.setText("Stretch x: ");
		stretch_bg_x_btn = new Button(c1, SWT.CHECK);

		Composite c2 = new Composite(background_group, SWT.None);
		c2.setLayout(new GridLayout(2, false));
		c2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		stretch_bg_y_label = new Label(c2, SWT.None);
		stretch_bg_y_label.setText("Stretch y: ");
		stretch_bg_y_btn = new Button(c2, SWT.CHECK);

		browse_bg = new Button(background_group, SWT.PUSH);
		browse_bg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		browse_bg.setText("Browse...");

		Composite c3 = new Composite(this, SWT.None);
		c3.setLayout(new GridLayout(2, false));
		c3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		add_consumer_button = new Button(c3, SWT.PUSH);
		add_consumer_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		add_consumer_button.setText("Add Indicator");

		add_controller_button = new Button(c3, SWT.PUSH);
		add_controller_button.setText("Add Controller");
		add_controller_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		addListeners();
	}

	private void addListeners() {

		add_consumer_button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				WidgetRepresentation newFigureRep = new WidgetRepresentation();
				if (dashboard != null && !dashboard.isDisposed()) {
					dashboard.addIndicatorWidget(newFigureRep);
				}
				refresh(false);
			}
		});

		add_controller_button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				WidgetRepresentation newFigureRep = new WidgetRepresentation();
				if (dashboard != null && !dashboard.isDisposed()) {
					dashboard.addControllerWidget(newFigureRep);
				}
				refresh(false);
			}
		});

		stretch_bg_y_btn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				System.out.println("Y event");
				if (dashboard != null && !dashboard.isDisposed()) {
					System.out.println("Y stretch: " + stretch_bg_y_btn.getSelection());
					dashboard.setStretch_bg_y(stretch_bg_y_btn.getSelection());
					dashboard.refreshBgImage();
				}
			}
		});

		stretch_bg_x_btn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (dashboard != null && !dashboard.isDisposed()) {
					if (dashboard != null && !dashboard.isDisposed()) {
						dashboard.setStretch_bg_x(stretch_bg_x_btn.getSelection());
						dashboard.refreshBgImage();
					}
				}
			}
		});
		
		browse_bg.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(dashboard != null && !dashboard.isDisposed()) {
					dashboard.callBackgroundDialog();
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		dashboards_combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				int index = dashboards_combo.getSelectionIndex();

				if (index > -1 && index < dashboards.size()) {
					dashboard = dashboards.get(index);

					stretch_bg_x_btn.setSelection(dashboard.isStretch_bg_x());
					stretch_bg_y_btn.setSelection(dashboard.isStretch_bg_y());
					polling_text.setText("" + dashboard.getRepresentation().widget_polling_delay);
					name_text.setText("" + dashboard.getName());

					/*
					 * links_combo.removeAll(); for (int i = 0; i <
					 * dashboard.getWidgetLinks().size(); i++) {
					 * links_combo.add( i + " " +
					 * dashboard.getWidgetLinks().get(i).getFigure().getClass().
					 * getSimpleName()); if (current_link ==
					 * dashboard.getWidgetLinks().get(i)) {
					 * links_combo.select(i); } }
					 */
				} else {
					dashboard = null; // ?
				}
			}
		});

	

		polling_text.addVerifyListener(new IntegerListener());

		set_button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dashboard != null && !dashboard.isDisposed()) {
					if(name_text.getText().length() > 0) {
						if (name_text.getText() != dashboard.getName()) {
							dashboard.setName(name_text.getText());
						}
					}
					
					if (polling_text.getText().length() > 0) {
						int delay = Integer.parseInt(polling_text.getText());
						if (delay != dashboard.getWidget_pollingDelay()) {
							dashboard.setWidgetPollingDelay(delay);
						}
					}

					refresh(true);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void refresh(boolean autoselect) {
		dashboards = ViewCoordinator.getDashboardEditors();
		dashboards_combo.removeAll();
		for (DashboardComposite d : dashboards) {
			dashboards_combo.add(d.getName());
		}
		if(autoselect) {
			if (dashboard != null && !dashboard.isDisposed()) {
				dashboards_combo.select(dashboards.indexOf(dashboard));
			} else {
				dashboards_combo.setText("-Dashboard-");
			}
		}
	}

	public void setDashboard(DashboardComposite dashboard) {
		refresh(false);
		if(dashboards.contains(dashboard) && !dashboard.isDisposed()) {
			this.dashboard = dashboard;
			dashboards_combo.select(dashboards.indexOf(dashboard));
			stretch_bg_x_btn.setSelection(dashboard.isStretch_bg_x());
			stretch_bg_y_btn.setSelection(dashboard.isStretch_bg_y());
			polling_text.setText("" + dashboard.getRepresentation().widget_polling_delay);
			name_text.setText("" + dashboard.getName());
		}
	}
}
