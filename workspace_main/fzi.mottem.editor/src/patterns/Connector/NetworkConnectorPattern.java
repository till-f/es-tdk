package patterns.Connector;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractConnectionPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.INetwork;
import fzi.mottem.model.baseelements.INetworkConnector;
import fzi.mottem.model.testrigmodel.CANBus;
import fzi.mottem.model.testrigmodel.CANPort;
import fzi.mottem.model.testrigmodel.Ethernet;
import fzi.mottem.model.testrigmodel.EthernetPort;
import fzi.mottem.model.testrigmodel.IOPort;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.util.ecore.EcoreUtils;

/**
 * The Class NetworkConnectorPattern represents a connection for a network.
 * It decides automatically between the business objects for ethernet connection and can bus connection,
 * which depends on the ends of the connection.
 * Business object of the connection will be created in create-method and graphical representation will be 
 * added in add-method
 */
public class NetworkConnectorPattern extends AbstractConnectionPattern{

	public static final String INITIAL_NAME = "Port Connector";

	/**
	 * Instantiates a new network connector pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public NetworkConnectorPattern(IDiagramTypeProvider dtp)
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return INITIAL_NAME;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#canStartConnection(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		IOPort port = getPort(context.getSourceAnchor());
		INetwork network = getNetwork(context.getSourceAnchor());
		if( port != null || network != null ){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#canCreate(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		IOPort sourcePort = getPort(context.getSourceAnchor());
		INetwork sourceNetwork = getNetwork(context.getSourceAnchor());
		IOPort targetPort = getPort(context.getTargetAnchor());
		INetwork targetNetwork = getNetwork(context.getTargetAnchor());
		
		if( sourceNetwork == null && sourcePort == null ){
			return false;
		}
		if( targetNetwork == null && targetPort == null ){
			return false;
		}
		
		if(sourceNetwork instanceof Ethernet){
			if(targetPort instanceof EthernetPort){
				return true;
			}
		}
		if(sourceNetwork instanceof CANBus){
			if(targetPort instanceof CANPort){
				return true;
			}
		}
		if(sourcePort instanceof EthernetPort){
			if(targetNetwork instanceof Ethernet){
				return true;
			}
		}
		if(sourcePort instanceof CANPort){
			if(targetNetwork instanceof CANBus){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Gets the port. If business object of anchor instanceof IOPort = false, return null.
	 *
	 * @param anchor the anchor
	 * @return the port or null
	 */
	private IOPort getPort(Anchor anchor) {
		if (null == anchor) {
			return null;
		}
		Object businessObj = getBusinessObjectForPictogramElement(anchor.getParent());
		if(businessObj instanceof IOPort){
			return (IOPort) businessObj;
		}
		return null;
	}

	/**
	 * Gets the network. If business object of anchor instanceof INetwork = false, return null.
	 *
	 * @param anchor the anchor
	 * @return the network or null
	 */
	private INetwork getNetwork(Anchor anchor) {
		if (null == anchor) {
			return null;
		}
		Object businessObj = getBusinessObjectForPictogramElement(anchor.getParent());
		if(businessObj instanceof INetwork){
			return (INetwork) businessObj;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
//		System.out.println("add port connector");
		IAddConnectionContext addConContext = (IAddConnectionContext) context;
		INetworkConnector connector = (INetworkConnector) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();

		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());

		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPlainPolyline(connection);
		polyline.setLineStyle(LineStyle.SOLID);
		polyline.setForeground(manageColor(IColorConstant.BLACK));
//		polyline.setFilled(true);
		polyline.setWidth(1);
//		polyline.setLineVisible(true);
		link(connection, connector);
//		updatePictogramElement(connection);
		return connection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#create(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public Connection create(ICreateConnectionContext context) {
//		System.out.println("create ethernet port connector");
		Connection newConnection = null;

		IOPort sourcePort = getPort(context.getSourceAnchor());
		INetwork sourceNetwork = getNetwork(context.getSourceAnchor());
		IOPort targetPort = getPort(context.getTargetAnchor());
		INetwork targetNetwork = getNetwork(context.getTargetAnchor());
		
		if( sourceNetwork == null && sourcePort == null ){
			return null;
		}
		if( targetNetwork == null && targetPort == null ){
			return null;
		}
		
		INetworkConnector connector = null;
		
		if(sourcePort instanceof EthernetPort && targetNetwork instanceof Ethernet){
			connector = TestrigmodelFactory.eINSTANCE.createEthernetPortConnector();
		}
		else if(sourceNetwork instanceof Ethernet && targetPort instanceof EthernetPort){
			connector = TestrigmodelFactory.eINSTANCE.createEthernetPortConnector();
		}
		else if(sourcePort instanceof CANPort && targetNetwork instanceof CANBus){
			connector = TestrigmodelFactory.eINSTANCE.createCANPortConnector();
		}
		else if(sourceNetwork instanceof CANBus && targetPort instanceof CANPort){
			connector = TestrigmodelFactory.eINSTANCE.createCANPortConnector();
		}
		
		if( sourcePort != null ){
			connector.setNetworkPort(sourcePort);
			connector.setNetwork(targetNetwork);
		}
		else if( sourceNetwork != null ){
			connector.setNetwork(sourceNetwork);
			connector.setNetworkPort(targetPort);
		}
		
		final TestRigInstance testRig = EcoreUtils.getContainerInstanceOf(connector.getNetwork(), TestRigInstance.class);
		testRig.getNetworkConnectors().add(connector);

		AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
		addContext.setNewObject(connector);
		add(addContext);
		return newConnection;
	}

	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_NetworkConnector;
	}

	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_NetworkConnector;
	}


}
