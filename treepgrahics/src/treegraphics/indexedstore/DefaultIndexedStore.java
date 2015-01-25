package treegraphics.indexedstore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DefaultIndexedStore<T> implements IndexedStore<T> {

	protected List<T> items = new ArrayList<T>();
	
	protected Map<String, Comparator<T>> indexMap = new HashMap<String, Comparator<T>>();
	
	protected Map<String, TreeSet<T>> itemSetMap = new HashMap<String, TreeSet<T>>();
	
	@Override
	public void addIndex(String indexName, Comparator<T> comparator) {
		if (hasIndex(indexName)) {
			removeIndex(indexName);
		}
		Comparator<T> multiComparator = new MultiComparator(comparator);
		indexMap.put(indexName, multiComparator);
		TreeSet<T> itemSet = new TreeSet<T>(multiComparator);
		itemSet.addAll(items);
		itemSetMap.put(indexName, itemSet);
	}

	@Override
	public void removeIndex(String indexName) {
		if (hasIndex(indexName)) {
			indexMap.remove(indexName);
			itemSetMap.remove(indexName);
		}
	}

	@Override
	public boolean hasIndex(String indexName) {
		return indexMap.containsKey(indexName);
	}

	@Override
	public boolean hasIndex(Comparator<T> comparator) {
		return indexMap.containsValue(comparator);
	}

	@Override
	public void addItem(T item) {
		items.add(item);
		for (Map.Entry<String, TreeSet<T>> entry: itemSetMap.entrySet()) {
			entry.getValue().add(item);
		}
	}

	@Override
	public void removeItem(T item) {
		items.remove(item);
		for (Map.Entry<String, TreeSet<T>> entry: itemSetMap.entrySet()) {
			entry.getValue().remove(item);
		}
	}

	@Override
	public boolean hasItem(T item) {
		return items.contains(item);
	}

	@Override
	public List<T> getAll() {
		return new ArrayList<T>(items);
	}

	@Override
	public List<T> getAll(String orderIndexName) {
		if (hasIndex(orderIndexName)) {
			return new ArrayList<T>();
		}
		return new ArrayList<T>(itemSetMap.get(orderIndexName));
	}

	@Override
	public List<T> getFiltered(String filterIndexName, T fromItem, T toItem) {
		if (hasIndex(filterIndexName)) {
			return new ArrayList<T>();
		}
		return new ArrayList<T>(itemSetMap.get(filterIndexName).subSet(fromItem, toItem));
	}

	@Override
	public List<T> getFiltered(String filterIndexName, T fromItem, T toItem, String orderIndexName) {
		if (hasIndex(filterIndexName)) {
			return new ArrayList<T>();
		}
		@SuppressWarnings("unchecked")
		TreeSet<T> orderedResult = (TreeSet<T>)itemSetMap.get(orderIndexName).clone();
		orderedResult.retainAll(itemSetMap.get(filterIndexName).subSet(fromItem, toItem));
		return new ArrayList<T>(orderedResult);
	}
	
	// FIXME..
	protected class MultiComparator implements Comparator<T> {
		
		protected Comparator<T> innerComparator;
		
		public MultiComparator(Comparator<T> innerComparator) {
			this.innerComparator = innerComparator;
		}

		@Override
		public int compare(T item1, T item2) {
			int result = innerComparator.compare(item1, item2);
			if (result==0) {
				result = -1;
			}
			return result;
		}
		
	}

}
