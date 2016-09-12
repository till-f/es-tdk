package patterns.swExecutor;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;

import patterns.AbstractComponentPattern;
import diagram.EditorImageProvider;
import fzi.mottem.model.testrigmodel.Processor;
import fzi.mottem.model.testrigmodel.ProcessorCore;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;

/**
 * The Class ProcessorCorePattern represents a ProcessorCore.
 * Created and added in the class ProcessorPattern.
 */
public class ProcessorCorePattern extends AbstractComponentPattern {
	
	/** The Constant WIDTH_SIZE. */
	private static final int WIDTH_SIZE = 15;
	
	/** The Constant HEIGHT_SIZE. */
	private static final int HEIGHT_SIZE = 15;
	
	/** The processor. */
	private Processor processor;
	
	/**
	 * Instantiates a new processor core pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public ProcessorCorePattern(IDiagramTypeProvider dtp) {
		this.dtp = dtp;
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo instanceof Processor;
	}


	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canCreate(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public boolean canCreate(ICreateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo instanceof Processor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateImageId()
	 */
	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_PROCESSORCORE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateLargeImageId()
	 */
	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_PROCESSORCORE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject) {
		return mainBusinessObject instanceof ProcessorCore;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return "Processor Core";
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context) {
		ProcessorCore pc = TestrigmodelFactory.eINSTANCE.createProcessorCore();
		String name = "";
		this.processor.getProcessorCores().add(pc);
		if( pc.getProcessor()!= null){
			if(pc.getProcessor().getProcessorCores() != null ){
				name = "Core" + (pc.getProcessor().getProcessorCores().size() - 1);
			}
		}
		pc.setName(name);
//		this.processorCore = pc;
		addGraphicalRepresentation(context, pc);
		return new Object[] { pc };
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public PictogramElement add(IAddContext context) {
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();
		ContainerShape containerShape = peCreateService.createContainerShape(
				context.getTargetContainer(), true);


		ProcessorCorePattern core = new ProcessorCorePattern(dtp);		
		Image imageCore = gaService.createImage(containerShape, core.getCreateImageId());
		gaService.setLocationAndSize(imageCore, context.getX(), context.getY(), WIDTH_SIZE, HEIGHT_SIZE);

		peCreateService.createChopboxAnchor(containerShape);
		link(containerShape, context.getNewObject());
		
		layoutPictogramElement(containerShape);
		
		return containerShape;
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canResizeShape(org.eclipse.graphiti.features.context.IResizeShapeContext)
	 */
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		return false;
	}

}
