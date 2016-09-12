package features;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.impl.DefaultReconnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import util.ModelService;
import fzi.mottem.model.baseelements.IInspectable;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.INetwork;
import fzi.mottem.model.baseelements.INetworkConnector;
import fzi.mottem.model.baseelements.INetworkPort;
import fzi.mottem.model.testrigmodel.CDIInspectorPort;
import fzi.mottem.model.testrigmodel.IC5000Port;
import fzi.mottem.model.testrigmodel.IOnePort;
import fzi.mottem.model.testrigmodel.JTAGInspectorConnector;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.mottem.model.testrigmodel.TraceInspectorConnector;

/**
 * The Class ReconnectionFeature will be called if start or end of a connection is changed.
 * The methods are canReconnect to check if the end of the connection can be changed to the 
 * new component and postReconnect to change the domain object of the connection or even 
 * to delete the connection element and create a new connection element in the model file.
 * It is allowed to change the connection end to an instance of the same ecore class and
 * from IOnePort or IC5000Port or CDIInspectorPort to IOnePort or IC5000Port or CDIInspectorPort.  
 */
public class ReconnectionFeature extends DefaultReconnectionFeature {
	 
	
	private IInspectable inspectable;
	private IInspectorConnector connector;
	private IDiagramTypeProvider dtp;
	
    /**
     * Instantiates a new reconnection feature.
     *
     * @param iDiagramTypeProvider the feature provider
     */
    public ReconnectionFeature(IFeatureProvider featureProvider, IDiagramTypeProvider iDiagramTypeProvider) {
    	super(featureProvider);
    	dtp = iDiagramTypeProvider;
    }

    /* (non-Javadoc)
     * @see org.eclipse.graphiti.features.impl.DefaultReconnectionFeature#canReconnect(org.eclipse.graphiti.features.context.IReconnectionContext)
     */
    @Override
    public boolean canReconnect(IReconnectionContext context)
    {
    	if(context.getNewAnchor()!=null)
    	{
    		if(context.getNewAnchor().eContainer() instanceof PictogramElement)
    		{
    			PictogramElement newPictogramElement = (PictogramElement) context.getNewAnchor().eContainer();
    			Class<? extends EObject> eClassOld = newPictogramElement.getLink().getBusinessObjects().get(0).getClass();
    			
    			if( context.getOldAnchor()!= null )
    			{
    				if(((PictogramElement) context.getOldAnchor().eContainer()).getLink() != null)
    				{
    					if( eClassOld.equals(((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0).getClass()))
    					{
    						return true;
    					}
    					if( newPictogramElement.getLink().getBusinessObjects().get(0) instanceof CDIInspectorPort ||
    						newPictogramElement.getLink().getBusinessObjects().get(0) instanceof IC5000Port ||
    						newPictogramElement.getLink().getBusinessObjects().get(0) instanceof IOnePort)
    					{
    						if( ((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof CDIInspectorPort ||
    							((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IC5000Port ||
    							((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IOnePort)
    						{
    							return true;
    						}
    					}
    				}
    			}
    		} 
    	}
        return false;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.impl.DefaultReconnectionFeature#preReconnect(org.eclipse.graphiti.features.context.IReconnectionContext)
	 */
	@Override
	public void preReconnect(IReconnectionContext context)
	{
		if( context.getConnection().getLink().getBusinessObjects().get(0) instanceof IInspectorConnector)
		{
			inspectable = ((IInspectorConnector)context.getConnection().getLink().getBusinessObjects().get(0)).getInspectable();
			this.connector = (IInspectorConnector) context.getConnection().getLink().getBusinessObjects().get(0);
		}

		super.preReconnect(context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.features.impl.DefaultReconnectionFeature#postReconnect(org.eclipse.graphiti.features.context.IReconnectionContext)
	 */
	@Override
	public void postReconnect(IReconnectionContext context) {
		super.postReconnect(context);

		Connection connection = context.getConnection();
		if(connection.getLink() != null){
			if(connection.getLink().getBusinessObjects().get(0) != null){
				if(connection.getLink().getBusinessObjects().get(0) instanceof IInspectorConnector){
					IInspectorConnector connector = (IInspectorConnector) connection.getLink().getBusinessObjects().get(0);
					if(((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IInspector){
						if( (((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IC5000Port ||
								((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof CDIInspectorPort) &&
								(((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IC5000Port ||
								((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof CDIInspectorPort)){
							connector.setInspector((IInspector) ((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0));
						}
						else if((((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IC5000Port ||
								((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof CDIInspectorPort) &&
								((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IOnePort){

							EcoreUtil.delete(this.connector);
							JTAGInspectorConnector jtagInspectorConnector = TestrigmodelFactory.eINSTANCE.createJTAGInspectorConnector();
							jtagInspectorConnector.setInspectable(inspectable);
							jtagInspectorConnector.setInspector((IOnePort) ((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0));
							ModelService modelService = ModelService.getModelService(dtp);
							TestRigInstance model = modelService.getModel();
							model.getConnectors().add(jtagInspectorConnector);

							link(connection, jtagInspectorConnector);
							
						}
						else if((((PictogramElement) context.getOldAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IOnePort &&
								(((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof CDIInspectorPort) ||
								((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IC5000Port)){
							
							EcoreUtil.delete(connector);
							TraceInspectorConnector traceInspectorConnector = TestrigmodelFactory.eINSTANCE.createTraceInspectorConnector();
							traceInspectorConnector.setInspectable(inspectable);
							traceInspectorConnector.setInspector((IInspector) ((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0));
							ModelService modelService = ModelService.getModelService(dtp);
							TestRigInstance model = modelService.getModel();
							model.getConnectors().add(traceInspectorConnector);
							
							link(connection, traceInspectorConnector);
							
						}
					}
					else if(((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof IInspectable){
						connector.setInspectable((IInspectable) ((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0));
					}
				}
				else if(connection.getLink().getBusinessObjects().get(0) instanceof INetworkConnector){
					INetworkConnector connector = (INetworkConnector) connection.getLink().getBusinessObjects().get(0);
					if(((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof INetworkPort){
						connector.setNetworkPort((INetworkPort) ((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0));
					}
					else if(((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0) instanceof INetwork){
						connector.setNetwork((INetwork) ((PictogramElement) context.getNewAnchor().eContainer()).getLink().getBusinessObjects().get(0));
					}
				}
			}
		}
	}
} 