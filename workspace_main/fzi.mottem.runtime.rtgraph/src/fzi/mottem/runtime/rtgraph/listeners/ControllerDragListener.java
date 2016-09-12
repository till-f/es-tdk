package fzi.mottem.runtime.rtgraph.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tracker;

import fzi.mottem.runtime.rtgraph.ControllerWidgetLink;
import fzi.mottem.runtime.rtgraph.settingsViews.SetupUI;

/**
 * This class defines a drag & move listener for the PTspec view controllers. It
 * allows the user to move controllers with mouse click and drag and it updates
 * the figure representation after the operation is finished.
 * 
 * @author K Katev
 *
 */
public class ControllerDragListener implements Listener {

	int resize_box_side = 20;
	Canvas c;
	Tracker tracker = null;
	Display display = Display.getCurrent();
	final Cursor cursor_resize;
	final Cursor cursor_default;
	final Cursor cursor_hand;
	Rectangle bounds;
	Rectangle resizeBounds;
	Rectangle dragBounds;

	private boolean in_resize_box = false;
	private boolean in_drag_box = false;
	private ControllerWidgetLink link;

	public ControllerDragListener(ControllerWidgetLink l) {
		this.link = l;
		c = l.getCanvas();
		bounds = c.getBounds();
		resizeBounds = new Rectangle(bounds.width - resize_box_side, bounds.height - resize_box_side, resize_box_side,
				resize_box_side);
		dragBounds = new Rectangle(bounds.x, bounds.y, resize_box_side, resize_box_side);

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
		
			/*
			 * if the click event is in the drag square of the canvas (upper left)
			 */
			if (event.x <= resize_box_side && event.y <= resize_box_side) {

				SetupUI.focusOnLink(link);
				link.getDashboard().setCurrentLink(link);

				tracker = new Tracker(c.getParent(), SWT.NONE);
				bounds = c.getBounds();

				if (event.x >= bounds.width - resize_box_side && event.y >= bounds.height - resize_box_side) {
					tracker = new Tracker(c.getParent(), SWT.RESIZE | SWT.RIGHT | SWT.DOWN);
					tracker.setStippled(true);
				}

				tracker.setRectangles(
						new Rectangle[] { new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height) });
				boolean finished = tracker.open();

				Rectangle tr = tracker.getRectangles()[0];

				if (tr.x != bounds.x || tr.y != bounds.y || tr.width != bounds.width || tr.height != bounds.height) {
					if (finished)
						link.updateRepresentation(false, true, false);
					c.setBounds(tracker.getRectangles()[0]);
					resizeBounds = new Rectangle(c.getBounds().width - resize_box_side,
							c.getBounds().height - resize_box_side, resize_box_side, resize_box_side);

					dragBounds = new Rectangle(c.getBounds().x, c.getBounds().y, resize_box_side, resize_box_side);
				}

				SetupUI.focusOnLink(link);
				c.layout(true);

			}
			
			/*
			 * If the click event is in the resize square of the canvas (bottom right)
			 */
			if (event.x >= bounds.width - resize_box_side && event.y >= bounds.height - resize_box_side) {
				bounds = c.getBounds();
				tracker = new Tracker(c.getParent(), SWT.RESIZE | SWT.RIGHT | SWT.DOWN);

				tracker.setStippled(true);
				tracker.setRectangles(
						new Rectangle[] { new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height) });
				tracker.open();

				Rectangle tr = tracker.getRectangles()[0];

				if (tr.width != bounds.width || tr.height != bounds.height) {
					c.setBounds(tracker.getRectangles()[0]);

					resizeBounds = new Rectangle(c.getBounds().width - resize_box_side,
							c.getBounds().height - resize_box_side, resize_box_side, resize_box_side);

					dragBounds = new Rectangle(c.getBounds().x, c.getBounds().y, resize_box_side, resize_box_side);
					link.updateRepresentation(false, true, false);
					bounds = c.getBounds();
				}
				SetupUI.focusOnLink(link);
			}

			break;
		case SWT.MouseMove:

			if (!in_drag_box && event.x <= resize_box_side && event.y <= resize_box_side) {
				c.setCursor(cursor_hand);
				in_drag_box = true;
			} else if (in_drag_box && !(event.x <= resize_box_side && event.y <= resize_box_side)) {
				c.setCursor(cursor_default);
				in_drag_box = false;
			}

			if (!in_resize_box && resizeBounds.contains(event.x, event.y)) {
				c.setCursor(cursor_resize);
				in_resize_box = true;
			} else if (in_resize_box && !resizeBounds.contains(event.x, event.y)) {
				c.setCursor(cursor_default);
				in_resize_box = false;
			}

			break;
		case SWT.MouseUp:
			//link.getDashboard().redraw();
			break;
		}
	}
}
