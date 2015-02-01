package treegraphics.viewport;

import java.util.List;

import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

abstract public class AbstractCachedViewport extends AbstractViewport {

	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;

	protected DrawableService drawableService = new IndexedStoreDrawableService();

	protected DrawableService drawableCacheService = new IndexedStoreDrawableService();
	
	protected Rectangle drawableCacheRectangle = new Rectangle(0, 0, 0, 0);
	
	protected Rectangle renderedRectangle = new Rectangle(0, 0, 0, 0);
	
	@Override
	public void addDrawable(Drawable drawable) {
		drawableService.addDrawable(drawable);
		if (drawableCacheRectangle.intersects(drawable.getReservedRectangle())) {
			drawableCacheService.addDrawable(drawable);
			// TODO: redraw...
		}
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		drawableService.removeDrawable(drawable);
		// FIXME: talán jobb, ha ez az ifen kívül marad...
		drawableCacheService.removeDrawable(drawable);
		if (drawableCacheRectangle.intersects(drawable.getReservedRectangle())) {
			// TODO: redraw...
		}
	}

	@Override
	public void setOrigin(Point origin) {
		// FIXME: round to pixel...
		this.origin = origin;
		refresh();
	}

	@Override
	public Point getOrigin() {
		return origin;
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
		refresh();
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
	public void refresh() {
		// TODO
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

	protected Rectangle getMinimumDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()/2+100, area.getHeight()/2+100);
	}

	protected Rectangle getMaximumDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()+100, area.getHeight()+100);
	}
	
	protected Rectangle getMinimumRenderingArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*2+100, area.getHeight()*2+100);
	}

	protected Rectangle getMaximumRenderingArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*3+100, area.getHeight()*3+100);
	}
	
	protected Rectangle getPaddedRectangle(Rectangle rectangle, double xPadding, double yPadding) {
		Point leftTop = new Point(rectangle.getLeft()-xPadding, rectangle.getTop()-yPadding);
		Dimension dimension = new Dimension(rectangle.getWidth()+(xPadding*2), rectangle.getHeight()+(yPadding*2));
		return new Rectangle(leftTop, dimension);
	}
	
}
