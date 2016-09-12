package property.filter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import fzi.mottem.model.testrigmodel.IOne;
import fzi.mottem.model.testrigmodel.IOnePort;

/**
 * The Class IOneFilter accepts if the connected business object is instanceof IOne or IOnePort.
 */
public class IOneFilter extends AbstractPropertySectionFilter {

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter#accept(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean accept(PictogramElement pictogramElement) {
//		System.out.println("accept IOne?");
        final EObject eObject = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);
        if (eObject instanceof IOne || eObject instanceof IOnePort) {
//    		System.out.println("accept IOne");
            return true;
        }
        return false;
	}
}
