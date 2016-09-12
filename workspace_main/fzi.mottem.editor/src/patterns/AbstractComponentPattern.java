package patterns;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.pattern.AbstractPattern;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import fzi.mottem.model.baseelements.INamed;

/**
 * The Class AbstractComponentPattern has some methods to add graphical representations for pattern classes.
 * There are also other methods, which maybe have not to be overwritten in pattern sub classes. 
 */
public abstract class AbstractComponentPattern extends AbstractPattern {

	/** The DiagramTypeProvider. */
	protected IDiagramTypeProvider dtp;
	
	/**
	 * Adds the graphical representation with the form of a rectangle.
	 *
	 * @param context the add context
	 * @param backgroundColor the background color
	 * @param width the width
	 * @param height the height
	 * @return the pictogram element
	 */
	protected PictogramElement addGraphicalRepresentation(IAddContext context, IColorConstant backgroundColor,
			int width, int height){
		
		INamed newEntity = (INamed) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(
				context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		
		drawRectangle(context, backgroundColor, width,
				height, containerShape, gaService);

		peCreateService.createChopboxAnchor(containerShape);

		// links pictogram element and business object
		link(containerShape, newEntity);
		layoutPictogramElement(containerShape);
		return containerShape;
		
	}
	
	/**
	 * Adds the graphical representation with the form of a rectangle with an image.
	 *
	 * @param context the add context
	 * @param backgroundColor the background color
	 * @param width the width
	 * @param height the height
	 * @param xImage the x image
	 * @param yImage the y image
	 * @param widthImage the width image
	 * @param heigthImage the heigth image
	 * @return the pictogram element
	 */
	protected PictogramElement addGraphicalRepresentationWithImage(IAddContext context, IColorConstant backgroundColor,
			int width, int height, int xImage, int yImage, int widthImage, int heigthImage ){
		
		INamed newEntity = (INamed) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(
				context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		
		// draws the rectangle for the graphical representation
		Rectangle rectangle = drawRectangle(context, backgroundColor, width,
				height, containerShape, gaService);

		Image image = gaService.createImage(rectangle, this.getCreateImageId());
		gaService.setLocationAndSize(image, xImage, yImage, widthImage, heigthImage);

		peCreateService.createChopboxAnchor(containerShape);

		// links pictogram element and business object
		link(containerShape, newEntity);
		layoutPictogramElement(containerShape);
		return containerShape;
		
	}
	
	/**
	 * Adds the graphical representation with form of a rectangle with an image and a text field.
	 *
	 * @param context the add context
	 * @param backgroundColor the background color
	 * @param width the width
	 * @param height the height
	 * @param xImage the x image
	 * @param yImage the y image
	 * @param widthImage the width image
	 * @param heigthImage the heigth image
	 * @param textContent the text content
	 * @param xText the x text
	 * @param yText the y text
	 * @param widthText the width text
	 * @param heightText the height text
	 * @return the pictogram element
	 */
	protected PictogramElement addGraphicalRepresentationWithImageAndText(IAddContext context, IColorConstant backgroundColor,
			int width, int height, int xImage, int yImage, int widthImage, int heigthImage,
			String textContent, int xText, int yText, int widthText, int heightText ){
		INamed newEntity = (INamed) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(
				context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		// draws the rectangle for the graphical representation
		Rectangle rectangle = drawRectangle(context, backgroundColor, width, height, containerShape, gaService);

		rectangle.setFilled(true);
		rectangle.setBackground(gaService.manageColor(getDiagram(), backgroundColor));

		Image image = gaService.createImage(rectangle, this.getCreateImageId());
		gaService.setLocationAndSize(image, xImage, yImage, widthImage, heightText);

		Text text = gaService.createText(rectangle);
		text.setValue(textContent);
		gaService.setLocationAndSize(text, xText, yText, widthText, heightText);

		// direct editing
		getFeatureProvider().getDirectEditingInfo().setActive(true);

		peCreateService.createChopboxAnchor(containerShape);

		// links pictogram element and business object
		link(containerShape, newEntity);
		layoutPictogramElement(containerShape);
		return containerShape;
	}

	/**
	 * Adds the graphical representation with form of a rectangle with an image and a text field.
	 *
	 * @param context the add context
	 * @param backgroundColor the background color
	 * @param width the width
	 * @param height the height
	 * @param xImage the x image
	 * @param yImage the y image
	 * @param widthImage the width image
	 * @param heigthImage the heigth image
	 * @param textContent the text content
	 * @param textColor the text color
	 * @param xText the x text
	 * @param yText the y text
	 * @param widthText the width text
	 * @param heightText the height text
	 * @return the pictogram element
	 */
	protected PictogramElement addGraphicalRepresentationWithImageAndTextWithSpecificTextColor(
			IAddContext context, IColorConstant backgroundColor, int width, int height, int xImage, 
			int yImage, int widthImage, int heigthImage, String textContent, IColorConstant textColor,
			int xText, int yText, int widthText, int heightText ){
		INamed newEntity = (INamed) context.getNewObject();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(
				context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		// draws the rectangle for the graphical representation
		Rectangle rectangle = drawRectangle(context, backgroundColor, width, height, containerShape, gaService);

		rectangle.setFilled(true);
		rectangle.setBackground(gaService.manageColor(getDiagram(), backgroundColor));

		Image image = gaService.createImage(rectangle, this.getCreateImageId());
		gaService.setLocationAndSize(image, xImage, yImage, widthImage, heightText);

		Text text = gaService.createText(rectangle);
		text.setValue(textContent);
		text.setForeground(manageColor(textColor));
		gaService.setLocationAndSize(text, xText, yText, widthText, heightText);

		// direct editing
		getFeatureProvider().getDirectEditingInfo().setActive(true);

		peCreateService.createChopboxAnchor(containerShape);

		// links pictogram element and business object
		link(containerShape, newEntity);
		layoutPictogramElement(containerShape);
		return containerShape;
	}
	
	/**
	 * Helper method to draw the rectangle.
	 *
	 * @param context the add context
	 * @param backgroundColor the background color
	 * @param width the width
	 * @param height the height
	 * @param containerShape the container shape
	 * @param gaService the ga service
	 * @return the rectangle
	 */
	private Rectangle drawRectangle(IAddContext context,
			IColorConstant backgroundColor, int width, int height,
			ContainerShape containerShape, IGaService gaService) {
		Rectangle rectangle = gaService.createRectangle(containerShape);

		gaService.setLocationAndSize(rectangle, context.getX(), context.getY(),
				width, height);

		rectangle.setFilled(true);
		rectangle.setBackground(gaService.manageColor(getDiagram(),
				backgroundColor));
		return rectangle;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractBasePattern#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	@Override
	public boolean canAdd(IAddContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#canCreate(org.eclipse.graphiti.features.context.ICreateContext)
	 */
	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isPatternControlled(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean isPatternControlled(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.pattern.AbstractPattern#isPatternRoot(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean isPatternRoot(PictogramElement pictogramElement) {
		Object businessObject = getBusinessObjectForPictogramElement(pictogramElement);
		return isMainBusinessObjectApplicable(businessObject);
	}
	
}