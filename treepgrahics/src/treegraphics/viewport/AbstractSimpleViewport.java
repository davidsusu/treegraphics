package treegraphics.viewport;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics_awt.Graphics2DCanvas;

abstract public class AbstractSimpleViewport extends AbstractViewport {

	protected DrawableService drawableService = new IndexedStoreDrawableService();
	
	@Override
	public void addDrawable(Drawable drawable) {
		drawableService.addDrawable(drawable);
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		drawableService.removeDrawable(drawable);
	}

	@Override
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	@Override
	public List<Drawable> getDrawablesAt(Point point) {
		List<Drawable> drawablesAt = new ArrayList<Drawable>(drawableService.getAffectedDrawables(point));
		Collections.reverse(drawablesAt);
		return drawablesAt;
	}

	@Override
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		Point point =  new Point(((x+0.5)/zoom)+origin.getX(), ((y+0.5)/zoom)+origin.getY());
		return getDrawablesAt(point);
	}

	protected void repaint(Graphics2D g) {
		Canvas canvas = new Graphics2DCanvas(g);
		
		// FIXME
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(new Rectangle(0, 0, getWidth(), getHeight()));
		
		canvas.setOrigin(origin);
		canvas.setZoom(zoom);
		canvas.setAntialiasing(true);

		Rectangle area = getArea();
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(area);
		
		for (DrawListener drawListener: drawListeners) {
			drawListener.beforeDraw(canvas, area);
		}
		
		for (Drawable drawable: drawableService.getAffectedDrawables(area)) {
			drawable.draw(canvas);
		}

		for (DrawListener drawListener: drawListeners) {
			drawListener.afterDraw(canvas, area);
		}
	}
	
}
