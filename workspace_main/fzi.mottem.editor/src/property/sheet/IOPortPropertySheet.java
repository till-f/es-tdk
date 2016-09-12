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

import fzi.mottem.model.datastreammodel.DataStreamInstance;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.util.eclipse.IntegrationUtils;
//import fzi.util.ecore.EcoreUtils;
import fzi.util.ecore.EcoreUtils;

/**
 * The Class IOPortPropertySheet will be called if the connected business object is a IOPort.
 * This checks the Class IOPortFilter.
 * The property sheet has a text field for the name and a field for the data stream instance.
 * There is also a button to change the data stream instance of the IOPort.
 */
public class IOPortPropertySheet extends GFPropertySection implements ITabbedPropertyConstants {
    
	/** The port. */
	private IOPort port = null;

	/** The data stream widget. */
	private Text dataStreamWidget;
    
    /** The name widget. */
    private Text nameWidget;
	
	/** The data stream instances. */
	private List<IResource> dataStreamInstances;
	
	/** The change data stream instance button. */
	private Button changeDataStreamInstanceButton;

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
		nameWidget.setSize(300, 25);
//		getWidgetFactory().createLabel(parent, "inspector: ");
//		inspectorWidget = getWidgetFactory().createText(parent, "");
//		inspectorWidget.setEditable(false);
//		inspectorWidget.setSize(300, 25);
//		getWidgetFactory().createLabel(parent, "");
		getWidgetFactory().createLabel(parent, "Data Stream Instance: ");
		dataStreamWidget = getWidgetFactory().createText(parent, "");
		dataStreamWidget.setEditable(false);
		dataStreamWidget.setSize(300, 25);
//		selectedDataStream = getWidgetFactory().createCCombo(parent);
//		selectedDataStream.addSelectionListener(referencedDataStreamListener);
		changeDataStreamInstanceButton = getWidgetFactory().createButton(parent, "change data stream instance", SWT.PUSH);
		changeDataStreamInstanceButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Display d;

				  final Shell s;

				    d = Display.getCurrent();
				    s = new Shell(d);
				    s.setSize(350, 350);
				    
				    s.setText("Select data stream instance");
				    final org.eclipse.swt.widgets.List l = new org.eclipse.swt.widgets.List(s, SWT.MULTI | SWT.BORDER);
				    l.setBounds(50, 50, 250, 150);
			        IWorkspace workspace = ResourcesPlugin.getWorkspace();
			        String fileExtension = "etm-dstream";
					IPath workspacePath = workspace.getRoot().getLocation();

					dataStreamInstances = IntegrationUtils.getResources(workspacePath, fileExtension);
				    for (IResource res : dataStreamInstances) {
//				    	System.out.println(res.getName());
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
							port = (IOPort) bo;
							TransactionalEditingDomain editingDomain = TransactionUtil
									.getEditingDomain(getDiagram());
							editingDomain.getCommandStack().execute(
									new RecordingCommand(editingDomain) {

										@Override
										protected void doExecute() {
//											if( port.getRefDataStreamInstance() == null ){
//												RefDataStreamInstance refDataStreamInstance = TestrigmodelFactory.eINSTANCE.createRefDataStreamInstance();
//												refDataStreamInstance.setIoPort(port);
////												port.setRefDataStreamInstance(refDataStreamInstance);
//											}
											IResource file = dataStreamInstances.get(l.getSelectionIndex());

											URI platformUri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
											try {
												DataStreamInstance d =	(DataStreamInstance) EcoreUtils.loadFullEMFModel(platformUri);
												port.setSymbolContainer(d);
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
//				    d.dispose();

				
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
		// TODO Auto-generated method stub
		super.setInput(part, selection);
//        IWorkspace workspace = ResourcesPlugin.getWorkspace();
//        String fileExtension = "etm-dstream";
//		IPath workspacePath = workspace.getRoot().getLocation();
//		dataStreamInstances = IntegrationUtils.getResources(workspacePath, fileExtension);
//		
////		selectedDataStream.removeAll();
//		for (IResource file : dataStreamInstances) {
//			selectedDataStream.add(file.getName());
//		}

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
            port = (fzi.mottem.model.testrigmodel.IOPort) bo;
            String nameValue = port.getName();
            nameWidget.setText(nameValue == null ? "" : nameValue);
            nameWidget.setSize(300, 25);
            String dataStreamValue = "";
            if( port.getSymbolContainer() != null ){
            	if( port.getSymbolContainer() != null ){
            		dataStreamValue = port.getSymbolContainer().getName();
            	}
            	
            }
            dataStreamWidget.setText(dataStreamValue == null ? "" : dataStreamValue);
            dataStreamWidget.setSize(300, 25);
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
                    changePropertyValue();
                }
            });
        }
    };

    /**
     * Change property value.
     */
    private void changePropertyValue() {
    	if(nameWidget.getText()== ""){
    		return;
    	}
    	String newValue = nameWidget.getText();
    	if (!newValue.equals(port.getName())) {
    		port.setName(newValue);
    	}
    }
    
}
