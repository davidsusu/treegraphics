package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;

abstract public class AbstractSimpleViewport implements Viewport {

	protected DrawableService drawableService;
	
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
	public List<Drawable> getDrawablesAt(Point point) {
		List<Drawable> drawablesAt = new ArrayList<Drawable>(drawableService.getAffectedDrawables(point));
		Collections.reverse(drawablesAt);
		return drawablesAt;
	}

	@Override
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		Point virtualPoint =  new Point(((x+0.5)/zoom)+origin.getX(), ((y+0.5)/zoom)+origin.getY());
		return getDrawablesAt(virtualPoint);
	}
	
}
