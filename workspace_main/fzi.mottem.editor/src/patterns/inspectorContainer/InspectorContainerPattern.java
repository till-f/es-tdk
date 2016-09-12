package patterns.inspectorContainer;

import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

import patterns.AbstractComponentPattern;
import diagram.EditorImageProvider;
import fzi.mottem.model.baseelements.IInspector;
import fzi.mottem.model.baseelements.INamed;

/**
 * The Class InspectorContainerPattern is the superclass for the concrete inspector container classes.
 * It implements some methods, which can be used for all subclasses.
 */
public abstract class InspectorContainerPattern extends AbstractComponentPattern {

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateImageId()
		 */
		@Override
		public String getCreateImageId() {
			return EditorImageProvider.IMG_Inspector;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.pattern.AbstractPattern#getCreateLargeImageId()
		 */
		@Override
		public String getCreateLargeImageId() {
			return EditorImageProvider.IMG_Inspector;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.graphiti.pattern.AbstractPattern#canResizeShape(org.eclipse.graphiti.features.context.IResizeShapeContext)
		 */
		@Override
		public boolean canResizeShape(IResizeShapeContext context) {
			return false;
		}
		
		/**
		 * Adds the graphical representation of inspector.
		 *
		 * @param context the context
		 * @param backgroundColor the background color
		 * @param width the width
		 * @param height the height
		 * @param xRectangle the x rectangle
		 * @param yRectangle the y rectangle
		 * @param widthRectangle the width rectangle
		 * @param heigthRectangle the heigth rectangle
		 * @param xText the x text
		 * @param yText the y text
		 * @param widthText the width text
		 * @param heightText the height text
		 * @param textContent the text content
		 * @param widthPort the width port
		 * @param heightPort the height port
		 * @param inspectorPort the inspector port
		 * @return the pictogram element
		 */
		protected PictogramElement addGraphicalRepresentationOfInspector(IAddContext context,
				IColorConstant backgroundColor, int width, int height, int xRectangle, int yRectangle,
				int widthRectangle, int heigthRectangle, int xText, int yText, int widthText, int heightText,
				String textContent, int widthPort, int heightPort, IInspector inspectorPort){
			
			INamed newEntity = (INamed) context.getNewObject();
			IPeCreateService peCreateService = Graphiti.getPeCreateService();
			ContainerShape containerShape = peCreateService.createContainerShape(
					context.getTargetContainer(), true);

			IGaService gaService = Graphiti.getGaService();
			Rectangle invisible = gaService.createInvisibleRectangle(containerShape);
			gaService.setLocationAndSize(invisible, context.getX(), context.getY(), width, height);
			Rectangle rectangle = gaService.createRectangle(invisible);
			
			gaService.setLocationAndSize(rectangle, xRectangle, yRectangle, widthRectangle, heigthRectangle);
			rectangle.setFilled(true);
			rectangle.setBackground(gaService.manageColor(getDiagram(), backgroundColor));
			peCreateService.createChopboxAnchor(containerShape);
			
			Text text = gaService.createText(rectangle);
			text.setValue(textContent);
			gaService.setLocationAndSize(text, xText, yText, widthText, heightText);
			
			Shape portShape = peCreateService.createShape(containerShape, true);

			Rectangle portRectangle = gaService.createRectangle(portShape);
			
			gaService.setLocationAndSize(portRectangle, (width/2 - widthPort/2), heigthRectangle, widthPort, heightPort);
			portRectangle.setFilled(true);
			portRectangle.setBackground(gaService.manageColor(getDiagram(), IColorConstant.BLACK));

			peCreateService.createChopboxAnchor(portShape);
			link(portShape, inspectorPort);
			link(containerShape, newEntity);
			layoutPictogramElement(containerShape);
			return containerShape;
		}

		/**
		 * Adds the graphical representation of inspector with specific text color.
		 *
		 * @param context the context
		 * @param backgroundColor the background color
		 * @param width the width
		 * @param height the height
		 * @param xRectangle the x rectangle
		 * @param yRectangle the y rectangle
		 * @param widthRectangle the width rectangle
		 * @param heigthRectangle the heigth rectangle
		 * @param xText the x text
		 * @param yText the y text
		 * @param widthText the width text
		 * @param heightText the height text
		 * @param textContent the text content
 		 * @param textColor the text color
		 * @param widthPort the width port
		 * @param heightPort the height port
		 * @param inspectorPort the inspector port
		 * @return the pictogram element
		 */
		protected PictogramElement addGraphicalRepresentationOfInspectorWithSpecificTextColor(IAddContext context,
				IColorConstant backgroundColor, int width, int height, int xRectangle, int yRectangle,
				int widthRectangle, int heigthRectangle, int xText, int yText, int widthText, int heightText,
				String textContent, IColorConstant textColor, int widthPort, int heightPort, IInspector inspectorPort){
			
			INamed newEntity = (INamed) context.getNewObject();
			IPeCreateService peCreateService = Graphiti.getPeCreateService();
			ContainerShape containerShape = peCreateService.createContainerShape(
					context.getTargetContainer(), true);

			IGaService gaService = Graphiti.getGaService();
			Rectangle invisible = gaService.createInvisibleRectangle(containerShape);
			gaService.setLocationAndSize(invisible, context.getX(), context.getY(), width, height);
			Rectangle rectangle = gaService.createRectangle(invisible);
			
			gaService.setLocationAndSize(rectangle, xRectangle, yRectangle, widthRectangle, heigthRectangle);
			rectangle.setFilled(true);
			rectangle.setBackground(gaService.manageColor(getDiagram(), backgroundColor));
			peCreateService.createChopboxAnchor(containerShape);
			
			Text text = gaService.createText(rectangle);
			text.setValue(textContent);
			text.setForeground(manageColor(textColor));
			gaService.setLocationAndSize(text, xText, yText, widthText, heightText);
			
			Shape portShape = peCreateService.createShape(containerShape, true);

			Rectangle portRectangle = gaService.createRectangle(portShape);
			
			gaService.setLocationAndSize(portRectangle, (width/2 - widthPort/2), heigthRectangle, widthPort, heightPort);
			portRectangle.setFilled(true);
			portRectangle.setBackground(gaService.manageColor(getDiagram(), IColorConstant.BLACK));

			peCreateService.createChopboxAnchor(portShape);
			link(portShape, inspectorPort);
			link(containerShape, newEntity);
			layoutPictogramElement(containerShape);
			return containerShape;
		}

		
}
