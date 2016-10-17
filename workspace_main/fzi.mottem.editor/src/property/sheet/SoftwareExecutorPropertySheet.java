package property.sheet;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.testrigmodel.SoftwareExecutor;
import fzi.mottem.model.util.ModelUtils;
import fzi.util.eclipse.IntegrationUtils;
import fzi.util.ecore.EcoreUtils;

/**
 * The Class SoftwareExecutorPropertySheet will be called if the connected business object is a SoftwareExecutor.
 * This checks the Class SoftwareExecutorFilter.
 * The property sheet has a text fields for name, connected inspector and the code instance.
 * There is also a button to change the code instance of the SoftwareExecutor.
 */
public class SoftwareExecutorPropertySheet extends GFPropertySection implements ITabbedPropertyConstants {

	/** The software executor. */
	protected SoftwareExecutor softwareExecutor;
	
	/** The code widget. */
	private Text codeWidget;
	
	/** The name widget. */
	private Text nameWidget;
	
	/** The code instances. */
	private List<IResource> codeInstances;
	
	/** The change code instance button. */
	private Button changeCodeInstanceButton;


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
		getWidgetFactory().createLabel(parent, "CodeModel: ");
		codeWidget = getWidgetFactory().createText(parent, "");
		codeWidget.setEditable(false);
		codeWidget.setSize(500, 25);
		getWidgetFactory().createLabel(parent, "");
		changeCodeInstanceButton = getWidgetFactory().createButton(parent, "Select CodeModel", SWT.PUSH);
		changeCodeInstanceButton.addSelectionListener(new SelectionListener()
		{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					// TODO Auto-generated method stub
					Display d;
	
					  final Shell s;
	
					    d = Display.getCurrent();
					    s = new Shell(d);
					    s.setSize(550, 350);
					    
					    s.setText("Select CodeModel");
					    final org.eclipse.swt.widgets.List l = new org.eclipse.swt.widgets.List(s, SWT.MULTI | SWT.BORDER);
					    l.setBounds(50, 50, 450, 150);
				        IWorkspace workspace = ResourcesPlugin.getWorkspace();
				        String fileExtension = ModelUtils.FILE_EXTENSION_CODE_MODEL;
						IPath workspacePath = workspace.getRoot().getLocation();
	
						codeInstances = IntegrationUtils.getResources(workspacePath, fileExtension);
						
					    for (IResource res : codeInstances) {
							l.add(res.getName());
						}
					    final Button b1 = new Button(s, SWT.PUSH | SWT.BORDER);
					    b1.setBounds(50, 230, 50, 25);
					    b1.setText("OK");
					    b1.addSelectionListener(new SelectionAdapter() {
					      public void widgetSelected(SelectionEvent e) {
					        String selected[] = l.getSelection();
					        for (int i = 0; i < selected.length; i++) {
								final PictogramElement pe = getSelectedPictogramElement();
								Object bo = Graphiti.getLinkService()
										.getBusinessObjectForLinkedPictogramElement(pe);
								if (bo == null) {
									return;
								}
								softwareExecutor = (SoftwareExecutor) bo;
								TransactionalEditingDomain editingDomain = TransactionUtil
										.getEditingDomain(getDiagram());
								editingDomain.getCommandStack().execute(
										new RecordingCommand(editingDomain) {
	
											@Override
											protected void doExecute() {
												IResource file = codeInstances.get(l.getSelectionIndex());
	
												URI platformUri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
												try {
													CodeInstance codeInstance =	(CodeInstance) EcoreUtils.loadFullEMFModel(platformUri);
													softwareExecutor.setSymbolContainer(codeInstance);
													refresh();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
	
											}
										});
	
					          s.close();
					        }
					      }
					    });
					    final Button b2 = new Button(s, SWT.PUSH | SWT.BORDER);
					    b2.setBounds(130, 230, 50, 25);
					    b2.setText("Cancel");
					    b2.addSelectionListener(new SelectionAdapter() {
					    	public void widgetSelected(SelectionEvent e) {
					    		s.close();
					    	}
					    });
					    s.open();
					    while (!s.isDisposed()) {
					      if (!d.readAndDispatch())
					        d.sleep();
					    }
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e)
				{
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

		final PictogramElement pe = getSelectedPictogramElement();
		if (pe != null) {
			final EObject bo = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			// the filter assured, that it is a INamed
			if (bo == null)
				return;
			softwareExecutor = (fzi.mottem.model.testrigmodel.SoftwareExecutor) bo;
			String nameValue = softwareExecutor.getName();
			nameWidget.setText(nameValue == null ? "" : nameValue);
			nameWidget.setSize(500, 25);
			String codeValue = "";
			if( softwareExecutor.getSymbolContainer() != null ){
				if( softwareExecutor.getSymbolContainer() != null ){
					codeValue = softwareExecutor.getSymbolContainer().getName();
				}

			}
			codeWidget.setText(codeValue == null ? "" : codeValue);
			codeWidget.setSize(500, 25);
		}
		
		nameWidget.addModifyListener(nameListener);

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

    /**
     * Change property value.
     */
    private void changeNamePropertyValue() {
    	if(nameWidget.getText()== ""){
    		return;
    	}
    	String newValue = nameWidget.getText();
    	if (!newValue.equals(softwareExecutor.getName())) {
    		softwareExecutor.setName(newValue);
    		
			// do something to refresh/call the method getToolTip of EditorToolBehaviorProvider
			PictogramElement pe = getSelectedPictogramElement();
			pe.setActive(false);
			pe.setActive(true);

    	}
    }

	
//    private SelectionListener referencedCodeInstanceListener = new SelectionListener() {
//		
//		@Override
//		public void widgetSelected(SelectionEvent e) {
//			final PictogramElement pe = getSelectedPictogramElement();
//			Object bo = Graphiti.getLinkService()
//					.getBusinessObjectForLinkedPictogramElement(pe);
//			if (bo == null) {
//				return;
//			}
//			softwareExecutor = (SoftwareExecutor) bo;
//			TransactionalEditingDomain editingDomain = TransactionUtil
//					.getEditingDomain(getDiagram());
//			editingDomain.getCommandStack().execute(
//					new RecordingCommand(editingDomain) {
//						
//						@Override
//						protected void doExecute() {
////							if( core.getCodeInstance()() == null ){
////								RefCodeInstance refCodeInstance = TestrigmodelFactory.eINSTANCE.createRefCodeInstance();
////								refCodeInstance.setSoftwareExecutor(core);
//////								core.setRefCodeInstance(refCodeInstance);
////							}
//							IResource file = codeInstances.get(selectedCodeInstance.getSelectionIndex());
//
//							URI platformUri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
//							try {
//								CodeInstance codeInstance =	(CodeInstance) EcoreUtils.loadFullEMFModel(platformUri);
//								softwareExecutor.setCodeInstance(codeInstance);
//								refresh();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					});
//		}
//		
//		@Override
//		public void widgetDefaultSelected(SelectionEvent e) {
//			// TODO Auto-generated method stub
//
//		}
//	};


}
