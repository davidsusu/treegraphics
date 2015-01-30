package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.CachedState;

public class SimpleDrawableService extends AbstractDrawableService {

	List<Drawable> drawables = new ArrayList<Drawable>();
	
	@Override
	public void expireState(CachedState cachedState) {
		if (cachedState instanceof Drawable) {
			Drawable drawable = (Drawable)cachedState;
			if (drawables.contains(drawable)) {
				fireDrawableChange(drawable);
			}
		}
		expiredDependencies.add(cachedState);
		expireState();
	}

	@Override
	public void freeFromDependecies() {
		for (Drawable drawable: drawables) {
			drawable.unregisterDependent(this);
		}

	}

	@Override
	public void addDrawable(Drawable drawable) {
		drawables.add(drawable);
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		drawables.remove(drawable);
	}

	@Override
	public Collection<Drawable> getDrawables() {
		return new ArrayList<Drawable>(drawables);
	}

	@Override
	public Collection<Drawable> getAffectedDrawables(Rectangle area) {
		ArrayList<Drawable> affectedDrawables = new ArrayList<Drawable>();
		for (Drawable drawable: drawables) {
			if (drawable.getReservedRectangle().intersects(area)) {
				affectedDrawables.add(drawable);
			}
		}
		// TODO (sort by z...)
		return affectedDrawables;
	}

	@Override
	public Collection<Drawable> getAffectedDrawables(Point point) {
		ArrayList<Drawable> affectedDrawables = new ArrayList<Drawable>();
		for (Drawable drawable: drawables) {
			if (drawable.isPointDominated(point)) {
				affectedDrawables.add(drawable);
			}
		}
		// TODO (sort by z...)
		return affectedDrawables;
	}

}
