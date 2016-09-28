package fzi.mottem.runtime.rtgraph.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

import fzi.mottem.runtime.rtgraph.IndicatorWidgetLink;
import fzi.mottem.runtime.rtgraph.settingsViews.SetupUI;

/**
 * This class defines a drag & move listener for the PTspec widgets. It allows
 * the user to move widgets with mouse click and drag and it updates the figure
 * representation after the operation is finished.
 * 
 * @author K Katev
 *
 */
public class IndicatorDragListener implements Listener {

	int resize_box_side = 20;
	IndicatorWidgetLink link;
	Canvas c;
	Tracker tracker = null;
	Display display = Display.getCurrent();
	final Cursor cursor_resize;
	final Cursor cursor_default;
	final Cursor cursor_hand;
	Rectangle bounds;
	Rectangle resizeBounds;
	Shell shell;

	boolean in_resize_box = false;
	private boolean in_drag_box = false;

	/*
	 * @param link the link containing the canvas to which this listener is tied
	 */
	public IndicatorDragListener(IndicatorWidgetLink link) {
		this.link = link;
		this.c = link.getCanvas();
		bounds = c.getBounds();
		resizeBounds = new Rectangle(bounds.width - resize_box_side, bounds.height - resize_box_side, resize_box_side,
				resize_box_side);
		shell = display.getActiveShell();
		cursor_resize = display.getSystemCursor(SWT.CURSOR_SIZENWSE);
		cursor_default = display.getSystemCursor(SWT.CURSOR_ARROW);
		cursor_hand = display.getSystemCursor(SWT.CURSOR_HAND);
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.MouseDown:
			
			//put the canvas to the top of the drawing order
			c.moveAbove(null);
		
			tracker = new Tracker(c.getParent(), SWT.NONE);
			bounds = c.getBounds();

			if (event.x >= bounds.width - resize_box_side && event.y >= bounds.height - resize_box_side) {
				tracker = new Tracker(c.getParent(), SWT.RESIZE | SWT.RIGHT | SWT.DOWN);

				tracker.setStippled(true);
			}

			tracker.setRectangles(new Rectangle[] { new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height) });
			boolean finished = tracker.open();

			Rectangle tr = tracker.getRectangles()[0];

			if (tr.x != bounds.x || tr.y != bounds.y || tr.width != bounds.width || tr.height != bounds.height) {
				if (finished)
					link.updateRepresentation(false, true, false);
				c.setBounds(tracker.getRectangles()[0]);
				resizeBounds = new Rectangle(c.getBounds().width - resize_box_side,
						c.getBounds().height - resize_box_side, resize_box_side, resize_box_side);
			}

			SetupUI.focusOnLink(link);
			link.getDashboard().setCurrentLink(link);
			link.updateCanvasImage();
			c.layout(true);

			break;
		case SWT.MouseMove:
			if (!in_drag_box && !in_resize_box && event.x <= bounds.width && event.y <= bounds.height) {
				c.setCursor(cursor_hand);
			
				in_drag_box = true;
			} else if (in_drag_box && !(event.x <= bounds.width && event.y <= bounds.height)) {
				
				c.setCursor(cursor_default);
				in_drag_box = false;
				in_resize_box = false;
			}
			
			if (!in_resize_box && resizeBounds.contains(event.x, event.y)) {
				c.setCursor(cursor_resize);
				in_resize_box = true;
				in_drag_box = false;
				
			} else if (in_resize_box && !resizeBounds.contains(event.x, event.y)) {
				//c.setCursor(cursor_default);					
				in_resize_box = false;
				
			}

			break;
		case SWT.MouseUp:
			//link.getDashboard().redraw();
			break;

		}

	}
}
