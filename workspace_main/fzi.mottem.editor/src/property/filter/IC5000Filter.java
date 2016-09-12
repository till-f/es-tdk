package property.filter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import fzi.mottem.model.testrigmodel.IC5000;
import fzi.mottem.model.testrigmodel.IC5000Port;

/**
 * The Class IC5000Filter accepts if the connected business object is instanceof IC5000 or IC5000Port.
 */
public class IC5000Filter extends AbstractPropertySectionFilter
{
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter#accept(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean accept(PictogramElement pictogramElement)
	{
        final EObject eObject = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);
        if (eObject instanceof IC5000 || eObject instanceof IC5000Port)
        {
            return true;
        }
        return false;
	}
}
