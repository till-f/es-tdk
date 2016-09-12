package property.filter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import fzi.mottem.model.testrigmodel.IOPort;

/**
 * The Class IOPortFilter accepts if the connected business object is instanceof IOPort.
 */
public class IOPortFilter extends AbstractPropertySectionFilter {

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter#accept(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean accept(PictogramElement pictogramElement) {
//		System.out.println("accept port?");
	       final EObject eObject = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);
	        if (eObject instanceof IOPort) {
//	    		System.out.println("accept port");
	            return true;
	        }
	        return false;
	}

}
