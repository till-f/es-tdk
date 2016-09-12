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
import fzi.mottem.model.testrigmodel.AgilentInspector;
import fzi.mottem.model.testrigmodel.AgilentInspectorContainer;
import fzi.mottem.model.testrigmodel.InspectorContainer;

public class AgilentPropertySheet extends GFPropertySection implements ITabbedPropertyConstants {

	/** The inspector container. */
	private AgilentInspectorContainer inspectorContainer;
	
	/** The name widget. */
	private Text nameWidget;
	
	/** The runtime inspector class widget. */
	private Text runtimeInspectorClassWidget;
	
	/** The trace controller class widget. */
	private Text traceControllerClassWidget;
	
	//private Text attrWidget;
	//private Button changeAttributesInstanceButton;

	private Text urlWidget;

	
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
		
		getWidgetFactory().createLabel(parent, "Connect URL: ");
		urlWidget = getWidgetFactory().createText(parent, "");
		urlWidget.addModifyListener(urlWidgetListener);
		urlWidget.setSize(500, 25);

//		getWidgetFactory().createLabel(parent, "Attributes: ");
//		attrWidget = getWidgetFactory().createText(parent, "");
//		attrWidget.setEditable(false);
//		attrWidget.setSize(500, 25);
//		changeAttributesInstanceButton = getWidgetFactory().createButton(parent, "Change Attributes", SWT.PUSH);
//		changeAttributesInstanceButton.addSelectionListener(new SelectionListener()
//		{
//				@Override
//				public void widgetSelected(SelectionEvent e)
//				{
//					Display d;
//	
//					final Shell s;
//	
//				    d = Display.getCurrent();
//				    s = new Shell(d);
//				    s.setSize(550, 350);
//				    
//				    s.setText("Select Attributes Instance");
//				    final org.eclipse.swt.widgets.List l = new org.eclipse.swt.widgets.List(s, SWT.MULTI | SWT.BORDER);
//				    l.setBounds(50, 50, 450, 150);
//			        IWorkspace workspace = ResourcesPlugin.getWorkspace();
//			        String fileExtension = "etm-iattr";
//					IPath workspacePath = workspace.getRoot().getLocation();
//
//					List<IResource> pinAttrInstcs = IntegrationUtils.getResources(workspacePath, fileExtension);
//					
//				    for (IResource res : pinAttrInstcs)
//				    {
//						l.add(res.getName());
//					}
//				    
//				    final Button b1 = new Button(s, SWT.PUSH | SWT.BORDER);
//				    b1.setBounds(50, 230, 50, 25);
//				    b1.setText("OK");
//				    b1.addSelectionListener(new SelectionAdapter()
//				    {
//				      public void widgetSelected(SelectionEvent e)
//				      {
//				        String selected[] = l.getSelection();
//				        for (int i = 0; i < selected.length; i++)
//				        {
//							TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(getDiagram());
//
//							editingDomain.getCommandStack().execute(
//									new RecordingCommand(editingDomain) 
//									{
//										@Override
//										protected void doExecute()
//										{
//											IResource file = pinAttrInstcs.get(l.getSelectionIndex());
//
//											URI platformUri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
//											try
//											{
//												InspectorAttributesInstance attrInst = (InspectorAttributesInstance) EcoreUtils.loadFullEMFModel(platformUri);
//												
//												for (AgilentInspector ai : inspectorContainer.getAgilentPins())
//												{
//													ai.setSymbolContainer(attrInst);
//												}
//												
//												refresh();
//											} catch (IOException e) {
//												e.printStackTrace();
//											}
//
//										}
//									});
//
//				          s.close();
//				        }
//				      }
//				    });
//				    
//				    final Button b2 = new Button(s, SWT.PUSH | SWT.BORDER);
//				    b2.setBounds(130, 230, 50, 25);
//				    b2.setText("Cancel");
//				    b2.addSelectionListener(new SelectionAdapter()
//				    	{
//					    	public void widgetSelected(SelectionEvent e) {
//					    		s.close();
//					    	}
//					    });
//				    
//				    s.open();
//				    while (!s.isDisposed())
//				    {
//				      if (!d.readAndDispatch())
//				        d.sleep();
//				    }
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e)
//			{
//			}
//		});
		
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
		traceControllerClassWidget.removeModifyListener(traceControllerClassListener);
		urlWidget.removeModifyListener(urlWidgetListener);

		final PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			final EObject bo = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			// the filter assured, that it is a INamed
			if (bo == null)
				return;
			if(bo instanceof InspectorContainer){
				inspectorContainer =  (AgilentInspectorContainer) bo;
			}
			else if(bo instanceof IInspector){
				inspectorContainer =  (AgilentInspectorContainer) ((IInspector) bo).eContainer();
			}
			String nameValue = inspectorContainer.getName();
			nameWidget.setText(nameValue == null ? "" : nameValue);
			nameWidget.setSize(500, 25);

			String runtimeInspector = inspectorContainer.getAgilentPins().get(0).getRuntimeInspectorClass();
			runtimeInspectorClassWidget.setText(runtimeInspector == null ? "" : runtimeInspector);
			runtimeInspectorClassWidget.setSize(500, 25);
			
			String traceControllerClass = inspectorContainer.getAgilentPins().get(0).getTraceControllerClass();
			traceControllerClassWidget.setText(traceControllerClass == null ? "" : traceControllerClass);
			traceControllerClassWidget.setSize(500, 25);
			
			String url = inspectorContainer.getConnectURL();
			urlWidget.setText(url == null ? "" : url);
			urlWidget.setSize(500, 25);

			nameWidget.addModifyListener(nameListener);
			runtimeInspectorClassWidget.addModifyListener(runtimeInspectorClassListener);
			traceControllerClassWidget.addModifyListener(traceControllerClassListener);
			urlWidget.addModifyListener(urlWidgetListener);
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
    
    /** The url listener. */
    private ModifyListener urlWidgetListener = new ModifyListener() {
    	public void modifyText(ModifyEvent arg0) {
    		TransactionalEditingDomain editingDomain = getDiagramContainer().getDiagramBehavior().getEditingDomain();
    		editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain) {
    			@Override
    			protected void doExecute() {
    				changeURLPropertyValue();
    			}
    		});
    	}
    };
    
    /**
     * Change name property value.
     */
    private void changeNamePropertyValue() {
    	if(nameWidget.getText()== ""){
    		System.err.println("Name must not be empty");
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
    	String newValue = runtimeInspectorClassWidget.getText();
    	for(AgilentInspector ai : inspectorContainer.getAgilentPins())
    	{
    		ai.setRuntimeInspectorClass(newValue);
    	}
    }
    
    /**
     * Change trace controller class property value.
     */
    private void changeTraceControllerClassPropertyValue() {
    	String newValue = traceControllerClassWidget.getText();
    	for(AgilentInspector ai : inspectorContainer.getAgilentPins())
    	{
    		ai.setTraceControllerClass(newValue);
    	}
    }

    /**
     * Change trace controller class property value.
     */
    private void changeURLPropertyValue() {
    	String newValue = urlWidget.getText();
    	inspectorContainer.setConnectURL(newValue);
    }

}