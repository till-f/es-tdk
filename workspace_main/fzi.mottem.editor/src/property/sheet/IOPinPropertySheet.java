package property.sheet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
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

import fzi.mottem.model.testrigmodel.IOPin;


/**
 * The Class IOPinPropertySheet will be called if the connected business object is a IOPin.
 * This checks the Class IOPinFilter.
 * The property sheet has a text field for the name.
 */
public class IOPinPropertySheet extends GFPropertySection implements ITabbedPropertyConstants {

	/** The pin. */
	private IOPin pin;
	
	/** The name widget. */
	private Text nameWidget;
	
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
		if (pe != null)
		{
			final EObject bo = (EObject) Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pe);
			
			// the filter assured, that it is a INamed
			if (bo == null)
				return;
			
			pin =  (IOPin) bo;
			
			String nameValue = pin.getName();
			nameWidget.setText(nameValue == null ? "" : nameValue);
			nameWidget.setSize(300, 25);
			
			nameWidget.addModifyListener(nameListener);
		}

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
    	if (!newValue.equals(pin.getName())) {
    		pin.setName(newValue);
    		
			// do something to refresh/call the method getToolTip of EditorToolBehaviorProvider
    		
			PictogramElement pe = getSelectedPictogramElement();
			updateDiagram(pe, newValue);
			
			pe.setActive(false);
			pe.setActive(true);

    	}
    }
    
    private void updateDiagram(PictogramElement pe, String name)
    {
		if (pe instanceof ContainerShape)
		{
			for (Shape ele : ((ContainerShape) pe).getChildren())
			{
				if (ele.getGraphicsAlgorithm() instanceof org.eclipse.graphiti.mm.algorithms.Text)
				{
					org.eclipse.graphiti.mm.algorithms.Text txt = (org.eclipse.graphiti.mm.algorithms.Text)ele.getGraphicsAlgorithm();
					txt.setValue(name);
				}
			}
		}

    }
    


}