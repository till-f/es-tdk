package patterns.inspectorContainer;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import util.ModelService;
import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.testrigmodel.IC5000;
import fzi.mottem.model.testrigmodel.IC5000Port;
import fzi.mottem.model.testrigmodel.TestRigInstance;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;

/**
 * The Class IC5000Pattern represents an IC5000. 
 * The business object will be created in the create-method.
 * The representation of the business object is realized in the superclass InspectorContainerPattern.
 * In addition one inspector port will be created in the create-method (business object) 
 * and added (graphical representation) in the add-method.
 */
public class IC5000Pattern extends ISystemInspectorPattern {
	
	/** The Constant WIDTH_CONTAINER. */
	private static final int WIDTH_CONTAINER = 60;
	
	/** The Constant HEIGHT_CONTAINER. */
	private static final int HEIGHT_CONTAINER = 65;
	
	/** The Constant X_RECTANGLE. */
	private static final int X_RECTANGLE = 0;
	
	/** The Constant Y_RECTANGLE. */
	private static final int Y_RECTANGLE = 0;
	
	/** The Constant WIDTH_RECTANGLE. */
	private static final int WIDTH_RECTANGLE = 60;
	
	/** The Constant HEIGHT_RECTANGLE. */
	private static final int HEIGHT_RECTANGLE = 50;
	
	/** The Constant X_TEXT. */
	private static final int X_TEXT = 5;
	
	/** The Constant Y_TEXT. */
	private static final int Y_TEXT = 5;
	
	/** The Constant WIDTH_TEXT. */
	private static final int WIDTH_TEXT = 50;
	
	/** The Constant HEIGHT_TEXT. */
	private static final int HEIGHT_TEXT = 20;
	
	/** The Constant HEIGHT_PORT. */
	private static final int HEIGHT_PORT = 15;
	
	/** The Constant WIDTH_PORT. */
	private static final int WIDTH_PORT = 10;
	
	/** The Constant NAME. */
	private static final String NAME = "IC5000";
	
	/** The Constant BACKGROUNDCOLOR. */
	private static final IColorConstant BACKGROUNDCOLOR = new ColorConstant(30, 144, 255);

	/** The Constant TEXTCOLOR. */
	private static final IColorConstant TEXTCOLOR = new ColorConstant(255, 255, 255);

	/** The inspector port. */
	private IC5000Port inspectorPort;
	
	/** The inspector connector. */
	private IInspectorConnector inspectorConnector;

	/**
	 * Instantiates a new ic5000 pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public IC5000Pattern(IDiagramTypeProvider dtp) {
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
		//creates business object of IC5000
		IC5000 inspectorContainer = TestrigmodelFactory.eINSTANCE.createIC5000();
		inspectorContainer.setName(NAME);
		ModelService modelService = ModelService.getModelService(dtp);
		TestRigInstance model = modelService.getModel();
		model.getInspectors().add(inspectorContainer);
		
		//creates business object of IC5000Port
    	IC5000Port inspectorPort = TestrigmodelFactory.eINSTANCE.createIC5000Port();
    	inspectorContainer.setPort(inspectorPort);
    	inspectorPort.setName("IC5000Port");
    	inspectorPort.setRuntimeInspectorClass("fzi.mottem.runtime.rti.isystem.ISYSTEMAccessDriver");
    	inspectorPort.setTraceControllerClass("fzi.mottem.runtime.rti.isystem.ISYSTEMTraceDriver");
    	this.inspectorPort = inspectorPort;
		
		addGraphicalRepresentation(context, inspectorContainer);
		return new Object[] { inspectorContainer };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
		return addGraphicalRepresentationOfInspectorWithSpecificTextColor(context, BACKGROUNDCOLOR, WIDTH_CONTAINER, HEIGHT_CONTAINER,
				X_RECTANGLE, Y_RECTANGLE, WIDTH_RECTANGLE, HEIGHT_RECTANGLE, X_TEXT, Y_TEXT, WIDTH_TEXT, HEIGHT_TEXT,
				NAME, TEXTCOLOR, WIDTH_PORT, HEIGHT_PORT, this.inspectorPort);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof IC5000;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Saves the connection (business object) from IC5000 to a processor to delete it later 
	 * if the user wants to delete the IC5000.
	 */
	@Override
	public void delete(IDeleteContext context) {
		IC5000 inspectorContainer = (IC5000) getBusinessObjectForPictogramElement(context.getPictogramElement());
		this.inspectorConnector = inspectorContainer.getPort().getInspectorConnector();
		super.delete(context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Method will be called if user confirmed to delete the IC5000.
	 * Deletes the connection (business object) from IC5000 to a processor.
	 */
	@Override
	public void postDelete(IDeleteContext context) {
		if(this.inspectorConnector!=null){
			EcoreUtil.delete(this.inspectorConnector);
		}
		super.postDelete(context);
	}

	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_Inspector_IC5000;
	}

	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_Inspector_IC5000;
	}
	
	

}
