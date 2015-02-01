package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

abstract public class AbstractSimpleViewport extends AbstractViewport {

	protected DrawableService drawableService = new IndexedStoreDrawableService();
	
	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;

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
	public Point getOrigin() {
		return origin;
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	@Override
	public double getZoom() {
		return zoom;
	}

	@Override
	public Rectangle getArea() {
		Dimension dimension = new Dimension(getWidth()/zoom, getHeight()/zoom);
		return new Rectangle(origin, dimension);
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

}
