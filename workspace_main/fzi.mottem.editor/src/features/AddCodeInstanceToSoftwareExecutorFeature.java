package features;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import fzi.mottem.model.codemodel.CodeInstance;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.SoftwareExecutor;
import fzi.util.ecore.EcoreUtils;

/**
 * Doesn't work at the moment.
 * 
 * The Class AddCodeInstanceToSoftwareExecutorFeature. It should be possible to set a reference from a 
 * code instance to a software executor per drag&drop from the workspace. 
 */
public class AddCodeInstanceToSoftwareExecutorFeature extends org.eclipse.graphiti.features.impl.AbstractAddFeature{

	/**
	 * Instantiates a new feature class to adds the code instance to a software executor .
	 *
	 * @param fp the feature provider
	 */
	public AddCodeInstanceToSoftwareExecutorFeature(IFeatureProvider fp) {
		super(fp);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context) {
            final Object target = getBusinessObjectForPictogramElement(context.getTargetContainer());
            if( target instanceof Processor){
//            	System.out.println(context.getTargetContainer());
            }
//            System.out.println(target.getClass());
            if (target instanceof fzi.mottem.model.testrigmodel.SoftwareExecutor) {
            	return true;
            }
//        }
        return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
        final Object target = getBusinessObjectForPictogramElement(context.getTargetContainer());
        if (target instanceof fzi.mottem.model.testrigmodel.SoftwareExecutor) {
        	SoftwareExecutor softwareExecutor = (SoftwareExecutor) target;
			if(context.getNewObject() instanceof IResource){
				IResource file = (IResource) context.getNewObject();
				URI platformUri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
				try {
					CodeInstance codeInstance =	(CodeInstance) EcoreUtils.loadFullEMFModel(platformUri);
					softwareExecutor.setSymbolContainer(codeInstance);
//					System.out.println("added code instance: " + codeInstance.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }
		return null;
	}

}
