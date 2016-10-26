package fzi.mottem.runtime.rtgraph.views;

import java.util.ArrayList;

import org.csstudio.swt.widgets.figures.AbstractMarkedWidgetFigure;
import org.csstudio.swt.widgets.figures.GaugeFigure;
import org.csstudio.swt.widgets.figures.KnobFigure;
import org.csstudio.swt.widgets.figures.MeterFigure;
import org.csstudio.swt.widgets.figures.ProgressBarFigure;
import org.csstudio.swt.widgets.figures.ScaledSliderFigure;
import org.csstudio.swt.widgets.figures.TankFigure;
import org.csstudio.swt.widgets.figures.ThermometerFigure;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.swt.xygraph.util.XYGraphMediaFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import fzi.mottem.runtime.dataexchanger.DataExchanger;
import fzi.mottem.runtime.dataexchanger.Signal;
import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink;
import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink.WidgetType;
import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.ControllerWidgetLink;
import fzi.mottem.runtime.rtgraph.EclipseFileSystemHelper;
import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;
import fzi.mottem.runtime.rtgraph.SetupUnit;
import fzi.mottem.runtime.rtgraph.ViewCoordinator;
import fzi.mottem.runtime.rtgraph.XML.DashboardRepresentation;
import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;
import fzi.mottem.runtime.rtgraph.editors.DashboardEditor;
import fzi.mottem.runtime.rtgraph.listeners.CallControllerSettingsListener;
import fzi.mottem.runtime.rtgraph.listeners.CallWidgetSettingsListener;
import fzi.mottem.runtime.rtgraph.listeners.ControllerDragListener;
import fzi.mottem.runtime.rtgraph.listeners.IndicatorDragListener;
import fzi.mottem.runtime.rtgraph.runnables.WidgetUpdater;
import fzi.mottem.runtime.rtgraph.settingsViews.SetupUI;

public class DashboardComposite extends Dashboard {

	public DashboardComposite(Composite parent, int style, DashboardRepresentation representation) {
		super(parent, style);
		this.representation = (representation != null) ? representation : new DashboardRepresentation();
		init();
		applyRepresentation();
		thisDC = this;
	}

	MouseListener clickListener;

	Menu popupMenu;
	Menu signalsMenu;

	DashboardComposite thisDC;

	ArrayList<MenuItem> signalsMenuItems = new ArrayList<MenuItem>();

	Rectangle highlight;

	// ------------------------------------------------

	AbstractWidgetExchangeLink current_widget;

	public AbstractWidgetExchangeLink getCurrent_link() {
		return current_widget;
	}

	public void setCurrentLink(AbstractWidgetExchangeLink current_link) {
		System.out.println("Dashboard: Setting current link to " + current_link);
		this.current_widget = current_link;
	}

	public void removeWidgetLink(AbstractWidgetExchangeLink link) {

		if (widgetLinks.contains(link)) {

			System.out.println("Dashboard: removing link " + link);

			widgetLinks.remove(link);

			if (current_widget.equals(link)) {
				if (widgetLinks.size() > 0) {
					current_widget = widgetLinks.get(0);
				} else {
					current_widget = null;
				}
			}
			link = null;
			// System.out.println("Link removed : " + link);
			setDirty(true);
		}
	}

	public void removeCurrentLink() {
		if (current_widget != null) {
			removeWidgetLink(current_widget);
		}
	}

	// ------------------------------------------------

	private DashboardRepresentation representation;

	WidgetUpdater widgetUpdater;
	public ArrayList<AbstractWidgetExchangeLink> widgetLinks = new ArrayList<AbstractWidgetExchangeLink>();

	private Image bg_image;
	private boolean dirty = false;

	protected int mousePosX;

	protected int mousePosY;

	private DashboardEditor editor;

	private MenuItem openSettingsViewItem;

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		if (editor != null)
			editor.fireChange();
	}

	@Override
	public DashboardRepresentation getRepresentation() {
		return representation;
	}

	@Override
	public void setRepresentation(DashboardRepresentation representation) {
		this.representation = representation;
	}

	@Override
	public void applyRepresentation() {
		setDashboardBackgroundImage(representation.background_path);
		generateWidgets();
	}

	@Override
	public void updateRepresentation() {

		representation.figureRepresentations.clear();
		for (AbstractWidgetExchangeLink wcons : widgetLinks) {
			wcons.updateRepresentation();
			representation.figureRepresentations.add(wcons.getRepresentation());
		}

	}

	public void generateWidgets() {

		for (AbstractWidgetExchangeLink link : widgetLinks) {
			link.drop();
			link.delete();
		}

		widgetLinks.clear();

		for (WidgetRepresentation widgetRep : representation.figureRepresentations) {

			switch (widgetRep.getWidgetType()) {
			case W_ABSTRACT:
				addIndicatorWidget(widgetRep);
				break;
			case W_CONTROLLER:
				addControllerWidget(widgetRep);
				break;
			case W_INDICATOR:
				addIndicatorWidget(widgetRep);
				break;
			default:
				addIndicatorWidget(widgetRep);
				break;
			}
		}
	}

	public void addIndicatorWidget(WidgetRepresentation widgetRepresentation) {
		WidgetRepresentation newRep = widgetRepresentation;
		if (newRep == null) {
			newRep = new WidgetRepresentation();
		}

		newRep.setWidgetType(WidgetType.W_INDICATOR);
		AbstractMarkedWidgetFigure figure = createIndicatorFigure(newRep.getType());

		final IndicatorWidgetLink l = new IndicatorWidgetLink(figure, this, newRep);

		widgetLinks.add(0, l);

		l.getCanvas().addMouseListener(new CallWidgetSettingsListener(l));
		IndicatorDragListener dragListener = new IndicatorDragListener(l);
		l.getCanvas().addListener(SWT.MouseDown, dragListener);
		l.getCanvas().addListener(SWT.MouseMove, dragListener);

		DataExchanger.registerConsumer(newRep.getSignalUID(), l);

		// SetupUI.refresh();
		setDirty(true);
	}

	public void addControllerWidget(WidgetRepresentation widgetRepresentation) {
		WidgetRepresentation newRep = widgetRepresentation;
		if (newRep == null) {
			newRep = new WidgetRepresentation();
		}

		newRep.setWidgetType(WidgetType.W_CONTROLLER);
		AbstractMarkedWidgetFigure figure = createControllerFigure(newRep.getType());

		final ControllerWidgetLink l = new ControllerWidgetLink(figure, this, newRep);
		widgetLinks.add(l);

		ControllerDragListener listener = new ControllerDragListener(l);
		l.getCanvas().addMouseListener(new CallControllerSettingsListener(l));
		l.getCanvas().addListener(SWT.MouseDown, listener);
		l.getCanvas().addListener(SWT.MouseMove, listener);

		// SetupUI.refresh();
		setDirty(true);
	}

	private static AbstractMarkedWidgetFigure createIndicatorFigure(int type) {

		AbstractMarkedWidgetFigure figure = null;

		switch (type) {
		case Constants.WIDGET_THERMO:
			figure = new ThermometerFigure();
			break;
		case Constants.WIDGET_GAUGE:
			figure = new GaugeFigure();
			figure.setBackgroundColor(XYGraphMediaFactory.getInstance().getColor(0, 0, 0));
			figure.setForegroundColor(XYGraphMediaFactory.getInstance().getColor(255, 255, 255));
			// ((GaugeFigure) figure).setTransparent(true);
			break;
		case Constants.WIDGET_METER:
			figure = new MeterFigure();
			break;
		case Constants.WIDGET_PROGRESS:
			figure = new ProgressBarFigure();
			((ProgressBarFigure) figure).setHorizontal(true);
			break;
		case Constants.WIDGET_TANK:
			figure = new TankFigure();
			break;
		case Constants.WIDGET_TEXT:
			figure = new TextFigure();
			break;
		case Constants.WIDGET_IMAGE:
			figure = new ImageFigure();
			break;
		default:
			figure = new GaugeFigure();
		}

		return figure;
	}

	private static AbstractMarkedWidgetFigure createControllerFigure(int type) {
		AbstractMarkedWidgetFigure figure = null;
		switch (type) {
		case Constants.CONTROLLER_KNOB:
			figure = new KnobFigure();
			((KnobFigure) figure).setEffect3D(true);
			((KnobFigure) figure).setIncrement(0.1);
			figure.setValueLabelFormat("0.#");
			break;
		case Constants.CONTROLLER_SLIDE:
			figure = new ScaledSliderFigure();
			((ScaledSliderFigure) figure).setEffect3D(true);
			((ScaledSliderFigure) figure).setPageIncrement(0.1);
			figure.setValueLabelFormat("0.#");
			break;
		default:
			figure = new KnobFigure();
		}

		/*
		 * because for some reason initialising and switching controller figures
		 * (switching slider to knob) causes the font to be null -> an exception
		 * is thrown and the figure can no longer be used
		 */
		figure.setFont(XYGraphMediaFactory.getInstance().getFont(new FontData("Arial", 9, SWT.NORMAL)));

		return figure;
	}

	public void setFigureType(int link_index, int figure_type) {
		AbstractWidgetExchangeLink link = widgetLinks.get(link_index);
		final AbstractMarkedWidgetFigure figure;

		switch (link.getWidgetType()) {
		case W_ABSTRACT:
			figure = createIndicatorFigure(figure_type);
			;
			break;
		case W_CONTROLLER:
			figure = createControllerFigure(figure_type);
			break;
		case W_INDICATOR:
			figure = createIndicatorFigure(figure_type);
			break;
		default:
			figure = createIndicatorFigure(figure_type);
			;
			break;
		}

		widgetLinks.get(link_index).updateFigure(figure);

	}

	public void refreshBgImage() {

		if (bg_image != null && !bg_image.isDisposed()) {
			int width = bg_image.getImageData().width;
			int height = bg_image.getImageData().height;

			if (representation.scale_background_x)
				width = this.getSize().x;

			if (representation.scale_background_y)
				height = this.getSize().y;

			try {
				Image scaled = imageScale(bg_image, width, height);
				setBackgroundImage(scaled);
				// scaled.dispose();
			} catch (Exception e) {
				System.out.println("Resize was not successful");
			} // end try
		}
	}

	private Image imageScale(Image image, int width, int height) {

		ImageData data = image.getImageData();
		if (data.width == width && data.height == height)
			return image;
		data = data.scaledTo(width, height);
		Image scaled = new Image(Display.getDefault(), data);
		return scaled;
	}

	public boolean isStretch_bg_x() {
		return representation.scale_background_x;
	}

	public void setStretch_bg_x(boolean stretch_bg_x) {
		representation.scale_background_x = stretch_bg_x;
	}

	public boolean isStretch_bg_y() {
		return representation.scale_background_y;
	}

	public void setStretch_bg_y(boolean stretch_bg_y) {
		representation.scale_background_y = stretch_bg_y;
	}

	public void setWidgetPollingDelay(int widget_pollingDelay) {
		this.representation.widget_polling_delay = widget_pollingDelay;
		widgetUpdater.setWidgetPollInterval(widget_pollingDelay);
		setChanged();
	}

	public String getBackground_path() {
		return representation.background_path;
	}

	public void setBackground_path(String background_path) {
		representation.background_path = background_path;
	}

	public void highlightWidget(int x, int y, int h, int w) {

	}

	public void initContainer() {

		GridData widgetCompositeData = new GridData();
		widgetCompositeData.verticalAlignment = SWT.FILL;
		setLayoutData(widgetCompositeData);

		clickListener = new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {

				DashboardComposite dashboard = (DashboardComposite) e.getSource();
				dashboard.mousePosX = e.x;
				dashboard.mousePosY = e.y;

				SetupUI.focusOnDashboard(dashboard, e.x, e.y);

				mousePosX = e.x;
				mousePosY = e.y;
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				callBackgroundDialog();
			}
		};

		addMouseListener(clickListener);

		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (representation.scale_background_x || representation.scale_background_y)
					refreshBgImage();
			}
		});

		popupMenu = new Menu(this);

		MenuItem addControllerItem = new MenuItem(popupMenu, SWT.CASCADE);
		addControllerItem.setText("Add Controller");
		addControllerItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/controller-icon.png").createImage());

		MenuItem addIndicatorItem = new MenuItem(popupMenu, SWT.CASCADE);
		addIndicatorItem.setText("Add Indicator");
		addIndicatorItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/dashboard-icon.png").createImage());

		Menu indicatorMenu = new Menu(popupMenu);
		addIndicatorItem.setMenu(indicatorMenu);
		Menu controllerMenu = new Menu(popupMenu);
		addControllerItem.setMenu(controllerMenu);

		ArrayList<MenuItem> indicatorItems = new ArrayList<MenuItem>();
		for (int i = 0; i < Constants.WIDGET_TYPES.length; i++) {
			MenuItem mi = new MenuItem(indicatorMenu, SWT.None);
			mi.setText(Constants.WIDGET_TYPES[i]);
			int itype = i;
			mi.addSelectionListener(new SelectionListener() {
				int type = itype;

				@Override
				public void widgetSelected(SelectionEvent e) {
					WidgetRepresentation rep = new WidgetRepresentation();
					rep.setX(mousePosX);
					rep.setY(mousePosY);
					rep.setType(type);
					addIndicatorWidget(rep);
					SetupUI.refresh();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}
			});
			indicatorItems.add(mi);
		}

		ArrayList<MenuItem> controllerItems = new ArrayList<MenuItem>();
		for (int i = 0; i < Constants.CONTROLLER_TYPES.length; i++) {
			MenuItem mi = new MenuItem(controllerMenu, SWT.None);
			int ctype = i;
			mi.setText(Constants.CONTROLLER_TYPES[i]);
			mi.addSelectionListener(new SelectionListener() {
				int type = ctype;

				@Override
				public void widgetSelected(SelectionEvent e) {
					WidgetRepresentation rep = new WidgetRepresentation();
					rep.setType(type);
					rep.setX(mousePosX);
					rep.setY(mousePosY);
					addControllerWidget(rep);
					SetupUI.refresh();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {

				}
			});
			controllerItems.add(mi);
		}

		MenuItem addImageItem = new MenuItem(popupMenu, SWT.CASCADE);
		addImageItem.setText("Add Image");
		addImageItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/icon-image-16.png").createImage());

		addImageItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				WidgetRepresentation wr = new WidgetRepresentation();
				wr.setType(Constants.WIDGET_IMAGE);
				String result = callImageDialog("Select image source for the Image Widget");
				if (result != null)
					wr.setText(result);
				wr.setX(mousePosX);
				wr.setY(mousePosY);

				// force image dimension when loading it for the first time
				ImageData imgData = new ImageData(result);
				wr.setHeight(imgData.height);
				wr.setWidth(imgData.width);

				addIndicatorWidget(wr);
				SetupUI.refresh();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		new MenuItem(popupMenu, SWT.SEPARATOR);

		MenuItem setBackgroundItem = new MenuItem(popupMenu, SWT.NONE);
		setBackgroundItem.setText("Set Background");
		setBackgroundItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/folder-picture-icon.png")
				.createImage());

		MenuItem removeBackgroundItem = new MenuItem(popupMenu, SWT.NONE);
		removeBackgroundItem.setText("Remove Background");
		removeBackgroundItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/remove.png").createImage());

		new MenuItem(popupMenu, SWT.SEPARATOR);

		openSettingsViewItem = new MenuItem(popupMenu, SWT.NONE);
		openSettingsViewItem.setText("Open Settings View");
		openSettingsViewItem.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("fzi.mottem.runtime.rtgraph", "/icons/settings-icon.png").createImage());

		setMenu(popupMenu);

		openSettingsViewItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ViewCoordinator.showSettingsViewpart();
				SetupUI.focusOnDashboard(thisDC);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		addIndicatorItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		addControllerItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		setBackgroundItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				callBackgroundDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		removeBackgroundItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				representation.background_path = "";
				setEmptyBackground();
				layout(true);
				setDirty(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setEmptyBackground() {
		Display display = Display.getCurrent();

		Color bg_gray_widget = new Color(display, 248, 248, 248);
		// this is also the widget background color as of right now
		bg_image = null;
		setBackgroundImage(null);
		setBackground(bg_gray_widget);

	}

	public String callImageDialog(String title) {
		EclipseFileSystemHelper helper = new EclipseFileSystemHelper();

		Shell shell = new Shell(Display.getCurrent());
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText(title);
		dialog.setFilterExtensions(new String[] { "*.*", "*.jpg", "*.gif", "*.png", "*.jpeg", "*.bmp" });

		dialog.setFilterPath(Platform.getLocation().toOSString());
		String result = dialog.open();
		return result;
	}

	public void callBackgroundDialog() {
		String path = callImageDialog("Select a background image for Dashboard " + this.name);
		setDashboardBackgroundImage(path);
	}

	private void setDashboardBackgroundImage(String path) {
		if (path != null) {
			
			EclipseFileSystemHelper helper = new EclipseFileSystemHelper();
			boolean imageInWorkspace = helper.fileIsInWorkspace(path);
			boolean isAbsolutePath = helper.isAbsolutePath(path);
			String absolutePath = null;
			String representationImagePath = null;
			
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
				bg_image = new Image(Display.getCurrent(), imgData);
				setBackground_path(representationImagePath); //representation.background_path	
				
			} catch (SWTException e) {
				System.out.println("Dashboard " + this.name + " could not load image from path " + path);
				setEmptyBackground();
			}
			
			bg_image = new Image(Display.getCurrent(), path);
			setBackgroundImage(bg_image);
			refreshBgImage();
			layout(true);
			setDirty(true);
		}
	}

	public ArrayList<AbstractWidgetExchangeLink> getWidgetLinks() {
		return widgetLinks;
	}

	public void redraw() {
		parent.layout(true);
	}

	public void startRunnables() {
		widgetUpdater = new WidgetUpdater(widgetLinks, representation.widget_polling_delay);
		Display.getCurrent().timerExec(0, widgetUpdater);
	}

	public void updateBgData() {
		bg_image = getBackgroundImage();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	private void init() {
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		initContainer();
		startRunnables();
	}

	public void addIndicators(ArrayList<Signal> signals) {
		int initial_x_position = 0;
		int initial_y_position = 0;
		for (Signal s : signals) {
			WidgetRepresentation rep = new WidgetRepresentation();
			rep.setSignalUID(s.getId());
			rep.setX(initial_x_position);
			rep.setY(initial_y_position);
			initial_x_position += 32;
			initial_y_position += 32;
			addIndicatorWidget(rep);
		}
	}

	public void addControllers(ArrayList<Signal> selected_out_signals) {
		int initial_x_position = 64;
		int initial_y_position = 64;
		for (Signal s : selected_out_signals) {
			WidgetRepresentation rep = new WidgetRepresentation();
			rep.setSignalUID(s.getId());
			rep.setX(initial_x_position);
			rep.setY(initial_y_position);
			initial_x_position += 32;
			initial_y_position += 32;
			addControllerWidget(rep);
		}
	}

	public void setEditor(DashboardEditor dashboardEditor) {
		this.editor = dashboardEditor;
	}

}
