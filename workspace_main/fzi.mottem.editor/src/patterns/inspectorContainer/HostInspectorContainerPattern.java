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
import fzi.mottem.model.testrigmodel.CDIInspectorPort;
import fzi.mottem.model.testrigmodel.HostInspectorContainer;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;

/**
 * The Class HostInspectorContainerPattern represents an HostInspector. 
 * The business object will be created in the create-method.
 * The graphical representation of the business object is added in the add-method.
 * In addition one inspector port will be created in the create-method (business object) 
 * and added (graphical representation) in the add-method.
 * Add- and create-method have to be extended if more than one port should be created.
 */
public class HostInspectorContainerPattern extends InspectorContainerPattern {
	
	/** The Constant WIDTH_CONTAINER. */
	private static final int WIDTH_CONTAINER = 75;
	
	/** The Constant HEIGHT_CONTAINER. */
	private static final int HEIGHT_CONTAINER = 85;
	
	/** The Constant X_RECTANGLE. */
	private static final int X_RECTANGLE = 0;
	
	/** The Constant Y_RECTANGLE. */
	private static final int Y_RECTANGLE = 0;
	
	/** The Constant WIDTH_RECTANGLE. */
	private static final int WIDTH_RECTANGLE = 75;
	
	/** The Constant HEIGHT_RECTANGLE. */
	private static final int HEIGHT_RECTANGLE = 70;
	
	/** The Constant X_TEXT1. */
	private static final int X_TEXT1 = 10;
	
	/** The Constant Y_TEXT1. */
	private static final int Y_TEXT1 = 5;
	
	/** The Constant WIDTH_TEXT1. */
	private static final int WIDTH_TEXT1 = 70;
	
	/** The Constant HEIGHT_TEXT1. */
	private static final int HEIGHT_TEXT1 = 20;
	
	/** The Constant X_TEXT2. */
	private static final int X_TEXT2 = 10;
	
	/** The Constant Y_TEXT2. */
	private static final int Y_TEXT2 = 25;
	
	/** The Constant WIDTH_TEXT2. */
	private static final int WIDTH_TEXT2 = 70;
	
	/** The Constant HEIGHT_TEXT2. */
	private static final int HEIGHT_TEXT2 = 20;
	
	/** The Constant HEIGHT_PORT. */
	private static final int HEIGHT_PORT = 15;
	
	/** The Constant WIDTH_PORT. */
	private static final int WIDTH_PORT = 10;
	
	/** The Constant TEXTFIELD1. */
	private static final String TEXTFIELD1 = "Host";
	
	/** The Constant TEXTFIELD2. */
	private static final String TEXTFIELD2 = "Inspector";
	
	/** The Constant BACKGROUNDCOLOR. */
	private static final IColorConstant BACKGROUNDCOLOR = new ColorConstant(152, 251, 152);

	
	/** The inspector ports. */
	private List<CDIInspectorPort> inspectorPorts;
	
	/** The inspector connectors. */
	private List<IInspectorConnector> inspectorConnectors;
	
	/**
	 * Instantiates a new host inspector container pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public HostInspectorContainerPattern(IDiagramTypeProvider dtp) {
		this.dtp = dtp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return "Eclipse Debugger";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context) {
		inspectorPorts = new ArrayList<>();
		//creates business object of HostInspectorContainer
		HostInspectorContainer inspectorContainer = TestrigmodelFactory.eINSTANCE.createHostInspectorContainer();
		inspectorContainer.setName("HostInspector");
		ModelService modelService = ModelService.getModelService(dtp);
		TestRigInstance model = modelService.getModel();
		model.getInspectors().add(inspectorContainer);
		
		//creates business object of CDIInspectorPort
    	CDIInspectorPort inspectorPort = TestrigmodelFactory.eINSTANCE.createCDIInspectorPort();
    	inspectorContainer.getPorts().add(inspectorPort);
    	String name = "";
    	if(inspectorContainer.getPorts() != null ){
    		name = "HostInspectorPort" + (inspectorContainer.getPorts().size() - 1);
    	}
    	inspectorPort.setName(name);
		inspectorPort.setRuntimeInspectorClass("fzi.mottem.runtime.rti.cdi.CDIInspector");
		this.inspectorPorts.add(inspectorPort);

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
		text.setValue(TEXTFIELD1);
		gaService.setLocationAndSize(text, X_TEXT1, Y_TEXT1, WIDTH_TEXT1, HEIGHT_TEXT1);
		Text text2 = gaService.createText(rectangle);
		text2.setValue(TEXTFIELD2);
		gaService.setLocationAndSize(text2, X_TEXT2, Y_TEXT2, WIDTH_TEXT2, HEIGHT_TEXT2);
		
		Shape portShape = peCreateService.createShape(containerShape, true);

		Rectangle portRectangle = gaService.createRectangle(portShape);
		
		gaService.setLocationAndSize(portRectangle, (WIDTH_CONTAINER/2 - WIDTH_PORT/2), HEIGHT_RECTANGLE, WIDTH_PORT, HEIGHT_PORT);
		portRectangle.setFilled(true);
		portRectangle.setBackground(gaService.manageColor(getDiagram(), IColorConstant.BLACK));
		peCreateService.createChopboxAnchor(containerShape);

		peCreateService.createChopboxAnchor(portShape);
		link(portShape, this.inspectorPorts.get(0));
		link(containerShape, newEntity);
		layoutPictogramElement(containerShape);
		return containerShape;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof HostInspectorContainer;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Saves the connections (business objects) from HostInspectorContainer (ports) to a processor to delete them later 
	 * if the user wants to delete the HostInspectorContainer.
	 */
	@Override
	public void delete(IDeleteContext context) {
		this.inspectorConnectors = new ArrayList<>();
		HostInspectorContainer inspectorContainer = (HostInspectorContainer) getBusinessObjectForPictogramElement(context.getPictogramElement());
		for (int i = 0; i <inspectorContainer.getPorts().size(); i++) {
			if(inspectorContainer.getPorts().get(i).getInspectorConnector() != null){
				this.inspectorConnectors.add(inspectorContainer.getPorts().get(i).getInspectorConnector());
			}
		}
		super.delete(context);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Method will be called if user confirmed to delete the HostInspectorContainer.
	 * Deletes the connections (business objects) from HostInspector (ports) to a processor.
	 */
	@Override
	public void postDelete(IDeleteContext context) {
		for (int i = 0; i < inspectorConnectors.size(); i++) {
			if(this.inspectorConnectors.get(i)!=null){
				EcoreUtil.delete(this.inspectorConnectors.get(i));
			}
		} 
		super.postDelete(context);
	}

	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_Inspector_HostInspector;
	}

	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_Inspector_HostInspector;
	}


}
