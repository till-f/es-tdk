package property.sheet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.testrigmodel.HostInspectorContainer;
import fzi.mottem.model.testrigmodel.InspectorContainer;


/**
 * The Class HostInspectorPropertySheet will be called if the connected business object is a 
 * HostInspectorContainer or CDIInspectorPort. This checks the Class HostInspectorFiler.
 * The property sheet has text fields for name, runtime inspector class and trace controller class.
 */
public class HostInspectorPropertySheet extends GFPropertySection implements ITabbedPropertyConstants {

	/** The inspector container. */
	private HostInspectorContainer inspectorContainer;
	
	/** The name widget. */
	private Text nameWidget;
	
	/** The runtime inspector class widget. */
	private Text runtimeInspectorClassWidget;
	
	/** The trace controller class widget. */
	private Text traceControllerClassWidget;


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
		getWidgetFactory().createLabel(parent, "Trace Controller Class: ");
		traceControllerClassWidget = getWidgetFactory().createText(parent, "");
		traceControllerClassWidget.addModifyListener(traceControllerClassListener);
		traceControllerClassWidget.setSize(500, 25);
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
		traceControllerClassWidget.removeModifyListener(traceControllerClassListener);

		final PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			final EObject bo = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			// the filter assured, that it is a INamed
			if (bo == null)
				return;
			if(bo instanceof InspectorContainer){
				inspectorContainer =  (HostInspectorContainer) bo;
			}
			else if(bo instanceof IInspector){
				inspectorContainer =  (HostInspectorContainer) ((IInspector) bo).eContainer();
			}
			String nameValue = inspectorContainer.getName();
			nameWidget.setText(nameValue == null ? "" : nameValue);
			nameWidget.setSize(500, 25);
			String runtimeInspector = "";
			if( inspectorContainer.getPorts() != null ){
				if( inspectorContainer.getPorts().get(0) != null ){
					if( inspectorContainer.getPorts().get(0).getRuntimeInspectorClass() != null ){
						runtimeInspector = inspectorContainer.getPorts().get(0).getRuntimeInspectorClass();
					}
				}
			}
			runtimeInspectorClassWidget.setText(runtimeInspector == null ? "" : runtimeInspector);
			runtimeInspectorClassWidget.setSize(500, 25);
			String traceControllerClass = "";
			if( inspectorContainer.getPorts() != null ){
				if( inspectorContainer.getPorts().get(0) != null ){
					if( inspectorContainer.getPorts().get(0).getTraceControllerClass() != null ){
						traceControllerClass = inspectorContainer.getPorts().get(0).getTraceControllerClass();
					}
				}
			}
			traceControllerClassWidget.setText(traceControllerClass == null ? "" : traceControllerClass);
			traceControllerClassWidget.setSize(500, 25);
			
			nameWidget.addModifyListener(nameListener);
			runtimeInspectorClassWidget.addModifyListener(runtimeInspectorClassListener);
			traceControllerClassWidget.addModifyListener(traceControllerClassListener);

		}

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
    
    /** The trace controller class listener. */
    private ModifyListener traceControllerClassListener = new ModifyListener() {
    	public void modifyText(ModifyEvent arg0) {
    		TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
    		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
    			@Override
    			protected void doExecute() {
    				changeTraceControllerClassPropertyValue();
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
    	if (!newValue.equals(inspectorContainer.getPorts().get(0).getRuntimeInspectorClass())) {
    		for (int i = 0; i < inspectorContainer.getPorts().size(); i++) {
    			inspectorContainer.getPorts().get(i).setRuntimeInspectorClass(newValue);
    		}
    	}
    }

    /**
     * Change trace controller class property value.
     */
    private void changeTraceControllerClassPropertyValue() {
    	if(traceControllerClassWidget.getText()== ""){
    		return;
    	}
    	String newValue = traceControllerClassWidget.getText();
    	if (!newValue.equals(inspectorContainer.getPorts().get(0).getTraceControllerClass())) {
    		for (int i = 0; i < inspectorContainer.getPorts().size(); i++) {
    			inspectorContainer.getPorts().get(i).setTraceControllerClass(newValue);
			}
    	}
    }
}
