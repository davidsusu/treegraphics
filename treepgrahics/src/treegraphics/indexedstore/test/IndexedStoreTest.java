package treegraphics.indexedstore.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import treegraphics.indexedstore.DefaultIndexedStore;
import treegraphics.indexedstore.IndexedStore;

public class IndexedStoreTest {

	public static void main(String[] args) {
		IndexedStore<Area> areaStore = new DefaultIndexedStore<Area>();

		areaStore.addItem(new Area(0, 10, 30, 30, 1));
		areaStore.addItem(new Area(30, 0, 60, 20, 2));
		areaStore.addItem(new Area(20, 15, 65, 35, 3));
		areaStore.addItem(new Area(10, 40, 30, 60, 4));
		areaStore.addItem(new Area(40, 40, 60, 50, 5));
		areaStore.addItem(new Area(50, 60, 70, 70, 6));
		areaStore.addItem(new Area(0, 50, 20, 70, 7));

		areaStore.addIndex("top", new TopAreaComparator());
		areaStore.addIndex("left", new LeftAreaComparator());
		areaStore.addIndex("bottom", new BottomAreaComparator());
		areaStore.addIndex("right", new RightAreaComparator());
		areaStore.addIndex("number", new NumberAreaComparator());
		
		System.out.println("\n----------\n");
		System.out.println(getViewportFiltered(areaStore, new Area(15, 25, 45 ,55, 0)));
		
		System.out.println("\n----------\n");
		System.out.println(getViewportFiltered(areaStore, new Area(35, 5, 55, 65, 0)));
		
		System.out.println("\n----------\n");
		System.out.println(getViewportFiltered(areaStore, new Area(5, 5, 25, 55, 0)));
	}
	
	protected static List<Area> getViewportFiltered(IndexedStore<Area> areaStore, Area viewportArea) {
		List<Area> topFiltered = areaStore.getFiltered("top", null, new Area(0, viewportArea.bottom, 10, viewportArea.bottom+10, 0), "number");
		List<Area> bottomFiltered = areaStore.getFiltered("bottom", new Area(0, viewportArea.top-10, 10, viewportArea.top, 0), null);
		List<Area> leftFiltered = areaStore.getFiltered("left", null, new Area(viewportArea.right, 0, viewportArea.right+10, 10, 0));
		List<Area> rightFiltered = areaStore.getFiltered("right", new Area(viewportArea.left-10, 0, viewportArea.left, 10, 0), null);

		List<Area> viewportFiltered = new ArrayList<Area>(topFiltered);
		viewportFiltered.retainAll(bottomFiltered);
		viewportFiltered.retainAll(leftFiltered);
		viewportFiltered.retainAll(rightFiltered);
		
		return viewportFiltered;
	}                                                                                    
	
	protected static class Area {

		public final int left;

		public final int top;

		public final int right;

		public final int bottom;

		public final int number;
		
		public Area(int left, int top, int right, int bottom, int number) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
			this.number = number;
		}
		
		@Override
		public String toString() {
			return ("Area("+number+")");
		}
		
	}
	
	protected static class TopAreaComparator implements Comparator<Area> {

		@Override
		public int compare(Area area1, Area area2) {
			return Integer.compare(area1.top, area2.top);
		}
		
	}
	
	protected static class LeftAreaComparator implements Comparator<Area> {

		@Override
		public int compare(Area area1, Area area2) {
			return Integer.compare(area1.left, area2.left);
		}
		
	}
	
	protected static class BottomAreaComparator implements Comparator<Area> {

		@Override
		public int compare(Area area1, Area area2) {
			return Integer.compare(area1.bottom, area2.bottom);
		}
		
	}
	
	protected static class RightAreaComparator implements Comparator<Area> {

		@Override
		public int compare(Area area1, Area area2) {
			return Integer.compare(area1.right, area2.right);
		}
		
	}
	
	protected static class NumberAreaComparator implements Comparator<Area> {

		@Override
		public int compare(Area area1, Area area2) {
			return Integer.compare(area1.number, area2.number);
		}
		
	}
	
}
