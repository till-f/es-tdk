package patterns.networkPort;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;

import patterns.AbstractComponentPattern;
import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.INetworkConnector;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.mottem.model.testrigmodel.UUT;

/**
 * The Class IOPortPattern is the superclass for the concrete port classes CANPortPattern and EthernetPortPattern.
 * There are some methods implemented, which can be used for both concrete port pattern classes.
 */
public abstract class IOPortPattern  extends AbstractComponentPattern {

	private INetworkConnector connector;

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context) {
		Object businessObject = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return businessObject instanceof UUT;
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canCreate(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public boolean canCreate(ICreateContext context) {
		Object businessObject = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return businessObject instanceof UUT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canResizeShape(org.eclipse.graphiti.features.context.IResizeShapeContext)
	 */
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateImageId()
	 */
	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_IOPORT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateLargeImageId()
	 */
	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_IOPORT;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Saves the connection (business object) from io-port to the network to delete it later if the user wants to delete the io-port.
	 */
	@Override
	public void delete(IDeleteContext context) {
		IOPort port = (IOPort) getBusinessObjectForPictogramElement(context.getPictogramElement());
		this.connector = port.getNetworkConnector();
		super.delete(context);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Method will be called if user confirmed to delete the io-port.
	 * Deletes the connection (business object) from io-port to the network.
	 */
	@Override
	public void postDelete(IDeleteContext context) {
		if(this.connector!=null){
			EcoreUtil.delete(this.connector);
		}
		super.postDelete(context);
	}

}
