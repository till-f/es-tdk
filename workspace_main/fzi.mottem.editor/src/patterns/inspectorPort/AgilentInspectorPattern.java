package patterns.inspectorPort;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import patterns.AbstractComponentPattern;
import fzi.mottem.model.baseelements.IInspectorConnector;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.testrigmodel.AgilentInspector;
import fzi.mottem.model.testrigmodel.AgilentInspectorContainer;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;

public class AgilentInspectorPattern extends AbstractComponentPattern
{
	
	/** The connector. */
	private IInspectorConnector connector; 

	public AgilentInspectorPattern(IDiagramTypeProvider dtp)
	{
		this.dtp = dtp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isMainBusinessObjectApplicable(java.lang.Object)
	 */
	@Override
	public boolean isMainBusinessObjectApplicable(Object mainBusinessObject)
	{
		return mainBusinessObject instanceof AgilentInspector;
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#isPatternControlled(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement)
	{
		Object domainObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(domainObject);
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#isPatternRoot(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement)
	{
		Object domainObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(domainObject);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context)
	{
		AgilentInspector inspector = TestrigmodelFactory.eINSTANCE.createAgilentInspector();
		inspector.setName("OszilloscopePin");
		inspector.setRuntimeInspectorClass("fzi.mottem.runtime.rti.keysight.IVIAccessDriver");
		inspector.setTraceControllerClass("fzi.mottem.runtime.rti.keysight.IVITraceDriver");
		Object container = getBusinessObjectForPictogramElement(context.getTargetContainer());
		if (container instanceof AgilentInspectorContainer)
		{
			((AgilentInspectorContainer) container).getAgilentPins().add(inspector);
		}
		addGraphicalRepresentation(context, inspector);

		return new Object[] { inspector };
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context)
	{
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo instanceof AgilentInspectorContainer;
	}

	/* (non-Javadoc)
	 * @see util.AbstractComponentPattern#canCreate(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public boolean canCreate(ICreateContext context)
	{
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		
		if (bo instanceof AgilentInspectorContainer)
		{
			AgilentInspectorContainer container = (AgilentInspectorContainer) bo;
			
			if (container.getAgilentPins().size() > 0)
			{
				return false;
			}
			else
			{
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
		INamed newEntity = (INamed) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		
		// draws the rectangle for the graphical representation
		Rectangle rectangle = gaService.createRectangle(containerShape);

		gaService.setLocationAndSize(rectangle, context.getX(), context.getY(), 10, 15);

		rectangle.setFilled(true);
		rectangle.setBackground(gaService.manageColor(getDiagram(), IColorConstant.BLACK));

		peCreateService.createChopboxAnchor(containerShape);

		// links pictogram element and business object
		link(containerShape, newEntity);
		layoutPictogramElement(containerShape);
		return containerShape;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName()
	{
		return "OscilloscopePin";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#delete(org.eclipse.graphiti.features.context.IDeleteContext)
	 */
	@Override
	public void delete(IDeleteContext context)
	{
		AgilentInspector inspector = (AgilentInspector) getBusinessObjectForPictogramElement(context.getPictogramElement());
		this.connector = inspector.getInspectorConnector();
		super.delete(context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#postDelete(org.eclipse.graphiti.features.context.IDeleteContext)
	 */
	@Override
	public void postDelete(IDeleteContext context)
	{
		if (this.connector != null)
		{
			EcoreUtil.delete(this.connector);
		}
		super.postDelete(context);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canMoveShape(org.eclipse.graphiti.features.context.IMoveShapeContext)
	 */
	@Override
	public boolean canMoveShape(IMoveShapeContext context)
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canResizeShape(org.eclipse.graphiti.features.context.IResizeShapeContext)
	 */
	@Override
	public boolean canResizeShape(IResizeShapeContext context)
	{
		return false;
	}
}
