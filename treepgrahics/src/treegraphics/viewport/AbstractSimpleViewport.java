package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;

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

	@Override
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		Point point =  new Point(((x+0.5)/zoom)+origin.getX(), ((y+0.5)/zoom)+origin.getY());
		return getDrawablesAt(point);
	}

}
