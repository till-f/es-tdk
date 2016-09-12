package patterns.network;

import org.eclipse.graphiti.features.context.IResizeShapeContext;

import patterns.AbstractComponentPattern;

/**
 * The Class NetworkPattern is the superclass for the concrete network classes EthernetPattern and CANBusPattern.
 */
public abstract class NetworkPattern extends AbstractComponentPattern{

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canResizeShape(org.eclipse.graphiti.features.context.IResizeShapeContext)
	 */
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		return false;
	}

}
