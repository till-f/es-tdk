package patterns.inspectorContainer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import util.ModelService;
import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.testrigmodel.CANInspectorPort;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.mottem.model.testrigmodel.VN7600;

/**
 * The Class VN7600Pattern represents an VN7600. 
 * The business object will be created in the create-method.
 * The graphical representation of the business object is added in the add-method.
 * In addition three inspector port will be created in the create-method (business object) 
 * and added (graphical representation) in the add-method.

 */
public class VN7600Pattern extends VectorInspectorPattern {
	
	/** The Constant WIDTH_CONTAINER. */
	private static final int WIDTH_CONTAINER = 75;
	
	/** The Constant HEIGHT_CONTAINER. */
	private static final int HEIGHT_CONTAINER = 65;
	
	/** The Constant X_RECTANGLE. */
	private static final int X_RECTANGLE = 0;
	
	/** The Constant Y_RECTANGLE. */
	private static final int Y_RECTANGLE = 0;
	
	/** The Constant WIDTH_RECTANGLE. */
	private static final int WIDTH_RECTANGLE = 75;
	
	/** The Constant HEIGHT_RECTANGLE. */
	private static final int HEIGHT_RECTANGLE = 50;
	
	/** The Constant WIDTH_PORT. */
	private static final int WIDTH_PORT = 10;
	
	/** The Constant HEIGHT_PORT. */
	private static final int HEIGHT_PORT = 15;
	
	/** The Constant X_TEXT. */
	private static final int X_TEXT = 10;
	
	/** The Constant Y_TEXT. */
	private static final int Y_TEXT = 5;
	
	/** The Constant WIDTH_TEXT. */
	private static final int WIDTH_TEXT = 65;
	
	/** The Constant HEIGHT_TEXT. */
	private static final int HEIGHT_TEXT = 20;
	
	/** The Constant NAME. */
	private static final String NAME = "VN7600";
	
	/** The Constant NUMBER_OF_PORTS. */
	private static final int NUMBER_OF_PORTS = 3;
	
	/** The Constant BACKGROUNDCOLOR. */
	private static final IColorConstant BACKGROUNDCOLOR = new ColorConstant(183, 0, 50);
	
	/** The Constant TEXTCOLOR. */
	private static final IColorConstant TEXTCOLOR = new ColorConstant(255, 255, 255);

	/** The can inspector ports. */
	private List<CANInspectorPort> canInspectorPorts;
	
	/** The connectors. */
	private List<IInspectorConnector> connectors;

	/**
	 * Instantiates a new VN7600 pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public VN7600Pattern(IDiagramTypeProvider dtp) {
		this.dtp = dtp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context) {
		canInspectorPorts = new ArrayList<>();
		//creates business object of VN7600
		VN7600 inspectorContainer = TestrigmodelFactory.eINSTANCE.createVN7600();
		inspectorContainer.setName(NAME);
		ModelService modelService = ModelService.getModelService(dtp);
		TestRigInstance model = modelService.getModel();
		model.getInspectors().add(inspectorContainer);
		
		for (int i = 0; i < NUMBER_OF_PORTS; i++) {
			//creates business object of CANInspectorPort
			CANInspectorPort inspectorPort = TestrigmodelFactory.eINSTANCE.createCANInspectorPort();
			inspectorContainer.getCanPorts().add(inspectorPort);

			int portNumber = inspectorContainer.getCanPorts().size() - 1;
			String name = "CANInspectorPort" + portNumber;
			
			inspectorPort.setName(name);
	    	inspectorPort.setRuntimeInspectorClass("fzi.mottem.runtime.rti.vector.VectorAccessDriver");
	    	inspectorPort.setTraceControllerClass("fzi.mottem.runtime.rti.vector.VectorTraceDriver");
			inspectorPort.setChannelNumber(portNumber + 1);
			
			this.canInspectorPorts.add(inspectorPort);
		}

		addGraphicalRepresentation(context, inspectorContainer);
		return new Object[] { inspectorContainer };
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {

		INamed newEntity = (INamed) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(
				context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		Rectangle invisible = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisible, context.getX(), context.getY(), WIDTH_CONTAINER, HEIGHT_CONTAINER);
		Rectangle rectangle = gaService.createRectangle(invisible);
		gaService.setLocationAndSize(rectangle, X_RECTANGLE, Y_RECTANGLE, WIDTH_RECTANGLE, HEIGHT_RECTANGLE);
		rectangle.setFilled(true);
		rectangle.setBackground(gaService.manageColor(getDiagram(), BACKGROUNDCOLOR));
		
		peCreateService.createChopboxAnchor(containerShape);
		
		Text text = gaService.createText(rectangle);
		text.setValue(NAME);
		text.setForeground(manageColor(TEXTCOLOR));
		gaService.setLocationAndSize(text, X_TEXT, Y_TEXT, WIDTH_TEXT, HEIGHT_TEXT);

		
		for (int i = 0; i < canInspectorPorts.size(); i++) {
			Shape portShape = peCreateService.createShape(containerShape, true);
			
			Rectangle portRectangle = gaService.createRectangle(portShape);
			
			gaService.setLocationAndSize(portRectangle, (WIDTH_CONTAINER/4 * (i + 1) - WIDTH_PORT/2), HEIGHT_RECTANGLE, WIDTH_PORT, HEIGHT_PORT);
			portRectangle.setFilled(true);
			portRectangle.setBackground(gaService.manageColor(getDiagram(), IColorConstant.BLACK));
			peCreateService.createChopboxAnchor(containerShape);
			
			peCreateService.createChopboxAnchor(portShape);
			link(portShape, this.canInspectorPorts.get(i));
		}
		
		
		link(containerShape, newEntity);
		layoutPictogramElement(containerShape);
		return containerShape;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof VN7600;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Saves the connections (business objects) from VN7600 (ports) to a can bus to delete them later 
	 * if the user wants to delete the VN7600.
	 */
	@Override
	public void delete(IDeleteContext context) {
		this.connectors = new ArrayList<>();
		VN7600 inspectorContainer = (VN7600) getBusinessObjectForPictogramElement(context.getPictogramElement());
		for (int i = 0; i <inspectorContainer.getCanPorts().size(); i++) {
			if(inspectorContainer.getCanPorts().get(i).getInspectorConnector() != null){
				this.connectors.add(inspectorContainer.getCanPorts().get(i).getInspectorConnector());
			}
		}
		super.delete(context);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Method will be called if user confirmed to delete the VN7600.
	 * Deletes the connections (business objects) from VN7600 (ports) to a can bus.
	 */
	@Override
	public void postDelete(IDeleteContext context) {
		for (int i = 0; i < connectors.size(); i++) {
			if(this.connectors.get(i)!=null){
				EcoreUtil.delete(this.connectors.get(i));
			}
		}
		super.postDelete(context);
	}

	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_Inspector_VN7600;
	}

	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_Inspector_VN7600;
	}

	
}
