package features;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import fzi.mottem.model.datastreammodel.DataStreamInstance;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.util.ecore.EcoreUtils;

/**
 * The Class AddDataStreamInstanceToIOPortFeature.  It should be possible to set a reference from a 
 * data stream instance to a i/o-port executor per drag&drop from the workspace. 
 */
public class AddDataStreamInstanceToIOPortFeature extends AbstractAddFeature	 {

	/**
	 * Instantiates a new feature class to add the data stream instance to an i/o-port.
	 *
	 * @param fp the feature provider
	 */
	public AddDataStreamInstanceToIOPortFeature(IFeatureProvider fp) {
		super(fp);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context) {
		if (context.getTargetContainer() instanceof ContainerShape) {
			final Object target = getBusinessObjectForPictogramElement(context.getTargetContainer());
			if (target instanceof fzi.mottem.model.testrigmodel.IOPort) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
        final Object target = getBusinessObjectForPictogramElement(context.getTargetContainer());
        if (target instanceof fzi.mottem.model.testrigmodel.IOPort) {
        	IOPort port = (IOPort) target;
			if(context.getNewObject() instanceof IResource){
				IResource file = (IResource) context.getNewObject();
				URI platformUri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
				try {
					DataStreamInstance dataStreamInstance =	(DataStreamInstance) EcoreUtils.loadFullEMFModel(platformUri);
					port.setSymbolContainer(dataStreamInstance);
//					System.out.println("added data stream: " + dataStreamInstance.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }
		return null;
	}

}
