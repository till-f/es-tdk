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
import fzi.mottem.model.baseelements.IInspectable;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.testrigmodel.AgilentInspector;
import fzi.mottem.model.testrigmodel.CANBus;
import fzi.mottem.model.testrigmodel.CANInspectorPort;
import fzi.mottem.model.testrigmodel.CDIInspectorPort;
import fzi.mottem.model.testrigmodel.IC5000Port;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.IOnePort;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.util.ecore.EcoreUtils;

/**
 * The Class InspectorConnectorPattern  represents a connection from an inspector to inspectable component.
 * It decides automatically between the business objects for JTAGInspectorConnector, TraceInspectorConnector,
 * and CANInspectorConnector, which depends on the ends of the connection.
 * Business object of the connection will be created in create-method and graphical representation will be 
 * added in add-method
 */
public class InspectorConnectorPattern extends AbstractConnectionPattern
{
	public static final String INITIAL_NAME = "HW Connector";

	/**
	 * Instantiates a new inspector connector pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public InspectorConnectorPattern(IDiagramTypeProvider dtp)
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
	public boolean canStartConnection(ICreateConnectionContext context)
	{
		IInspector sourceInspector = getInspector(context.getSourceAnchor());
		IInspectable sourceInspectable = getInspectable(context.getSourceAnchor());
		if(sourceInspector != null)
		{
			if(sourceInspector.getInspectorConnector() != null){
				if(sourceInspector.getInspectorConnector().getInspector() != null){
					return false;
				}
			}
		}
		if(sourceInspectable != null)
		{
			if(sourceInspectable.getInspectorConnector() != null){
				if(sourceInspectable.getInspectorConnector().getInspector() != null){
					return false;
				}
			}
		}
		if( sourceInspector != null || sourceInspectable != null ){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context)
	{
		if (false == context instanceof IAddConnectionContext)
		{
			return false;
		}
		IAddConnectionContext acc = (IAddConnectionContext) context;
		return null != acc.getNewObject();
	}

	/**
	 * Gets the inspector. If business object of anchor instanceof IInspector = false, return null.
	 *
	 * @param anchor the source anchor
	 * @return the inspector or null
	 */
	private IInspector getInspector(Anchor anchor)
	{
		if (null == anchor) {
			return null;
		}
		Object businessObj = getBusinessObjectForPictogramElement(anchor.getParent());
		if(businessObj instanceof IInspector){
			return (IInspector) businessObj;
		}
		return null;
	}
	
	/**
	 * Gets the inspectable. If business object of anchor instanceof IInspectable = false, return null.
	 *
	 * @param anchor the target anchor
	 * @return the inspectable or null
	 */
	private IInspectable getInspectable(Anchor anchor)
	{
		if (null == anchor) {
			return null;
		}
		Object businessObj = getBusinessObjectForPictogramElement(anchor.getParent());
		if(businessObj instanceof IInspectable){
			return (IInspectable) businessObj;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#canCreate(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public boolean canCreate(ICreateConnectionContext context)
	{
		IInspector sourceInspector = getInspector(context.getSourceAnchor());
		IInspectable sourceInspectable = getInspectable(context.getSourceAnchor());
		IInspector targetInspector = getInspector(context.getTargetAnchor());
		IInspectable targetInspectable = getInspectable(context.getTargetAnchor());
		
		if(sourceInspector == null && sourceInspectable == null ){
			return false;
		}
		if(targetInspectable == null && targetInspector == null ){
			return false;
		}
		
		if(targetInspector != null){
			if(targetInspector.getInspectorConnector() != null){
				if(targetInspector.getInspectorConnector().getInspector() != null){
					return false;
				}
			}
		}
		if(targetInspectable != null){
			if(targetInspectable.getInspectorConnector() != null){
				if(targetInspectable.getInspectorConnector().getInspector() != null){
					return false;
				}
			}
		}
		
		if(sourceInspector instanceof IOnePort){
			if(targetInspectable instanceof Processor){
				return true;
			}
		}
		if(sourceInspector instanceof IC5000Port){
			if(targetInspectable instanceof Processor){
				return true;
			}
		}
		if(sourceInspector instanceof CANInspectorPort){
			if(targetInspectable instanceof CANBus){
				return true;
			}
		}
		if(sourceInspector instanceof CDIInspectorPort){
			if(targetInspectable instanceof Processor){
				return true;
			}
		}
		if(sourceInspectable instanceof Processor){
			if(targetInspector instanceof IC5000Port || targetInspector instanceof IOnePort 
					|| targetInspector instanceof CDIInspectorPort){
				return true;
			}
		}
		if(sourceInspectable instanceof CANBus){
			if(targetInspector instanceof CANInspectorPort){
				return true;
			}
		}
		if(sourceInspectable instanceof IOPin){
			if(targetInspector instanceof AgilentInspector){
				return true;
			}
		}
		if(sourceInspector instanceof AgilentInspector){
			if(targetInspectable instanceof IOPin){
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context)
	{
		IAddConnectionContext addContext = (IAddConnectionContext) context;
		IInspectorConnector connector = (IInspectorConnector) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();

		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		
		connection.setStart(addContext.getSourceAnchor());
		connection.setEnd(addContext.getTargetAnchor());

		IGaService gaService = Graphiti.getGaService();
		Polyline polyline = gaService.createPlainPolyline(connection);
		polyline.setLineStyle(LineStyle.SOLID);
		polyline.setForeground(manageColor(IColorConstant.BLACK));
		polyline.setWidth(1);
		polyline.getPictogramElement().setActive(false);
		link(connection, connector);
		return connection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractConnectionPattern#create(org.eclipse.graphiti.features.context.ICreateConnectionContext)
	 */
	@Override
	public Connection create(ICreateConnectionContext context)
	{
		Connection newConnection = null;
		IInspector sourceInspector = getInspector(context.getSourceAnchor());
		IInspectable sourceInspectable = getInspectable(context.getSourceAnchor());
		IInspector targetInspector = getInspector(context.getTargetAnchor());
		IInspectable targetInspectable = getInspectable(context.getTargetAnchor());
		if ((sourceInspector == null && sourceInspectable == null) || (targetInspectable == null && targetInspector == null)) {
			return null;
		}
		IInspectorConnector connector = null;
		if(sourceInspector instanceof IOnePort && targetInspectable instanceof Processor)
		{
			connector = TestrigmodelFactory.eINSTANCE.createJTAGInspectorConnector();
		}
		else if(sourceInspector instanceof IC5000Port && targetInspectable instanceof Processor)
		{
			connector = TestrigmodelFactory.eINSTANCE.createTraceInspectorConnector();
		}
		else if(sourceInspector instanceof CANInspectorPort && targetInspectable instanceof CANBus)
		{
			connector = TestrigmodelFactory.eINSTANCE.createCANInspectorConnector();
		}
		else if(sourceInspector instanceof CDIInspectorPort && targetInspectable instanceof Processor)
		{
			connector = TestrigmodelFactory.eINSTANCE.createTraceInspectorConnector();
		}
		else if(sourceInspectable instanceof CANBus && targetInspector instanceof CANInspectorPort)
		{
			connector = TestrigmodelFactory.eINSTANCE.createCANInspectorConnector();
		}
		else if(sourceInspectable instanceof IOPin && targetInspector instanceof AgilentInspector ||
				sourceInspector instanceof AgilentInspector && targetInspectable instanceof IOPin )
		{
			connector = TestrigmodelFactory.eINSTANCE.createPinConnector();
		}
		else if(sourceInspectable instanceof Processor)
		{ 
				if(targetInspector instanceof IOnePort){
					connector = TestrigmodelFactory.eINSTANCE.createJTAGInspectorConnector();
				}
				else if(targetInspector instanceof IC5000Port){
					connector = TestrigmodelFactory.eINSTANCE.createTraceInspectorConnector();
				}
				else if(targetInspector instanceof CDIInspectorPort){
					connector = TestrigmodelFactory.eINSTANCE.createTraceInspectorConnector();
				}
		}
		else
		{
			System.out.println("Not supported yet");
			return null;
		}
		if(sourceInspector != null)
		{
			connector.setInspector(sourceInspector);
			connector.setInspectable(targetInspectable);
			sourceInspector.setInspectorConnector(connector);
		}
		if(sourceInspectable != null)
		{
			connector.setInspectable(sourceInspectable);
			connector.setInspector(targetInspector);
			targetInspector.setInspectorConnector(connector);
		}
		
		final TestRigInstance testRig = EcoreUtils.getContainerInstanceOf(connector.getInspector(), TestRigInstance.class);
		testRig.getConnectors().add(connector);
		
		AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
		addContext.setNewObject(connector);
		add(addContext);
		return newConnection;
	}
	
	@Override
	public String getCreateImageId()
	{
		return EditorImageProvider.IMG_InspectorConnector;
	}

	@Override
	public String getCreateLargeImageId()
	{
		return EditorImageProvider.IMG_InspectorConnector;
	}


}
