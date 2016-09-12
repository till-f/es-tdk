package patterns.swExecutor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
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
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import patterns.AbstractComponentPattern;
//import util.ModelService;
import util.NumberOfCoresDialog;
import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.mottem.model.testrigmodel.UUT;

/**
 * The Class ProcessorPattern represents a processor. The business object will be created in the create-method.
 * The representation of the business object is realized in the add-method.
 * The user is asked after the number of processor cores of the processor. 
 * The asked number of processor cores will be created (business objects of ProcessorCore) in the create-method and
 * the graphical representation as icons of ProcessorCore added in the add-method.
 * A processor can be added to an UUT.
 */
public class ProcessorPattern extends AbstractComponentPattern {

	/** The Constant WIDTH_STARTVALUE. */
	private static final int WIDTH_STARTVALUE = 75;
	
	/** The Constant HEIGHT. */
	private static final int HEIGHT = 80;
	
	/** The Constant WIDTH_IMAGE. */
	private static final int WIDTH_IMAGE = 16;
	
	/** The Constant HEIGHT_IMAGE. */
	private static final int HEIGHT_IMAGE = 16;
	
	/** The Constant X_TEXT. */
	private static final int X_TEXT = 5;
	
	/** The Constant Y_TEXT. */
	private static final int Y_TEXT = 5;
	
	/** The Constant WIDTH_TEXT. */
	private static final int WIDTH_TEXT = 135;
	
	/** The Constant HEIGHT_TEXT. */
	private static final int HEIGHT_TEXT = 16;
	
	/** The Constant SPACING. */
	private static final int SPACING = 20;
	
	/** The Constant BACKGROUNDCOLOR of the shape. */
	private static final IColorConstant BACKGROUNDCOLOR = new ColorConstant(255, 255, 255);
	
	/** The cores. */
	private List<ProcessorCore> cores;
	
	/** The connector. */
	private IInspectorConnector connector;

	/**
	 * Instantiates a new processor pattern and sets the DiagramTypeProvider.
	 *
	 * @param dp the DiagramTypeProvider
	 */
	public ProcessorPattern(IDiagramTypeProvider dp) {
		this.dtp = dp;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 * The size of the graphical representation of Processor depends on the number of processor cores of the processor.
	 * The add-method also adds the graphical representation of the processor cores.
	 */
	@Override
	public PictogramElement add(IAddContext context) {
		INamed newEntity = (INamed) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(
				context.getTargetContainer(), true);

		int width = 0;
		int height = 0;
		if(cores.size()<16){
			width = WIDTH_STARTVALUE + ((cores.size()-1)/2)*20;
			height = HEIGHT;
		}
		else{
			width = WIDTH_STARTVALUE + ((cores.size()-3)/4)*20;
			height = HEIGHT + 50;
		}
		
		IGaService gaService = Graphiti.getGaService();
		Rectangle rectangle = gaService.createRectangle(containerShape);

		gaService.setLocationAndSize(rectangle, context.getX(), context.getY(),
				width, height);

		rectangle.setFilled(true);
		rectangle.setBackground(gaService.manageColor(getDiagram(),	BACKGROUNDCOLOR));

		Text text = gaService.createText(rectangle);
		text.setValue(newEntity.getName());
		gaService.setLocationAndSize(text, X_TEXT, Y_TEXT, WIDTH_TEXT, HEIGHT_TEXT);


		// direct editing
		getFeatureProvider().getDirectEditingInfo().setActive(true);

		peCreateService.createChopboxAnchor(containerShape);

		// links pictogram element and business object (Processor)
		link(containerShape, newEntity);

		if(cores.size() == 1){
			Shape coreShape = peCreateService.createShape(containerShape, true);
			ProcessorCorePattern corePattern = new ProcessorCorePattern(dtp);		
			//Shape of ProcessorCore = Icon of ProcessorCore
			Image coreImage = gaService.createImage(coreShape, corePattern.getCreateImageId());
			gaService.setLocationAndSize(coreImage, ((width/2)-8), (height/2), WIDTH_IMAGE, HEIGHT_IMAGE);

			peCreateService.createChopboxAnchor(coreShape);
			// links pictogram element and business object (ProcessorCore)
			link(coreShape, cores.get(0));
		}
		else if(cores.size() == 2){
			Shape coreShape = peCreateService.createShape(containerShape, true);
			ProcessorCorePattern corePattern = new ProcessorCorePattern(dtp);
			//Shape of ProcessorCore = Icon of ProcessorCore
			Image coreImage = gaService.createImage(coreShape, corePattern.getCreateImageId());
			gaService.setLocationAndSize(coreImage, ((width/2)-8), (height/2 - 15), WIDTH_IMAGE, HEIGHT_IMAGE);

			peCreateService.createChopboxAnchor(coreShape);
			// links pictogram element and business object (ProcessorCore)
			link(coreShape, cores.get(0));
			
			Shape coreShape2 = peCreateService.createShape(containerShape, true);
			ProcessorCorePattern corePattern2 = new ProcessorCorePattern(dtp);		
			//Shape of ProcessorCore = Icon of ProcessorCore
			Image imageCore2 = gaService.createImage(coreShape2, corePattern2.getCreateImageId());
			gaService.setLocationAndSize(imageCore2, ((width/2)-8), (height/2 + 5), WIDTH_IMAGE, HEIGHT_IMAGE);

			peCreateService.createChopboxAnchor(coreShape2);
			// links pictogram element and business object (ProcessorCore)
			link(coreShape2, cores.get(1));
		}
		else if(cores.size()<16){
			int x = 20;
			for (int i = 0; i < cores.size(); i++) {
				int mod = i % 2;
				Shape coreShape = peCreateService.createShape(containerShape, true);
				ProcessorCorePattern corePattern = new ProcessorCorePattern(dtp);		
				//Shape of ProcessorCore = Icon of ProcessorCore
				Image coreImage = gaService.createImage(coreShape, corePattern.getCreateImageId());
				gaService.setLocationAndSize(coreImage, x, 30 + mod*SPACING, WIDTH_IMAGE, HEIGHT_IMAGE);

				peCreateService.createChopboxAnchor(coreShape);
				// links pictogram element and business object (ProcessorCore)
				link(coreShape, cores.get(i));
				if( mod == 1 ){
					x = x + SPACING;
				}
			}
		}
		else{
			int x = 20;
			for (int i = 0; i < cores.size(); i++) {
				int mod = i % 4;
				Shape coreShape = peCreateService.createShape(containerShape, true);
				ProcessorCorePattern corePattern = new ProcessorCorePattern(dtp);		
				//Shape of ProcessorCore = Icon of ProcessorCore
				Image coreImage = gaService.createImage(coreShape, corePattern.getCreateImageId());
				gaService.setLocationAndSize(coreImage, x, 30 + mod*SPACING, WIDTH_IMAGE, HEIGHT_IMAGE);

				peCreateService.createChopboxAnchor(coreShape);
				// links pictogram element and business object (ProcessorCore)
				link(coreShape, cores.get(i));
				if( mod == 3 ){
					x = x + SPACING;
				}
			}
		}

		layoutPictogramElement(containerShape);
		return containerShape;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 * The create-method also creates the business objects of the processor cores.
	 */
	@Override
	public Object[] create(ICreateContext context) {
		cores = new ArrayList<>();
		//creates business object of Processor
		Processor processor = TestrigmodelFactory.eINSTANCE.createProcessor();
		processor.setName("processor");
		final Object target = getBusinessObjectForPictogramElement(context.getTargetContainer());
		if (target instanceof UUT) {
			UUT domainObject = (UUT) target;
			domainObject.getProcessors().add(processor);
		}

		int number = 0;
		// dialog for the number of processor cores of the processor
		NumberOfCoresDialog dialog = new NumberOfCoresDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		dialog.create();
		if (dialog.open() == Window.OK) {
			try {
				number = Integer.parseInt(dialog.getNumber());
			} catch (NumberFormatException e) {
			}
		} 
		for (int i = 0; i < number; i++) {
			//creates business object of ProcessorCore
			ProcessorCore processorCore = TestrigmodelFactory.eINSTANCE.createProcessorCore();
			processor.getProcessorCores().add(processorCore);
			String name = "";
			if( processorCore.getProcessor()!= null){
				if(processorCore.getProcessor().getProcessorCores() != null ){
					name = "Core" + (processorCore.getProcessor().getProcessorCores().size() - 1);
				}
			}
			processorCore.setName(name);
			cores.add(processorCore);
		}

		addGraphicalRepresentation(context, processor);
		return new Object[] { processor };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return "Processor";
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo instanceof UUT;
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canCreate(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public boolean canCreate(ICreateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo instanceof UUT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof Processor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canDirectEdit(org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		GraphicsAlgorithm graphicsAlgorithm = context.getGraphicsAlgorithm();
		return businessObject instanceof INamed && graphicsAlgorithm instanceof Text;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getInitialValue(org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		INamed entity = (INamed) getBusinessObjectForPictogramElement(pe);
		return entity.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#checkValueValid(java.lang.String, org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		// null = valid value
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#setValue(java.lang.String, org.eclipse.graphiti.features.context.IDirectEditingContext)
	 */
	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		INamed iNamed = (INamed) getBusinessObjectForPictogramElement(pe);
		iNamed.setName(value);
		
		// text with the name = first children (get(0))
		if (null != pe.getGraphicsAlgorithm().getGraphicsAlgorithmChildren()
				.get(0)
				&& pe.getGraphicsAlgorithm().getGraphicsAlgorithmChildren()
						.get(0) instanceof Text) {
			Text text = (Text) pe.getGraphicsAlgorithm()
					.getGraphicsAlgorithmChildren().get(0);
			text.setValue(iNamed.getName());
		}
		updatePictogramElement(context.getPictogramElement());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getEditingType()
	 */
	public int getEditingType() {
		return TYPE_TEXT;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateImageId()
	 */
	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_PROCESSOR;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateLargeImageId()
	 */
	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_PROCESSOR;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Saves the connection (business object) from inspector to the processor to delete it later if the user wants to delete the processor.
	 */
	@Override
	public void delete(IDeleteContext context) {
		Processor processor = (Processor) getBusinessObjectForPictogramElement(context.getPictogramElement());
		this.connector = processor.getInspectorConnector();
		super.delete(context);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 * Method will be called if user confirmed to delete the processor.
	 * Deletes the connection (business object) from inspector to the processor.
	 */
	@Override
	public void postDelete(IDeleteContext context) {
		if(this.connector!=null){
			EcoreUtil.delete(this.connector);
		}
		super.postDelete(context);
	}

}
