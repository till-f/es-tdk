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
import fzi.mottem.model.testrigmodel.CANInspectorPort;
import fzi.mottem.model.testrigmodel.InspectorContainer;
import fzi.mottem.model.testrigmodel.VN7600;


/**
 * The Class VN7600PropertySheet will be called if the connected business object is a 
 * VN7600 or CANInspectorPort. This checks the Class VN7600Filter.
 * The property sheet has text fields for name and runtime inspector class.
 */
public class VN7600PropertySheet extends GFPropertySection implements ITabbedPropertyConstants {

	/** The inspector container. */
	private VN7600 inspectorContainer;
	
	/** the object */
	private CANInspectorPort inspectorPort = null;
	
	/** The name widget. */
	private Text nameWidget;
	
	/** The runtime inspector class widget. */
	private Text runtimeInspectorClassWidget;

	/** The runtime inspector class widget. */
	private Text traceControllerClassWidget;

	/** The channelNumber widget. */
	private Text channelNumberWidget;

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

		getWidgetFactory().createLabel(parent, "Channel Number: ");
		channelNumberWidget = getWidgetFactory().createText(parent, "");
		channelNumberWidget.addModifyListener(channelNumberListener);
		channelNumberWidget.setSize(500, 25);
		channelNumberWidget.setEnabled(false);
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
		if (pe != null)
		{
			final EObject bo = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);

			if (bo == null)
				return;
			
			String runtimeInspector = "";
			String traceController = "";
			String channelNumber = "";

			if(bo instanceof InspectorContainer)
			{
				inspectorPort = null;
				inspectorContainer = (VN7600) bo;

				if( inspectorContainer.getCanPorts() != null )
				{
					if( inspectorContainer.getCanPorts().get(0) != null )
					{
						if( inspectorContainer.getCanPorts().get(0).getRuntimeInspectorClass() != null )
						{
							runtimeInspector = "" + inspectorContainer.getCanPorts().get(0).getRuntimeInspectorClass();
							traceController = "" + inspectorContainer.getCanPorts().get(0).getTraceControllerClass();
						}
					}
				}

				runtimeInspectorClassWidget.setEnabled(false);
				traceControllerClassWidget.setEnabled(false);
				channelNumberWidget.setEnabled(false);
			}
			else if(bo instanceof IInspector)
			{
				inspectorPort = (CANInspectorPort) bo;
				inspectorContainer =  (VN7600) ((IInspector) bo).eContainer();
				
				runtimeInspector = "" + inspectorPort.getRuntimeInspectorClass();
				traceController = "" + inspectorPort.getTraceControllerClass();
				channelNumber = "" + inspectorPort.getChannelNumber();

				runtimeInspectorClassWidget.setEnabled(true);
				traceControllerClassWidget.setEnabled(true);
				channelNumberWidget.setEnabled(true);
			}
			
			runtimeInspectorClassWidget.setText(runtimeInspector);
			runtimeInspectorClassWidget.setSize(500, 25);
			traceControllerClassWidget.setText(traceController);
			traceControllerClassWidget.setSize(500, 25);
			channelNumberWidget.setText(channelNumber);
			channelNumberWidget.setSize(500, 25);
			
			String nameValue = inspectorContainer.getName();
			nameWidget.setText(nameValue == null ? "" : nameValue);
			nameWidget.setSize(500, 25);
			
			nameWidget.addModifyListener(nameListener);
			runtimeInspectorClassWidget.addModifyListener(runtimeInspectorClassListener);
			traceControllerClassWidget.addModifyListener(traceControllerClassListener);
			channelNumberWidget.addModifyListener(channelNumberListener);
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
    
    /** The runtime inspector class listener. */
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
    
    /** The channel number listener. */
    private ModifyListener channelNumberListener = new ModifyListener() {
    	public void modifyText(ModifyEvent arg0) {
    		TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
    		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
    			@Override
    			protected void doExecute() {
    				changeChannelNumberPropertyValue();
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
    private void changeRuntimeInspectorClassPropertyValue()
    {
    	if(runtimeInspectorClassWidget.getText()== "")
    	{
    		return;
    	}
    	
    	String newValue = runtimeInspectorClassWidget.getText();
    	
    	if (inspectorPort == null)
    	{
        	if (!newValue.equals(inspectorContainer.getCanPorts().get(0).getRuntimeInspectorClass()))
        	{
        		for (int i = 0; i < inspectorContainer.getCanPorts().size(); i++)
        		{
        			inspectorContainer.getCanPorts().get(i).setRuntimeInspectorClass(newValue);
        		}
        	}
    	}
    	else
    	{
        	if (!newValue.equals(inspectorPort.getRuntimeInspectorClass()))
        	{
    			inspectorPort.setRuntimeInspectorClass(newValue);
        	}
    	}
    }

    /**
     * Change runtime inspector class property value.
     */
    private void changeTraceControllerClassPropertyValue()
    {
    	if(traceControllerClassWidget.getText()== "")
    	{
    		return;
    	}
    	
    	String newValue = traceControllerClassWidget.getText();
    	
    	if (inspectorPort == null)
    	{
        	if (!newValue.equals(inspectorContainer.getCanPorts().get(0).getTraceControllerClass()))
        	{
        		for (int i = 0; i < inspectorContainer.getCanPorts().size(); i++)
        		{
        			inspectorContainer.getCanPorts().get(i).setTraceControllerClass(newValue);
        		}
        	}
    	}
    	else
    	{
        	if (!newValue.equals(inspectorPort.getTraceControllerClass()))
        	{
    			inspectorPort.setTraceControllerClass(newValue);
        	}
    	}
    }

    private void changeChannelNumberPropertyValue()
    {
    	if(channelNumberWidget.getText()== "" || inspectorPort == null)
    	{
    		return;
    	}
    	
    	String newValue = channelNumberWidget.getText();
    	
    	if (!newValue.equals(inspectorPort.getChannelNumber()))
    	{
			inspectorPort.setChannelNumber(Integer.parseInt(newValue));
    	}
    }
}
