package property.sheet;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.testrigmodel.IOne;
import fzi.mottem.model.testrigmodel.InspectorContainer;
import fzi.util.eclipse.IntegrationUtils;


/**
 * The Class IOnePropertySheet will be called if the connected business object is a 
 * IOne or IOnePort. This checks the Class IOneFilter.
 * The property sheet has text fields for name, runtime inspector class and workspace path.
 * There is also a button to change the workspace path, which is a file with the file extension .xjrf.
 */
public class IOnePropertySheet extends GFPropertySection implements ITabbedPropertyConstants {

	/** The inspector container. */
	private IOne inspectorContainer;
	
	/** The name widget. */
	private Text nameWidget;
	
	/** The runtime inspector class widget. */
	private Text runtimeInspectorClassWidget;

	/** The workspace path widget. */
	private Text workspacePathWidget;
	
	/** The change workspace path button. */
	private Button changeWorkspacePathButton;



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));
		
		getWidgetFactory().createLabel(parent, "Name: ");
		nameWidget = getWidgetFactory().createText(parent, "");
		nameWidget.addModifyListener(nameListener);
		nameWidget.setSize(500, 25);

		getWidgetFactory().createLabel(parent, "Runtime Inspector Class: ");
		runtimeInspectorClassWidget = getWidgetFactory().createText(parent, "");
		runtimeInspectorClassWidget.addModifyListener(runtimeInspectorClassListener);
		runtimeInspectorClassWidget.setSize(500, 25);

		getWidgetFactory().createLabel(parent, "WinIDEA Workspace File: ");
		workspacePathWidget = getWidgetFactory().createText(parent, "");
		workspacePathWidget.setEditable(true);
		workspacePathWidget.setSize(500, 25);
		workspacePathWidget.addModifyListener(workspacePathListener);

		getWidgetFactory().createLabel(parent, "");
		changeWorkspacePathButton = getWidgetFactory().createButton(parent, "Change Workspace File", SWT.PUSH);
		changeWorkspacePathButton.addSelectionListener(new SelectionListener()
			{
				
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
					dialog.setText("Choose WinIDEA Workspace File");
	
					dialog.setFilterPath(Platform.getLocation().toOSString());
					String [] extensions = {"*.xjrf", "*.*"};
					dialog.setFilterExtensions(extensions);
					
					String workspacePathStr = dialog.open();
					if (workspacePathStr == null)
						return;
					
					final IPath workspacePath = IntegrationUtils.getWorkspaceRelativePathForSystemPath(new Path(workspacePathStr));
					
					final PictogramElement pe = getSelectedPictogramElement();
					if (pe != null)
					{
						final EObject bo = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);

						if (bo == null)
						{
							return;
						}
						
						if(bo instanceof InspectorContainer)
						{
							inspectorContainer =  (IOne) bo;
						}
						else if(bo instanceof IInspector)
						{
							inspectorContainer =  (IOne) ((IInspector) bo).eContainer();
						}
						
			            TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
			            editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain)
				            {
				                @Override
				                protected void doExecute()
				                {
				                	inspectorContainer.setWorkspacePath(workspacePath.toString());
				                	refresh();
				                }
				            });
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		
		refresh();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.tabbed.AbstractPropertySection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh() {
		nameWidget.removeModifyListener(nameListener);
		runtimeInspectorClassWidget.removeModifyListener(runtimeInspectorClassListener);
		workspacePathWidget.removeModifyListener(workspacePathListener);

		final PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			final EObject bo = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			// the filter assured, that it is a INamed
			if (bo == null)
				return;
			if(bo instanceof InspectorContainer){
				inspectorContainer =  (IOne) bo;
			}
			else if(bo instanceof IInspector){
				inspectorContainer =  (IOne) ((IInspector) bo).eContainer();
			}
			String nameValue = inspectorContainer.getName();
			nameWidget.setText(nameValue == null ? "" : nameValue);
			nameWidget.setSize(500, 25);
			String runtimeInspector = "";
			if( inspectorContainer.getPort() != null ){
				if( inspectorContainer.getPort().getRuntimeInspectorClass() != null ){
					runtimeInspector = inspectorContainer.getPort().getRuntimeInspectorClass();
				}
			}
			runtimeInspectorClassWidget.setText(runtimeInspector == null ? "" : runtimeInspector);
			runtimeInspectorClassWidget.setSize(500, 25);
			
			String workspacePath = inspectorContainer.getWorkspacePath();
			workspacePathWidget.setText(workspacePath == null ? "" : workspacePath);
			workspacePathWidget.setSize(500, 25);

		}

		nameWidget.addModifyListener(nameListener);
		runtimeInspectorClassWidget.addModifyListener(runtimeInspectorClassListener);
		workspacePathWidget.addModifyListener(workspacePathListener);

	}

    /** The name listener. */
    private ModifyListener nameListener = new ModifyListener() {
        public void modifyText(ModifyEvent arg0) {
            TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
            editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
                @Override
                protected void doExecute() {
                    changeNamePropertyValue();
                }
            });
        }
    };

    /** The runtime inspector class listener. */
    private ModifyListener runtimeInspectorClassListener = new ModifyListener() {
    	public void modifyText(ModifyEvent arg0) {
    		TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
    		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
    			@Override
    			protected void doExecute() {
    				changeRuntimeInspectorClassPropertyValue();
    			}
    		});
    	}
    };
    
    /** The workspace path listener. */
    private ModifyListener workspacePathListener = new ModifyListener() {
    	public void modifyText(ModifyEvent arg0) {
    		TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
    		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
    			@Override
    			protected void doExecute() {
    				changeWorkspacePathPropertyValue();
    			}
    		});
    	}
    };

    
    /**
     * Change name property value.
     */
    private void changeNamePropertyValue() {
    	if(nameWidget.getText()== ""){
    		return;
    	}
    	String newValue = nameWidget.getText();
    	if (!newValue.equals(inspectorContainer.getName())) {
    		inspectorContainer.setName(newValue);
    		
			// do something to refresh/call the method getToolTip of EditorToolBehaviorProvider
			PictogramElement pe = getSelectedPictogramElement();
			pe.setActive(false);
			pe.setActive(true);

    	}
    }
    
    /**
     * Change runtime inspector class property value.
     */
    private void changeRuntimeInspectorClassPropertyValue() {
    	if(runtimeInspectorClassWidget.getText()== ""){
    		return;
    	}
    	String newValue = runtimeInspectorClassWidget.getText();
    	if (!newValue.equals(inspectorContainer.getPort().getRuntimeInspectorClass())) {
    		inspectorContainer.getPort().setRuntimeInspectorClass(newValue);
    	}
    }
    
    /**
     * Change workspace path property value.
     */
    private void changeWorkspacePathPropertyValue() {
    	if(workspacePathWidget.getText()== ""){
    		return;
    	}
    	String newValue = workspacePathWidget.getText();
    	if (!newValue.equals(inspectorContainer.getWorkspacePath())) {
    		inspectorContainer.setWorkspacePath(newValue);
    	}
    }


}
