package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.CachedState;

public class SimpleDrawableService extends AbstractDrawableService {

	List<Drawable> drawables = new ArrayList<Drawable>();
	
	@Override
	public void expireState(CachedState cachedState) {
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
	public List<Drawable> getDrawables() {
		return new ArrayList<Drawable>(drawables);
	}

	@Override
	public List<Drawable> getAffectedDrawables(Rectangle area) {
		ArrayList<Drawable> affectedDrawables = new ArrayList<Drawable>();
		for (Drawable drawable: drawables) {
			if (drawable.getReservedRectangle().intersects(area)) {
				affectedDrawables.add(drawable);
			}
		}
		Collections.sort(affectedDrawables, new ZDrawableComparator());
		return affectedDrawables;
	}

	@Override
	public List<Drawable> getAffectedDrawables(Point point) {
		ArrayList<Drawable> affectedDrawables = new ArrayList<Drawable>();
		for (Drawable drawable: drawables) {
			if (drawable.isPointDominated(point)) {
				affectedDrawables.add(drawable);
			}
		}
		Collections.sort(affectedDrawables, new ZDrawableComparator());
		return affectedDrawables;
	}
	
	// FIXME: duplicated from IndexedStoreDrawableService
	protected static class ZDrawableComparator implements Comparator<Drawable> {

		@Override
		public int compare(Drawable drawable1, Drawable drawable2) {
			return Double.compare(drawable1.getZ(), drawable2.getZ());
		}
		
	}

}
