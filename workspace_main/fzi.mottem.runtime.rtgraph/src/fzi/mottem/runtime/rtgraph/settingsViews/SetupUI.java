package fzi.mottem.runtime.rtgraph.settingsViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink;
import fzi.mottem.runtime.rtgraph.SetupUnit;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;

public class SetupUI {
	static Composite shell;

	static Composite buttonRow;
	static Button refreshButton;
	Button testButton;

	static Button saveProfileButton;

	static Button addGraphViewButton;
	static Button loadProfileButton;
	static Text graphName;

	private static Group widgetsGroup;
	private static WidgetSettingsUI widgetSettings;

	private static Group dbgroup;

	private static DashboardSettingsUI dashboardSettings;

	private static SignalsSettingsUI signalSettings;

	private static Group ssgroup;

	private static boolean isOpen;
	
	

	private SetupUI() {
	}

	private static void initShell() {
		shell.setLayout(new GridLayout(3, false));
		shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		initSignalsContainer();
		initWidgetSettingsContainer();
		initDashboardSettingsContainer();
	}

	private static void initSignalsContainer() {
		GridData d = new GridData(SWT.FILL, SWT.DEFAULT, true, true);

		ssgroup = new Group(shell, SWT.NONE);
		ssgroup.setText("Signals");
		ssgroup.setLayout(new GridLayout(1, false));
		ssgroup.setLayoutData(d);

		if (signalSettings == null || signalSettings.isDisposed()) {
			signalSettings = new SignalsSettingsUI(ssgroup, SWT.None);
			signalSettings.initContainerLayout();
			signalSettings.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, true));
		} else {
			signalSettings.setParent(widgetsGroup);
		}

		ssgroup.layout(true);
	}

	private static void initDashboardSettingsContainer() {
		GridData d = new GridData(SWT.FILL, SWT.DEFAULT, true, true, 1, 1);

		dbgroup = new Group(shell, SWT.NONE);
		dbgroup.setText("Dashboard settings");
		dbgroup.setLayout(new GridLayout(1, false));
		dbgroup.setLayoutData(d);

		if (dashboardSettings == null || dashboardSettings.isDisposed()) {
			dashboardSettings = new DashboardSettingsUI(dbgroup, SWT.None);
			dashboardSettings.initContainerLayout();
			dashboardSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		} else {
			dashboardSettings.setParent(widgetsGroup);
		}

		dbgroup.layout(true);
	}

	private static void initWidgetSettingsContainer() {
		widgetsGroup = new Group(shell, SWT.SHADOW_OUT);
		widgetsGroup.setText("Widgets");
		widgetsGroup.setLayout(new GridLayout(1, false));
		widgetsGroup.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));

		if (widgetSettings == null || widgetSettings.isDisposed()) {
			widgetSettings = new WidgetSettingsUI(widgetsGroup, SWT.NONE);
			widgetSettings.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, true));
		} else {
			widgetSettings.setParent(widgetsGroup);
		}

		widgetsGroup.layout(true);
	}

	private static void gatherSignals() {
		SetupUnit.refreshSignals();
	}

	public static void makeUI(Composite parent) {
		shell = parent;
		initShell();
		refresh();
	}

	public static void deSelect() {
		if (isOpen()) {
			gatherSignals();
			dashboardSettings.refresh(true);
			widgetSettings.deFocus();
			widgetSettings.setEnabled(false);
		}
	}

	public static void refresh() {
		if (isOpen()) {
			gatherSignals();
			signalSettings.refresh();
			widgetSettings.refresh();
			dashboardSettings.refresh(true);
		}
	}

	public static void refreshSignals() {
		if (isOpen()) {
			gatherSignals();
			//showSignals();
			signalSettings.refresh();
			if (widgetSettings != null)
				widgetSettings.refresh();
			SetupUnit.autoConnectWidgets();
			SetupUnit.autoConnectGraphViews();
		}
	}

	private static WidgetSettingsUI getWidgetSettings() throws RuntimeException {

		if (widgetSettings != null && !widgetSettings.isDisposed()) {
			return widgetSettings;
		} else {
			throw new RuntimeException();
		}

	}

	public static void focusOnDashboard(DashboardComposite dashboard) {
		if (isOpen()) {
			if(widgetSettings != null) {
				widgetSettings.setEnabled(true);
				widgetSettings.setAndFocusDashboard(dashboard);
				dashboardSettings.setDashboard(dashboard);
			}	
		}
	}

	public static void setOpen(boolean b) {
		isOpen = b;
	}

	public static boolean isOpen() {
		boolean open = ViewCoordinator.checkSettingsViewpart();
		return open;
	}

	public static void resetCurrentLink() {
		if (isOpen()) {
			getWidgetSettings().resetCurrentLink();
		}
	}

	public static void focusOnDashboard(DashboardComposite dashboard, int x, int y) {
		if (isOpen()) {
			focusOnDashboard(dashboard);
			widgetSettings.setPosition(x, y);
		}
	}

	public static void focusOnLink(AbstractWidgetExchangeLink link) {
		if (isOpen()) {
			widgetSettings.focusOnLink(link);
			widgetSettings.setEnabled(true);
		}
	}

	public static void deFocusWidget() {
		if(isOpen()) {
			widgetSettings.deFocus();
			widgetSettings.refreshCombos();
		}
	}
}
