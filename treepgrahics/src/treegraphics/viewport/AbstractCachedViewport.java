package treegraphics.viewport;

import java.util.List;

import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

abstract public class AbstractCachedViewport implements Viewport {

	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;

	protected DrawableService drawableService = new IndexedStoreDrawableService();

	protected DrawableService drawableCacheService = new IndexedStoreDrawableService();
	
	protected Rectangle drawableCacheRectangle = new Rectangle(0, 0, 0, 0);
	
	@Override
	public void addDrawable(Drawable drawable) {
		// TODO
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		// TODO
	}

	@Override
	public void setOrigin(Point origin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getOrigin() {
		return origin;
	}

	@Override
	public void setZoom(double zoom) {
		// TODO
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
		if (drawableCacheRectangle.containsPoint(point)) {
			return drawableCacheService.getAffectedDrawables(point);
		} else {
			return drawableService.getAffectedDrawables(point);
		}
	}

	@Override
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		Point point =  new Point(((x+0.5)/zoom)+origin.getX(), ((y+0.5)/zoom)+origin.getY());
		return getDrawablesAt(point);
	}

}
