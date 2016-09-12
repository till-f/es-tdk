package fzi.mottem.runtime.rtgraph.views;

import java.util.ArrayList;

import org.csstudio.swt.widgets.figures.AbstractMarkedWidgetFigure;
import org.csstudio.swt.widgets.figures.GaugeFigure;
import org.csstudio.swt.widgets.figures.MeterFigure;
import org.csstudio.swt.widgets.figures.ProgressBarFigure;
import org.csstudio.swt.widgets.figures.TankFigure;
import org.csstudio.swt.widgets.figures.ThermometerFigure;
import org.csstudio.swt.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import fzi.mottem.runtime.rtgraph.AbstractWidgetExchangeLink;
import fzi.mottem.runtime.rtgraph.Constants;
import fzi.mottem.runtime.rtgraph.SetupUnit;
import fzi.mottem.runtime.rtgraph.XML.DashboardRepresentation;
import fzi.mottem.runtime.rtgraph.XML.ProfileUtils;
import fzi.mottem.runtime.rtgraph.XML.WidgetRepresentation;
import fzi.mottem.runtime.rtgraph.runnables.WidgetUpdater;

public class Dashboard extends ObservableView<DashboardRepresentation> {

	private String background_path = "c:\\";
	protected String name = "";
	Composite parent;

	WidgetUpdater widgetUpdater;
	public ArrayList<AbstractWidgetExchangeLink> widgetLinks = new ArrayList<AbstractWidgetExchangeLink>();
	//public ArrayList<ControllerWidgetLink> controllerLinks = new ArrayList<ControllerWidgetLink>();

	private int widget_pollingDelay;

	private Image bg_image;
	private boolean stretch_bg_x = false;
	private boolean stretch_bg_y = false;

	private GridData widgetCompositeData;
	private GridData widgetElementData;
	private DashboardRepresentation representation;

	@Override
	public DashboardRepresentation getRepresentation() {
		return representation;
	}

	@Override
	public void setRepresentation(DashboardRepresentation representation) {
		this.representation = representation;
		//applyRepresentation();
	}

	@Override
	public void applyRepresentation() {

		stretch_bg_x = representation.scale_background_x;
		stretch_bg_y = representation.scale_background_y;
		widget_pollingDelay = representation.widget_polling_delay;
		
		try {
			bg_image = new Image(Display.getCurrent(), representation.background_path);
			setBackgroundImage(bg_image);
			setBackground_path(representation.background_path); //background_path = representation.background_path;
		} catch (SWTException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not load background image from " + representation.background_path);
			setBackgroundImage(
					new Image(Display.getCurrent(), SetupUnit.class.getResourceAsStream(Constants.gray_icon)));
			setBackground_path(SetupUnit.class.getResource(Constants.gray_icon).getPath());
		}

		generateWidgets();
	
	}

	@Override
	public void updateRepresentation() {

		representation.scale_background_x = stretch_bg_x;
		representation.scale_background_y = stretch_bg_y;
		representation.widget_polling_delay = widget_pollingDelay;
		representation.background_path = background_path;

		representation.figureRepresentations.clear();
		for (AbstractWidgetExchangeLink wcons : widgetLinks) {
			wcons.updateRepresentation();
			representation.figureRepresentations.add(wcons.getRepresentation());
		}

	}

	public void generateWidgets() {
		System.out.println("Dashboard " + name + ": cleaning up and generating widgets");
		for (AbstractWidgetExchangeLink link : widgetLinks) {
			link.drop();
			link.delete();
		}

		widgetLinks.clear();
		System.out.println("Dashboard " + name + ": generating widgets...");
		for (WidgetRepresentation widgetRep : representation.figureRepresentations) {
			addIndicatorWidget(widgetRep);
		}
	}

	public void addIndicatorWidget(WidgetRepresentation widgetRepresentation) {
		WidgetRepresentation newRep = widgetRepresentation;
		if (newRep == null) {
			newRep = new WidgetRepresentation();
		}

		final AbstractMarkedWidgetFigure figure = createWidgetFigure(newRep.getType());
	}

	private static AbstractMarkedWidgetFigure createWidgetFigure(int type) {
		final AbstractMarkedWidgetFigure figure;
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
		default:
			figure = new GaugeFigure();
		}
		return figure;
	}

	public void setFigureType(int link_index, int figure_type) {
		final AbstractMarkedWidgetFigure figure = createWidgetFigure(figure_type);
		widgetLinks.get(link_index).updateFigure(figure);
	}

	public void refreshBgImage() {

		if (bg_image != null && !bg_image.isDisposed()) {
			int width = bg_image.getImageData().width;
			int height = bg_image.getImageData().height;

			if (stretch_bg_x)
				width = this.getSize().x;

			if (stretch_bg_y)
				height = this.getSize().y;

			try {
				Image scaled = imageScale(bg_image, width, height);
				setBackgroundImage(scaled);
				// scaled.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			} // end try
		}
	}

	private Image imageScale(Image image, int width, int height) {

		ImageData data = image.getImageData();

		if (data.width == width && data.height == height)
			return image;

		data = data.scaledTo(width, height);
		Image scaled = new Image(Display.getDefault(), data);
		// image.dispose();
		return scaled;
	}

	public boolean isStretch_bg_x() {
		return stretch_bg_x;
	}

	public void setStretch_bg_x(boolean stretch_bg_x) {
		this.stretch_bg_x = stretch_bg_x;
	}

	public boolean isStretch_bg_y() {
		return stretch_bg_y;
	}

	public void setStretch_bg_y(boolean stretch_bg_y) {
		this.stretch_bg_y = stretch_bg_y;
	}

	public int getWidget_pollingDelay() {
		return widget_pollingDelay;
	}

	public void setWidgetPollingDelay(int widget_pollingDelay) {
		this.widget_pollingDelay = widget_pollingDelay;
		widgetUpdater.setWidgetPollInterval(widget_pollingDelay);
		setChanged();
	}

	public Dashboard(Composite parent, int style) {
		super(parent, style);
	}

	public String getBackground_path() {
		return background_path;
	}

	public void setBackground_path(String background_path) {
		this.background_path = background_path;
	}

	public void initContainer() {
		if (representation == null)
			representation = new DashboardRepresentation();
		initContainer(representation.widget_width_hint, representation.widget_height_hint,
				representation.widget_polling_delay);
	}

	public void initContainer(int w_w_hint, int w_h_hint, int widget_polling_delay) {
		if (representation == null)
			representation = new DashboardRepresentation();

		widgetCompositeData = new GridData();
		widgetCompositeData.verticalAlignment = SWT.FILL;

		widgetElementData = new GridData();

		widgetElementData.grabExcessVerticalSpace = true;
		widgetElementData.grabExcessHorizontalSpace = true;
		widgetElementData.minimumWidth = w_w_hint;
		widgetElementData.minimumHeight = w_h_hint;
		widgetElementData.verticalAlignment = SWT.FILL;
		widgetElementData.horizontalAlignment = SWT.FILL;

		setLayoutData(widgetCompositeData);
		// setBackgroundMode(SWT.INHERIT_FORCE); //no transparency

		widget_pollingDelay = widget_polling_delay;

		addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent event) {
				if (event.button == 3) {
					Shell shell = new Shell(Display.getCurrent());
					// shell.setSize(400, 400);
					FileDialog fd = new FileDialog(shell, SWT.SAVE);
					fd.setText("Save Dashboard XML");
					fd.setFilterPath(representation.getPath());
					String[] filterExt = { "*.*", "*" + Constants.EXTENSION_DASHBOARD };
					fd.setFilterExtensions(filterExt);
					String selected = fd.open();
					System.out.println(selected);

					if (selected != null) {
						// representation.setPath(selected); //updated by
						// ProfileUtils
						System.out.println("Saving " + representation.getPath() + " in " + selected);
						updateRepresentation();
						ProfileUtils.saveDashboardXML(representation, selected);
						System.out.println("GraphView XML data is saved! ");
					}

				}
			}
		});

		MouseListener clickListener = new MouseListener() {
	
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// SetupUI.getWidgetSettings().setDashboard(e.);
				
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
				if (stretch_bg_x || stretch_bg_y)
					refreshBgImage();
			}
		});	
	}

	public void callBackgroundDialog() {
		Shell shell = new Shell(Display.getCurrent());
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.jpg", "*.gif", "*.png", "*.jpeg", "*.bmp", "*.*" });

		dialog.setFilterPath("c:\\");
		String result = dialog.open();

		if (result != null) {
			background_path = result;
			bg_image = new Image(Display.getCurrent(), background_path);
			setBackgroundImage(bg_image);
			refreshBgImage();
			layout(true);
		}
	}

	public ArrayList<AbstractWidgetExchangeLink> getWidgetLinks() {
		return widgetLinks;
	}

	public void redraw() {
		parent.layout(true);
	}


	public void startRunnables() {
		widgetUpdater = new WidgetUpdater(widgetLinks, widget_pollingDelay);
		Display.getCurrent().timerExec(0, widgetUpdater);
	}

	public void updateBgData() {
		bg_image = getBackgroundImage();
	}
/*
	public ArrayList<ControllerWidgetLink> getControllerLinks() {
		return controllerLinks;
	}
*/
	public void setName(String name) {
		this.name = name;
	}

	/*
	@Override
	public void notifyObservers() {
		for (WidgetViewObserver o : observers) {
			o.update(this);
		}
	}*/
	
	public void init(DashboardRepresentation rep) {
		this.representation = rep;
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		initContainer();
		//addObserver(new WidgetViewObserver());
		startRunnables();
	}
}
