package property.filter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

import fzi.mottem.model.testrigmodel.Processor;

/**
 * The Class ProcessorFilter accepts if the connected business object is instanceof Processor.
 */
public class ProcessorFilter extends AbstractPropertySectionFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean accept(final PictogramElement pictogramElement) {
//		System.out.println("accept uut?");
        final EObject eObject = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(pictogramElement);
        if (eObject instanceof Processor) {
//    		System.out.println("accept uut");
            return true;
        }
        return false;
    }
}
