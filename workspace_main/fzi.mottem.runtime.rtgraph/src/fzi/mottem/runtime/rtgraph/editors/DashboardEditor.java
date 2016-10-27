package fzi.mottem.runtime.rtgraph.editors;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.XML.DashboardRepresentation;
import fzi.mottem.runtime.rtgraph.XML.ProfileUtils;
import fzi.mottem.runtime.rtgraph.settingsViews.SetupUI;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;

public class DashboardEditor extends EditorPart {

	public static final String editorId = "fzi.mottem.runtime.rtgraph.editors.DashboardEditor";
	
	private FileEditorInput input;
	private boolean isActive = false;
	
	Menu dashboard_menu;

	private DashboardRepresentation representation;
	private DashboardComposite dashboard;
	private SashForm mainHolder;

	public DashboardComposite getDashboard() {
		return dashboard;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		try {
			subMonitor.beginTask("Save", 2);
			dashboard.updateRepresentation();
			subMonitor.worked(1); // call worked() for each (sub-) job
			ProfileUtils.saveDashboardXML(representation, input.getFile().getRawLocation().toOSString());
			subMonitor.worked(1); // call worked() for each (sub-) job
			dashboard.setDirty(false);
			
			setName(input.getName());		
			//this.firePropertyChange(PROP_DIRTY);
		} finally {
			subMonitor.done();
		}

	}

	@Override
	public void doSaveAs() {
		Shell shell = new Shell(Display.getCurrent());
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setText("Save Dashboard XML");
		fd.setFilterPath(input.getFile().getRawLocation().toOSString());
		String[] filterExt = { "*.*", "*" + Constants.EXTENSION_DASHBOARD };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();

		if (selected != null) {
			dashboard.updateRepresentation();
			dashboard.setDirty(false);
			
			String newName = selected.substring(selected.lastIndexOf(File.separator) + 1);
			ProfileUtils.saveDashboardXML(representation, selected, newName);
			setName(newName);
			/*
			if(dashboard.getRepresentation().getName() != this.getPartName()) {
				setPartName(dashboard.getRepresentation().getName());
			}*/
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		if (!(input instanceof FileEditorInput)) {
			throw new RuntimeException(
					"Wrong input, tooltip: " + input.getToolTipText() + " " + input.getClass().getSimpleName());
		}

		this.input = (FileEditorInput) input;
		setSite(site);
		setInput(input);
		representation = ProfileUtils.getDashboardModel(this.input.getFile().getRawLocation().toOSString());		
		
		setPartName(input.getName());
		
		IPartListener2 pl = new IPartListener2() {

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				if(partRef.getPartName() == getPartName()) {
					//System.out.println("Part is active, refreshing setup ui");
					isActive = true;
					//SetupUI.refresh();
					
					SetupUI.focusOnDashboard(dashboard);
				}			
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
	
			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				if(partRef.getPartName() == getPartName()) {
					//System.out.println("Part is closed, refreshing setup ui");
					SetupUI.refresh();
					SetupUI.deSelect();
				}			
			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				if(partRef.getPartName() == getPartName()) {
					isActive = false;
				}	
			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				if(partRef.getPartName() == getPartName()) {
					//System.out.println("Part is open, focusing on it");
					SetupUI.focusOnDashboard(dashboard);
				}
			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				
			}

			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
				
			}
		};
		getSite().getPage().removePartListener(pl);
		getSite().getPage().addPartListener(pl);
	}

	@Override
	public boolean isDirty() {
		return dashboard.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		mainHolder = new SashForm(parent, SWT.VERTICAL);
		mainHolder.setLayout(new GridLayout(1, true));
		dashboard = new DashboardComposite(mainHolder, SWT.None, representation);
		dashboard.setEditor(this);
		
		dashboard.setName(this.getPartName());

		Display display = this.getSite().getShell().getDisplay();

		Listener keyListener = new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (isActive) {
					if (e.keyCode == SWT.DEL) {
						if(dashboard.getCurrent_link() != null) {
							dashboard.getCurrent_link().delete(); 
							SetupUI.deFocusWidget();	
						}
					} 
				}
			}
		};
		
		display.addFilter(SWT.KeyDown, keyListener);
		dashboard.setDirty(false);
		if(SetupUI.isOpen()) { //only activate the settings view if it is 
			//already is created
			ViewCoordinator.showSettingsViewpart();
		}
		
	}
	
	private void setName(String name) {
		setPartName(name);
		dashboard.setName(name);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	public void fireChange() {
		firePropertyChange(PROP_DIRTY);
	}

}
