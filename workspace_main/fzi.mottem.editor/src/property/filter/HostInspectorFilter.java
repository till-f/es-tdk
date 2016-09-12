package property.filter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import fzi.mottem.model.testrigmodel.CDIInspectorPort;
import fzi.mottem.model.testrigmodel.HostInspectorContainer;


/**
 * The Class HostInspectorFilter accepts if the connected business object is instanceof HostInspectorContainer or CDIInspectorPort.
 */
public class HostInspectorFilter extends AbstractPropertySectionFilter {

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter#accept(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean accept(PictogramElement pictogramElement) {
//		System.out.println("accept inspector?");
        final EObject eObject = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);
        if (eObject instanceof HostInspectorContainer || eObject instanceof CDIInspectorPort) {
//    		System.out.println("accept inspector");
            return true;
        }
        return false;
	}
}
