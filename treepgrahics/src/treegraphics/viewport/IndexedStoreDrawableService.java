package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.indexedstore.TreeSetIndexedStore;
import treegraphics.indexedstore.IndexedStore;
import treegraphics.util.CachedState;

public class IndexedStoreDrawableService extends AbstractDrawableService {

	protected IndexedStore<Drawable> store = null;
	
	public IndexedStoreDrawableService() {
		initIndexedStore(new TreeSetIndexedStore<Drawable>());
	}
	
	public IndexedStoreDrawableService(IndexedStore<Drawable> store) {
		initIndexedStore(store);
	}

	@Override
	public void clear() {
		store.clearItems();
	}

	protected void initIndexedStore(IndexedStore<Drawable> store) {
		this.store = store;
		this.store.addIndex("top", new TopDrawableComparator());
		this.store.addIndex("left", new LeftDrawableComparator());
		this.store.addIndex("bottom", new BottomDrawableComparator());
		this.store.addIndex("right", new RightDrawableComparator());
		this.store.addIndex("z", new ZDrawableComparator());
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
	public List<Drawable> getDrawables() {
		return store.getAll("z");
	}

	@Override
	public List<Drawable> getAffectedDrawables(Rectangle area) {
		double areaLeft = area.getLeft();
		double areaTop = area.getTop();
		double areaRight = area.getRight();
		double areaBottom = area.getBottom();
		
		List<Drawable> topFiltered = store.getFiltered("top", null, false, new FakeDrawable(new Point(0, areaBottom), new Point(10, areaBottom+10), 0), false, "z");
		List<Drawable> bottomFiltered = store.getFiltered("bottom", new FakeDrawable(new Point(0, areaTop-10), new Point(10, areaTop), 0), false, null, false);
		List<Drawable> leftFiltered = store.getFiltered("left", null, false, new FakeDrawable(new Point(areaRight, 0), new Point(areaRight+10, 10), 0), false);
		List<Drawable> rightFiltered = store.getFiltered("right", new FakeDrawable(new Point(areaLeft-10, 0), new Point(areaLeft, 10), 0), false, null, false);
		
		List<Drawable> areaFiltered = new ArrayList<Drawable>(topFiltered);
		areaFiltered.retainAll(bottomFiltered);
		areaFiltered.retainAll(leftFiltered);
		areaFiltered.retainAll(rightFiltered);
		
		return areaFiltered;
	}

	@Override
	public List<Drawable> getAffectedDrawables(Point point) {
		double pointX = point.getX();
		double pointY = point.getY();
		
		List<Drawable> topFiltered = store.getFiltered("top", null, false, new FakeDrawable(new Point(0, pointY), new Point(10, pointY+10), 0), true, "z");
		List<Drawable> bottomFiltered = store.getFiltered("bottom", new FakeDrawable(new Point(0, pointY-10), new Point(10, pointY), 0), true, null, false);
		List<Drawable> leftFiltered = store.getFiltered("left", null, false, new FakeDrawable(new Point(pointX, 0), new Point(pointX+10, 10), 0), true);
		List<Drawable> rightFiltered = store.getFiltered("right", new FakeDrawable(new Point(pointX-10, 0), new Point(pointX, 10), 0), true, null, false);
		
		List<Drawable> pointFiltered = new ArrayList<Drawable>(topFiltered);
		pointFiltered.retainAll(bottomFiltered);
		pointFiltered.retainAll(leftFiltered);
		pointFiltered.retainAll(rightFiltered);
		
		Iterator<Drawable> iterator = pointFiltered.iterator();
		while(iterator.hasNext()) {
			Drawable drawable = iterator.next();
			if (!drawable.isPointDominated(point)) {
				iterator.remove();
			}
		}
		
		return pointFiltered;
		
	}
	
	@Override
	public void expireState(CachedState cachedState) {
		if (cachedState instanceof Drawable) {
			Drawable drawable = (Drawable)cachedState;
			if (store.hasItem(drawable)) {
				store.updateItem(drawable);
			}
		}
		expiredDependencies.add(cachedState);
		expireState();
	}

	@Override
	public void freeFromDependecies() {
		for (Drawable drawable: store.getAll()) {
			drawable.unregisterDependent(this);
		}
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

}
