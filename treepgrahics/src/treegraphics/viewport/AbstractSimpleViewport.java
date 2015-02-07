package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

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
	public void rebuild() {
		refresh();
	}

	@Override
	public List<Drawable> getDrawablesAt(Point point) {
		List<Drawable> drawablesAt = new ArrayList<Drawable>(drawableService.getAffectedDrawables(point));
		Collections.reverse(drawablesAt);
		return drawablesAt;
	}

	protected void repaint(Canvas canvas) {
		int xDisplacement = getXDisplacement();
		int yDisplacement = getYDisplacement();
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(new Rectangle(xDisplacement, yDisplacement, getWidth()+xDisplacement, getHeight()+yDisplacement));
		
		// FIXME
		Point displacedOrigin = new Point(origin.getX()-(xDisplacement/zoom), origin.getY()-(yDisplacement/zoom));
		canvas.setOrigin(displacedOrigin);
		canvas.setZoom(zoom);
		canvas.setAntialiasing(true);

		Rectangle area = getArea();
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(area);


		for (DrawListener drawListener: drawListeners) {
			drawListener.beforeRefresh(canvas, area);
		}
		
		for (DrawListener drawListener: drawListeners) {
			drawListener.beforeDraw(canvas, area);
		}
		
		for (Drawable drawable: drawableService.getAffectedDrawables(area)) {
			drawable.draw(canvas);
		}

		for (DrawListener drawListener: drawListeners) {
			drawListener.afterDraw(canvas, area);
		}

		for (DrawListener drawListener: drawListeners) {
			drawListener.afterRefresh(canvas, area);
		}
	}
	
}
