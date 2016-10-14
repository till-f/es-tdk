package fzi.mottem.runtime.rtgraph;

import java.util.List;

import org.csstudio.swt.widgets.figures.AbstractMarkedWidgetFigure;
import org.csstudio.swt.widgets.figures.GaugeFigure;
import org.csstudio.swt.widgets.figures.MeterFigure;
import org.csstudio.swt.widgets.figures.ProgressBarFigure;
import org.csstudio.swt.widgets.figures.TankFigure;
import org.csstudio.swt.widgets.figures.ThermometerFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import fzi.mottem.runtime.dataexchanger.DEMessage;
import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;
import fzi.mottem.runtime.rtgraph.views.Dashboard;
import fzi.mottem.runtime.rtgraph.views.DashboardComposite;
import fzi.mottem.runtime.rtgraph.views.ImageFigure;
import fzi.mottem.runtime.rtgraph.views.TextFigure;

/**
 * This class implements the connection between the GUI widgets and the
 * DataExchanger.
 * 
 * It contains references to primary GUI elements like the widget Figure, the
 * figure container (canvas), the canvas container (the Widgets View), the
 * draw2d LightWeightSystem and a FigureRepresentation object which contains
 * serializeable GUI-relevant information.
 * 
 * The DataExchanger sends (asynchronous) data to the ExchangeLink, while the
 * GUI cyclically checks if this data has been changed.
 * 
 * @author Kalin Katev
 *
 */

public class IndicatorWidgetLink extends AbstractWidgetExchangeLink {

	private String name;
	Text canvas_name;

	private boolean focused;
	private Image image;

	public Dashboard getCanvasContainer() {
		return dashboard;
	}

	public void setCanvasContainer(DashboardComposite widgetsView) {
		this.dashboard = widgetsView;
	}

	public DashboardComposite getDashboard() {
		return dashboard;
	}

	public void setDashboard(DashboardComposite dashboard) {
		this.dashboard = dashboard;
	}

	/*
	 * Open question - should the latest Signal and time stamp be saved in
	 * private variables with corresponding getter methods?
	 */
	private IndicatorWidgetLink(AbstractMarkedWidgetFigure figure, DashboardComposite widgetsContainer) {
		this.dashboard = widgetsContainer;
		this.figure = figure;
		this.name = figure.getClass().getSimpleName();
		this.widgetType = WidgetType.W_INDICATOR;
		canvas = new Canvas(widgetsContainer, SWT.None);
		this.figure.setTransparent(true);
		this.figure.setValueLabelFormat("0.##");
		lws = new LightweightSystem(canvas);
		lws.setContents(figure);

		canvas_name = new Text(canvas, SWT.BORDER);
		canvas_name.setText(simpleName);

		representation = new WidgetRepresentation();
		representation.setWidgetType(widgetType);
	}

	public void setFocused(boolean focus) {
		this.focused = focus;
	}

	public boolean isFocused() {
		return focused;
	}

	public void updateCanvasImage() {
		/*
		 * this.image = canvas.getParent().getBackgroundImage(); if (image !=
		 * null && !image.isDisposed()) { org.eclipse.swt.graphics.Rectangle
		 * bounds = canvas.getBounds(); Point location = canvas.getLocation();
		 * 
		 * Image newImg = cropImage(image, location.x, location.y,
		 * bounds.height, bounds.width); canvas.setBackgroundImage(newImg); }
		 */
		if (getType() == Constants.WIDGET_IMAGE) {
			if (this.image != null && !this.image.isDisposed()) {
				org.eclipse.swt.graphics.Rectangle bounds = canvas.getBounds();
				Image newImg = imageScale(image, bounds.width, bounds.height);
				((ImageFigure) figure).setImage(newImg);
			}
		}
	}

	public void updateCanvasImage(Image img) {
		this.image = img;
		updateCanvasImage();
	}

	private Image imageScale(Image image, int width, int height) {
		ImageData data = image.getImageData();
		if (data.width == width && data.height == height)
			return image;
		data = data.scaledTo(width, height);
		Image scaled = new Image(Display.getDefault(), data);
		return scaled;
	}

	public IndicatorWidgetLink(AbstractMarkedWidgetFigure figure, DashboardComposite widgetsContainer,
			WidgetRepresentation representation) {
		this(figure, widgetsContainer);
		this.representation = representation;
		representation.setWidgetType(widgetType);
		// this.representation.setHeight(figure.getPreferredSize().height);
		// this.representation.setWidth(figure.getPreferredSize().width);
		applyRepresentation(true, true, true);
	}

	@Override
	public Signal getSignal() {
		return DataExchanger.getSignal(signalUID);
	}

	public void setSignalUID(String signalUID) {
		this.signalUID = signalUID;
		if (getType() != Constants.WIDGET_TEXT && representation.getText().length() < 1) {
			canvas.setToolTipText(simpleName + "\n" + signalUID);
		}
	}

	public void setSignalSimpleName(String simpleName) {
		this.simpleName = simpleName;
		/*
		 * if (getType() != Constants.WIDGET_TEXT &&
		 * representation.getText().length() < 1) {
		 * canvas.setToolTipText(this.simpleName + "\n" + signalUID); }
		 */
		// content.setText(simpleName + "\n" + signalUID);
		// setText(simpleName);
		// updateRepresentation(true, false, false);
	}

	public int getType() {

		return getFigureType(this.figure);
	}

	public String getName() {
		return name;
	}

	@Override
	public void setText(String text) {

		System.out.println("Widget: Settings text to " + text + " " + getType());

		String oldtext = new String(this.text);
		this.text = text;
		representation.setText(text);
		// updateRepresentation(true, false, false);
		if (getType() == Constants.WIDGET_TEXT) {
			if (text.length() > 0) {
				((TextFigure) figure).setText(text);
			} else {
				((TextFigure) figure).setText(this.simpleName + "\n" + signalUID);
			}
			canvas.setToolTipText(this.simpleName + "\n" + signalUID);
		} else if (getType() == Constants.WIDGET_IMAGE) {

			if (text.length() > 0 && !oldtext.equals(text)) {

				setWidgetImage(text);

				EclipseFileSystemHelper helper = new EclipseFileSystemHelper();

				String img_result = text;

				if (helper.fileIsInWorkspace(text)) {
					if (helper.isAbsolutePath(text)) {
						img_result = text;
						canvas.setToolTipText(helper.getWorkspaceRelativeFromAbsolute(text));
						representation.setText(helper.getWorkspaceRelativeFromAbsolute(text));
					} else {
						img_result = helper.getAbsoluteFromWorkspaceRelative(text);
						canvas.setToolTipText(text);
						representation.setText(text);
					}
				}

				try {

					ImageData imgData = new ImageData(img_result);
					this.image = new Image(Display.getCurrent(), imgData);
					canvas.setBounds(representation.getX(), representation.getY(), representation.getWidth(),
							representation.getHeight());
					updateCanvasImage();

				} catch (SWTException e) {
					String result = dashboard.callImageDialog();

					if (result != null) {

						// result is absolute
						String img_path = null;
						if (helper.fileIsInWorkspace(result)) {
							img_path = helper.getWorkspaceRelativeFromAbsolute(result);
						} else {
							img_path = result;
						}

						// force image dimension when loading it for the first
						// time
						ImageData imgData = new ImageData(result);
						this.image = new Image(Display.getCurrent(), imgData);

						getRepresentation().setHeight(imgData.height);
						getRepresentation().setWidth(imgData.width);
						getRepresentation().setText(img_path);
						canvas.setToolTipText(img_path);

						canvas.setBounds(representation.getX(), representation.getY(), representation.getWidth(),
								representation.getHeight());

						updateCanvasImage();

					} else {
						canvas.setBounds(representation.getX(), representation.getY(), representation.getWidth(),
								representation.getHeight());
						canvas.setToolTipText("Image not found!");
					}
				}
			}
		} else {
			if (text.length() > 0) {
				canvas.setToolTipText(text);
			} else {
				canvas.setToolTipText(this.simpleName + "\n" + signalUID);
			}
		}

	}

	private void setWidgetImage(String path) {
		EclipseFileSystemHelper helper = new EclipseFileSystemHelper();
		String absolutePath = null;
		String representationImagePath = null;
		boolean imageInWorkspace = helper.fileIsInWorkspace(path);
		boolean isAbsolutePath = helper.isAbsolutePath(path);

		/*
		 * if the image is in the workspace and the path is absolute then reduce
		 * to workspace relative path if the path is relative then find the
		 * absolute path
		 * 
		 * If the image is not in the workspace then just use the absolute path
		 */
		if (imageInWorkspace) {
			if (isAbsolutePath) {
				absolutePath = path;
				representationImagePath = helper.getWorkspaceRelativeFromAbsolute(path);
			} else {
				absolutePath = helper.getAbsoluteFromWorkspaceRelative(path);
				representationImagePath = path;
			}
		} else {
			absolutePath = path;
			representationImagePath = path;
		}
		
		try { //try to get the image
			
			ImageData imgData = new ImageData(absolutePath);
			this.image = new Image(Display.getCurrent(), imgData);
			canvas.setBounds(representation.getX(), representation.getY(), representation.getWidth(),
					representation.getHeight());
			representation.setHeight(imgData.height);
			representation.setWidth(imgData.width);
			representation.setText(absolutePath);
			updateCanvasImage();
			
		} catch (SWTException e) { //if it fails, call a browse dialog and try again
			
			String result = dashboard.callImageDialog();

			if (result != null) {
				if (helper.fileIsInWorkspace(result)) {
					representationImagePath = helper.getWorkspaceRelativeFromAbsolute(result);
					absolutePath = result;
				} else {
					absolutePath = result;
					representationImagePath = result;
				}
				
				ImageData imgData = new ImageData(absolutePath);
				this.image = new Image(Display.getCurrent(), imgData);
				
				representation.setHeight(imgData.height);
				representation.setWidth(imgData.width);
				representation.setText(absolutePath);
				
			} else {
				canvas.setBounds(representation.getX(), representation.getY(), representation.getWidth(),
						representation.getHeight());
				canvas.setToolTipText("Image not found!");
			}
			
		}

	}

	public void setTextFont(Font f) {
		if (getFigureType(figure) == Constants.WIDGET_TEXT) {
			((TextFigure) figure).setTextFont(f);
		}
	}

	public void setValueFont(Font fvalue) {
		if (getFigureType(figure) == Constants.WIDGET_TEXT) {
			((TextFigure) figure).setValueFont(fvalue);
		}
	}

	public String getText() {
		return text;
	}

	public void refresh() {
		figure.repaint();
	}

	public void updateFigure() {
		this.figure.setValue(value);
		updated = false;
	}

	public void setLogarithmic(boolean logarithmic_scale) {
		figure.setLogScale(logarithmic_scale);
	}

	public void setFigureLo(double low) {
		figure.setLoLevel(low);
		figure.setLoloLevel((low + figure.getRange().getLower()) / 2);
	}

	public void setFigureHi(double high) {
		figure.setHiLevel(high);
		figure.setHihiLevel((high + figure.getRange().getUpper()) / 2);
	}

	/**
	 * Return the minimum widget data boundary
	 * 
	 * @return data saved in the representation
	 */
	public double getMin() {
		return representation.getRangeMin();
	}

	/**
	 * Return the maximum widget data boundary
	 * 
	 * @return data saved in the representation
	 */
	public double getMax() {
		return representation.getRangeMax();
	}

	/**
	 * Return the low widget data boundary (only UI relevant)
	 * 
	 * @return data saved in the representation
	 */
	public double getLo() {
		return representation.getRangeLow();
	}

	/**
	 * Return the high widget data boundary (only UI relevant)
	 * 
	 * @return data saved in the representation
	 */
	public double getHi() {
		return representation.getRangeHigh();
	}

	/**
	 * This method applies the current FigureRepresentation on the figure and
	 * canvas elements of this link. Please see the FigureRepresentation java
	 * doc in order to see what options are set. The WidgetsView container will
	 * be refreshed after this operation.
	 * 
	 * @param applyFigure
	 *            if true, then the Figure options will be applied.
	 * @param applyCanvas
	 *            if true, then the Canvas options will be applied.
	 */
	public void applyRepresentation(boolean applySignal, boolean applyFigure, boolean applyCanvas) {

		if (applySignal) {
			signal = DataExchanger.getSignal(representation.getSignalUID());
			if (signal != null) {
				this.signalUID = signal.getId();
				setSignalSimpleName(signal.getSimpleName());
			}
		}

		String reptext = new String(representation.getText());
		System.out.println("Widget Consumer: Applying represetation, text = " + reptext);

		if (applyCanvas) {
			canvas.setBounds(representation.getX(), representation.getY(), representation.getWidth(),
					representation.getHeight());
		}

		if (applyFigure) {
			figure.setRange(representation.getRangeMin(), representation.getRangeMax());
			setFigureHi(representation.getRangeHigh());
			setFigureLo(representation.getRangeLow());
			setText(reptext);
			figure.setLogScale(representation.isLogarithmic());
			figure.setValueLabelFormat(representation.getNumberFormat());
		}

		dashboard.layout(true);
		if (DataExchanger.getSignal(signalUID) != null) {
			simpleName = DataExchanger.getSignal(signalUID).getSimpleName();
		}

		if (representation.getText() == null || representation.getText().length() < 1) {
			canvas.setToolTipText(simpleName + "\n" + signalUID);
		} else {
			canvas.setToolTipText(representation.getText());
		}

	}

	@Override
	public void applyRepresentation() {
		applyRepresentation(true, true, true);
	}

	public void setXY(int x, int y, int width, int height) {
		canvas.setBounds(x, y, width, height);
		updateRepresentation(false, true, false);
		dashboard.layout(true);
	}

	/**
	 * checks the figure type and returns its index
	 **/
	public int getFigureType(AbstractMarkedWidgetFigure figure) {
		int type = 0;
		if (figure.getClass() == GaugeFigure.class) {
			type = Constants.WIDGET_GAUGE;
		} else if (figure.getClass() == MeterFigure.class) {
			type = Constants.WIDGET_METER;
		} else if (figure.getClass() == ThermometerFigure.class) {
			type = Constants.WIDGET_THERMO;
		} else if (figure.getClass() == ProgressBarFigure.class) {
			type = Constants.WIDGET_PROGRESS;
		} else if (figure.getClass() == TankFigure.class) {
			type = Constants.WIDGET_TANK;
		} else if (figure.getClass() == TextFigure.class) {
			type = Constants.WIDGET_TEXT;
		} else if (figure.getClass() == ImageFigure.class) {
			type = Constants.WIDGET_IMAGE;
		}
		return type;
	}

	@Override
	public void consume(DEMessage message) {
		updated = true;
		value = message.getValue();
	}

	@Override
	public void consumeBurst(List<DEMessage> messages) {
		if (messages.size() > 0) {
			updated = true;
			value = messages.get(0).getValue();
		}
	}

}
