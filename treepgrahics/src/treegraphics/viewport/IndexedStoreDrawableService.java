package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.indexedstore.TreeSetIndexedStore;
import treegraphics.indexedstore.IndexedStore;
import treegraphics.util.CachedState;

public class IndexedStoreDrawableService implements DrawableService {

	protected List<DrawableChangeListener> drawableChangeListeners = new ArrayList<DrawableChangeListener>();
	
	protected IndexedStore<Drawable> store = new TreeSetIndexedStore<Drawable>();

	protected boolean isExpired = true;

	final protected List<CachedState> expiredDependencies = new ArrayList<CachedState>();
	
	final protected List<CachedState> dependents = new ArrayList<CachedState>();
	
	public IndexedStoreDrawableService() {
		store.addIndex("top", new TopDrawableComparator());
		store.addIndex("left", new LeftDrawableComparator());
		store.addIndex("bottom", new BottomDrawableComparator());
		store.addIndex("right", new RightDrawableComparator());
		store.addIndex("z", new ZDrawableComparator());
	}
	
	@Override
	public void addDrawable(Drawable drawable) {
		store.addItem(drawable);
		drawable.registerDependent(this);
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		store.removeItem(drawable);
		drawable.unregisterDependent(this);
	}

	@Override
	public Collection<Drawable> getDrawables() {
		return store.getAll("z");
	}

	@Override
	public Collection<Drawable> getAffectedDrawables(Rectangle area) {
		/*List<Drawable> affectedDrawables = new ArrayList<Drawable>();
		for (Drawable drawable: store.getAll("z")) {
			if (drawable.getReservedRectangle().intersects(area)) {
				affectedDrawables.add(drawable);
			}
		}
		return affectedDrawables;*/
		
		
		double areaLeft = area.getLeft();
		double areaTop = area.getTop();
		double areaRight = area.getRight();
		double areaBottom = area.getBottom();
		
		List<Drawable> topFiltered = store.getFiltered("top", null, new FakeDrawable(new Point(0, areaBottom), new Point(10, areaBottom+10), 0), "z");
		List<Drawable> bottomFiltered = store.getFiltered("bottom", new FakeDrawable(new Point(0, areaTop-10), new Point(10, areaTop), 0), null);
		List<Drawable> leftFiltered = store.getFiltered("left", null, new FakeDrawable(new Point(areaRight, 0), new Point(areaRight+10, 10), 0));
		List<Drawable> rightFiltered = store.getFiltered("right", new FakeDrawable(new Point(areaLeft-10, 0), new Point(areaLeft, 10), 0), null);
		
		List<Drawable> viewportFiltered = new ArrayList<Drawable>(topFiltered);
		viewportFiltered.retainAll(bottomFiltered);
		viewportFiltered.retainAll(leftFiltered);
		viewportFiltered.retainAll(rightFiltered);
		
		return viewportFiltered;
	}

	@Override
	// TODO: use index
	public Collection<Drawable> getAffectedDrawables(Point point) {

		List<Drawable> affectedDrawables = new ArrayList<Drawable>();
		for (Drawable drawable: store.getAll("z")) {
			if (drawable.isPointDominated(point)) {
				affectedDrawables.add(drawable);
			}
		}
		return affectedDrawables;
		
		/*
		// TODO
		return store.getAll("z");
		//return null;*/
	}

	public void addDrawableChangeListener(DrawableChangeListener drawableChangeListener) {
		drawableChangeListeners.add(drawableChangeListener);
	}

	public void removeDrawableChangeListener(DrawableChangeListener drawableChangeListener) {
		drawableChangeListeners.remove(drawableChangeListener);
	}
	
	protected class LeftDrawableComparator implements Comparator<Drawable> {

		@Override
		public int compare(Drawable drawable1, Drawable drawable2) {
			return Double.compare(drawable1.getReservedRectangle().getLeft(), drawable2.getReservedRectangle().getLeft());
		}
		
	}

	protected class TopDrawableComparator implements Comparator<Drawable> {

		@Override
		public int compare(Drawable drawable1, Drawable drawable2) {
			return Double.compare(drawable1.getReservedRectangle().getTop(), drawable2.getReservedRectangle().getTop());
		}
		
	}
	
	protected class RightDrawableComparator implements Comparator<Drawable> {

		@Override
		public int compare(Drawable drawable1, Drawable drawable2) {
			return Double.compare(drawable1.getReservedRectangle().getRight(), drawable2.getReservedRectangle().getRight());
		}
		
	}
	
	protected class BottomDrawableComparator implements Comparator<Drawable> {

		@Override
		public int compare(Drawable drawable1, Drawable drawable2) {
			return Double.compare(drawable1.getReservedRectangle().getBottom(), drawable2.getReservedRectangle().getBottom());
		}
		
	}
	
	protected static class ZDrawableComparator implements Comparator<Drawable> {

		@Override
		public int compare(Drawable drawable1, Drawable drawable2) {
			return Double.compare(drawable1.getZ(), drawable2.getZ());
		}
		
	}
	
	protected class FakeDrawable implements Drawable {
		
		final protected Point point1;
		final protected Point point2;
		final protected double z;
		
		public FakeDrawable(Point point1, Point point2, double z) {
			this.point1 = point1;
			this.point2 = point2;
			this.z = z;
		}

		@Override
		public void draw(Canvas canvas) {
		}

		@Override
		public double getZ() {
			return z;
		}

		@Override
		public Rectangle getReservedRectangle() {
			return new Rectangle(point1, point2);
		}

		@Override
		public boolean isPointDominated(Point point) {
			return false;
		}

		@Override
		public void expireState() {
		}

		@Override
		public void expireState(CachedState cachedState) {
		}

		@Override
		public void registerDependent(CachedState cachedState) {
		}

		@Override
		public void unregisterDependent(CachedState cachedState) {
		}

		@Override
		public void freeFromDependecies() {
		}
		
		@Override
		public String toString() {
			return "FakeDrawable("+point1+"; "+point2+")";
		}
		
	}

	@Override
	public void expireState() {
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState(this);
		}
	}

	@Override
	public void expireState(CachedState cachedState) {
		if (cachedState instanceof Drawable) {
			Drawable drawable = (Drawable)cachedState;
			store.updateItem(drawable);
		}
		expiredDependencies.add(cachedState);
		expireState();
	}

	@Override
	public void registerDependent(CachedState cachedState) {
		// FIXME
		if (!dependents.contains(cachedState)) {
			dependents.add(cachedState);
		}
	}

	@Override
	public void unregisterDependent(CachedState cachedState) {
		dependents.remove(cachedState);
	}

	@Override
	public void freeFromDependecies() {
		for (Drawable drawable: store.getAll()) {
			drawable.unregisterDependent(this);
		}
	}
	
}
