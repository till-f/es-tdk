package patterns;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.INamed;
import fzi.mottem.model.testrigmodel.IOPin;
import fzi.mottem.model.testrigmodel.PinSignal;
import fzi.mottem.model.testrigmodel.TestrigmodelFactory;
import fzi.mottem.model.testrigmodel.UUT;

/**
 * The Class IOPinPattern represents an I/O-Pin. 
 * The business object will be created in the create-method.
 * The representation of the business object is added in the add-method.
 * A pin can be added to an UUT.
 */
public class IOPinPattern extends AbstractComponentPattern {

	private static final int CONTAINER_W = 80;
	private static final int CONTAINER_H = 15;
	
	private static final int IMAGE_WIDTH = 13;
	
	private static final int IMAGE_HEIGHT = 15;
	
	private static final int TEXT_WIDTH = 80;

	private static final IColorConstant TEXTCOLOR = new ColorConstant(0, 0, 0);

	public static final String INITIAL_NAME = "Pin";
	
	/**
	 * Instantiates a new IO pin pattern and sets the DiagramTypeProvider.
	 *
	 * @param dtp the DiagramTypeProvider
	 */
	public IOPinPattern(IDiagramTypeProvider dtp) {
		this.dtp = dtp;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#create(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public Object[] create(ICreateContext context) {
		//creates a business object of IOPin.
		IOPin ioPin = TestrigmodelFactory.eINSTANCE.createIOPin();
		ioPin.setName(INITIAL_NAME);
        final Object targetBusinessObject = getBusinessObjectForPictogramElement(context.getTargetContainer());
        if (targetBusinessObject instanceof UUT)
        {
			UUT uut = (UUT) targetBusinessObject;
			uut.getIoPins().add(ioPin);
			//ioPin.setUut(uut);
			PinSignal defaultSignal = TestrigmodelFactory.eINSTANCE.createPinSignal();
			defaultSignal.setName("Value");
			ioPin.getPinSignals().add(defaultSignal);
        }

		addGraphicalRepresentation(context, ioPin);
		return new Object[] { ioPin };
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
		gaService.setLocationAndSize(invisible, context.getX(), context.getY(), CONTAINER_W, CONTAINER_H);

		Image image = gaService.createImage(invisible, this.getCreateImageId());
		gaService.setLocationAndSize(image, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

		// an anchor box allows the element to be a target or source of connections
		peCreateService.createChopboxAnchor(containerShape);

		link(containerShape, newEntity);

		Shape shape = peCreateService.createShape(containerShape, false);
		Text txtName = gaService.createText(shape, INITIAL_NAME);
		txtName.setForeground(manageColor(TEXTCOLOR));
		txtName.setHorizontalAlignment(Orientation.ALIGNMENT_LEFT);
		txtName.setVerticalAlignment(Orientation.ALIGNMENT_MIDDLE);
		gaService.setLocationAndSize(txtName,  IMAGE_WIDTH + 5, 0, TEXT_WIDTH, IMAGE_HEIGHT);


		layoutPictogramElement(containerShape);
		return containerShape;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateName()
	 */
	@Override
	public String getCreateName() {
		return INITIAL_NAME;
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
		return mainBusinessObject instanceof IOPin;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateImageId()
	 */
	@Override
	public String getCreateImageId() {
		return EditorImageProvider.IMG_IOPIN;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateLargeImageId()
	 */
	@Override
	public String getCreateLargeImageId() {
		return EditorImageProvider.IMG_IOPIN;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canResizeShape(org.eclipse.graphiti.features.context.IResizeShapeContext)
	 */
	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		return false;
	}

}
