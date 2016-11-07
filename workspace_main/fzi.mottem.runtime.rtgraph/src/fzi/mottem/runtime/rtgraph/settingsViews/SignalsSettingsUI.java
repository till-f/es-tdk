package fzi.mottem.runtime.rtgraph.settingsViews;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.dataexchanger.Signal.SignalType;
//import fzi.mottem.runtime.rtgraph.MenuIt;
import fzi.mottem.runtime.rtgraph.SetupUnit;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.editors.DashboardEditor;
import fzi.mottem.runtime.rtgraph.editors.GraphViewEditor;

public class SignalsSettingsUI extends Composite {
	
	ArrayList<Signal> in_signals = new ArrayList<Signal>();
	ArrayList<Signal> out_signals = new ArrayList<Signal>();
	
	ArrayList<Signal> selected_in_signals = new ArrayList<Signal>();
	ArrayList<Signal> selected_out_signals = new ArrayList<Signal>();
	
	Group input_signals_group;
	List in_signals_list;

	Group output_signals_group;
	List out_signals_list;

	Button refresh_button;
	/*
	Button add_graph_button;
	Button add_dashboard_button;
	
	Label nameLabel;
	Text nameText;
	*/
	private Menu inputPopupMenu;
	
	ArrayList<String> dashboards = new ArrayList<String>();
	ArrayList<String> graphviews = new ArrayList<String>();
	private MenuItem addInputToDbItem;
	private Menu addInputDbIndicatorMenu;
	private Menu addInputGvMenu;
	private MenuItem addInputToGVItem;
	private Menu outputPopupMenu;
	private MenuItem addOutputToControllerItem;
	private MenuItem addOutputToGVItem;
	private Menu addOutputDbControllerMenu;
	private Menu addOutputGVMenu;
	private MenuItem addOutputToIndicatorItem;
	private Menu addOutputDbIndicatorMenu;
	private ArrayList<Signal> bi_signals;
	

	public SignalsSettingsUI(Composite parent, int style) {
		super(parent, style);	
	}

	public void initContainerLayout() {
		setLayout(new GridLayout(2, false));
		
		input_signals_group = new Group(this, SWT.None);
		input_signals_group.setLayout(new GridLayout(1, false));
		input_signals_group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		input_signals_group.setText("Readable");
		
		in_signals_list = new List(input_signals_group, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		GridData listdata = new GridData(GridData.FILL_BOTH);
		listdata.heightHint = 5 * in_signals_list.getItemHeight(); // height for 5													// rows
		listdata.widthHint = 120; // pixels
		
		in_signals_list.setLayoutData(listdata);
		in_signals_list.add("Test");
		
		output_signals_group = new Group(this, SWT.None);
		output_signals_group.setLayout(new GridLayout(1, false));
		output_signals_group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		output_signals_group.setText("Writeable");
		out_signals_list = new List(output_signals_group, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		out_signals_list.setLayoutData(listdata);	
		
		refresh_button = new Button(this, SWT.PUSH);
		refresh_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		refresh_button.setText("Refresh");
		
		//Add context menus for input signals list
		inputPopupMenu = new Menu(in_signals_list);
		addInputToDbItem = new MenuItem(inputPopupMenu, SWT.CASCADE);
		addInputToDbItem.setText("Add Indicators to Dashboard");
		addInputToDbItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/dashboard-icon.png").createImage());

		addInputToGVItem = new MenuItem(inputPopupMenu, SWT.CASCADE);
		addInputToGVItem.setText("Add Traces to GraphView");
		addInputToGVItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/AutoScale.png").createImage());
		
		addInputDbIndicatorMenu = new Menu(inputPopupMenu);
		addInputGvMenu = new Menu(inputPopupMenu);
		
		addInputToDbItem.setMenu(addInputDbIndicatorMenu);
		addInputToGVItem.setMenu(addInputGvMenu);
		
		//add context menus for output signals list
		outputPopupMenu = new Menu(out_signals_list);
		
		addOutputToControllerItem = new MenuItem(outputPopupMenu, SWT.CASCADE);
		addOutputToControllerItem.setText("Add Controllers to Dashboard");
		addOutputToControllerItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/dashboard-icon.png").createImage());
		
		addOutputToIndicatorItem = new MenuItem(outputPopupMenu, SWT.CASCADE);
		addOutputToIndicatorItem.setText("Add Indicators to Dashboard");
		addOutputToIndicatorItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/dashboard-icon.png").createImage());
		
		addOutputToGVItem = new MenuItem(outputPopupMenu, SWT.CASCADE);
		addOutputToGVItem.setText("Add Traces to GraphView");
		addOutputToGVItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/AutoScale.png").createImage());
		
		addOutputDbControllerMenu = new Menu(outputPopupMenu);
		addOutputDbIndicatorMenu = new Menu(outputPopupMenu);
		addOutputGVMenu = new Menu(outputPopupMenu);
		
		addOutputToControllerItem.setMenu(addOutputDbControllerMenu);
		addOutputToIndicatorItem.setMenu(addOutputDbIndicatorMenu);
		addOutputToGVItem.setMenu(addOutputGVMenu);
		
		in_signals_list.setMenu(inputPopupMenu);
		out_signals_list.setMenu(outputPopupMenu);
		
		addListeners();	
		
		refresh();
	}

	private void addListeners() {
		
		in_signals_list.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
								
				int[] selectedItems = in_signals_list.getSelectionIndices();
				
				
				if (selectedItems.length > 0) {
					refreshMenus();
					selected_in_signals.clear();
					for(int i = 0; i < selectedItems.length; i++) {
						selected_in_signals.add(in_signals.get(selectedItems[i]));
						
					}
				}
			}
		});
		
		out_signals_list.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
								
				int[] selectedItems = out_signals_list.getSelectionIndices();
				
				
				if (selectedItems.length > 0) {
					refreshMenus();
					selected_out_signals.clear();
					for(int i = 0; i < selectedItems.length; i++) {
						selected_out_signals.add(out_signals.get(selectedItems[i]));
						
					}
				}
			}
		});
		
		refresh_button.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				refresh();	
			}
		});
		
	}
	
	public void refresh() {
		in_signals = SetupUnit.getSignals(SignalType.HW_INPUT);
		out_signals = SetupUnit.getSignals(SignalType.HW_OUTPUT);
		
		bi_signals = SetupUnit.getSignals(SignalType.BIDIRECTIONAL);
		
		in_signals.addAll(bi_signals);
		out_signals.addAll(bi_signals);
		
		in_signals_list.removeAll();
		out_signals_list.removeAll();
		
		for(Signal s : in_signals) {
			in_signals_list.add(s.getSimpleName());
		}
		
		for(Signal s : out_signals) {
			out_signals_list.add(s.getSimpleName());
		}
		
		dashboards.clear();
		graphviews.clear();
		
		refreshMenus();
	}
	
	private void refreshMenus() {
		addInputDbIndicatorMenu = new Menu(inputPopupMenu);
		addOutputDbControllerMenu = new Menu(outputPopupMenu);
		addOutputDbIndicatorMenu = new Menu(outputPopupMenu);
			
		for(DashboardEditor ed : ViewCoordinator.getDashboardEditorParts()) {
			dashboards.add(ed.getPartName());
			MenuItem input_mi = new MenuItem(addInputDbIndicatorMenu, SWT.None);
			MenuItem output_mi = new MenuItem(addOutputDbControllerMenu, SWT.None);
			MenuItem output_to_indicator_mi = new MenuItem(addOutputDbIndicatorMenu, SWT.None);
			input_mi.setText(ed.getPartName());
			output_mi.setText(ed.getPartName());
			output_to_indicator_mi.setText(ed.getPartName());
			
			input_mi.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Adding Input Signals to Dashboard " + ed.getPartName());
					ed.getDashboard().addIndicators(selected_in_signals);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {	
				}
			});
			
			output_mi.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Adding Output Signals to Dashboard " + ed.getPartName());
					ed.getDashboard().addControllers(selected_out_signals);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {	
				}
			});
			
			output_to_indicator_mi.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Adding Output Signals to Dashboard " + ed.getPartName());
					ed.getDashboard().addIndicators(selected_out_signals);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {	
				}
			});
			
		}
		
		addInputToDbItem.setMenu(addInputDbIndicatorMenu);	
		addOutputToControllerItem.setMenu(addOutputDbControllerMenu);
		addOutputToIndicatorItem.setMenu(addOutputDbIndicatorMenu);
		
			
		addInputGvMenu = new Menu(inputPopupMenu);
		addOutputGVMenu = new Menu(outputPopupMenu);
		
		for(GraphViewEditor ed : ViewCoordinator.getGraphEditorParts()) {
			graphviews.add(ed.getPartName());
			MenuItem mi = new MenuItem(addInputGvMenu, SWT.None);
			MenuItem output_mi = new MenuItem(addOutputGVMenu, SWT.None);
		
			mi.setText(ed.getPartName());
			output_mi.setText(ed.getPartName());
			
			mi.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Adding Input Signals to GraphView " + ed.getPartName());
					ed.getView().addTraces(selected_in_signals);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			
			output_mi.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Adding Output Signals to GraphView " + ed.getPartName());
					ed.getView().addTraces(selected_out_signals);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		
		addInputToGVItem.setMenu(addInputGvMenu);
		addOutputToGVItem.setMenu(addOutputGVMenu);
		
	}
	
}
