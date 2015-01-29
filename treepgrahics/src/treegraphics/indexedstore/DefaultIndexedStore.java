package treegraphics.indexedstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.NavigableSet;
import java.util.TreeSet;

import treegraphics.util.MultiComparator;

public class DefaultIndexedStore<T> implements IndexedStore<T> {

	protected List<T> items = new ArrayList<T>();
	
	protected Map<String, Comparator<T>> indexMap = new HashMap<String, Comparator<T>>();
	
	protected Map<String, TreeSet<T>> itemSetMap = new HashMap<String, TreeSet<T>>();
	
	@Override
	public void addIndex(String indexName, Comparator<T> comparator) {
		if (hasIndex(indexName)) {
			removeIndex(indexName);
		}
		Comparator<T> multiComparator = new MultiComparator<T>(comparator);
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
	public void updateItem(T item) {
		if (!hasItem(item)) {
			return;
		}
		for (Map.Entry<String, TreeSet<T>> entry: itemSetMap.entrySet()) {
			TreeSet<T> itemSet = entry.getValue();
			// FIXME
			//itemSet.remove(item);
			//itemSet.add(item);
			itemSet.clear();
			itemSet.addAll(items);
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
		if (!hasIndex(orderIndexName)) {
			return new ArrayList<T>();
		}
		return new ArrayList<T>(itemSetMap.get(orderIndexName));
	}

	@Override
	public List<T> getFiltered(String filterIndexName, T fromItem, T toItem) {
		return new ArrayList<T>(getFilteredCollection(filterIndexName, fromItem, toItem));
	}

	@Override
	public List<T> getFiltered(String filterIndexName, T fromItem, T toItem, String orderIndexName) {
		if (!hasIndex(filterIndexName)) {
			return new ArrayList<T>();
		}
		
		ArrayList<T> orderedResult;
		if (hasIndex(orderIndexName)) {
			orderedResult = new ArrayList<T>(itemSetMap.get(orderIndexName));
		} else {
			orderedResult = new ArrayList<T>(items);
		}
		orderedResult.retainAll(getFilteredCollection(filterIndexName, fromItem, toItem));
		
		return orderedResult;
	}
	
	protected Collection<T> getFilteredCollection(String filterIndexName, T fromItem, T toItem) {
		if (!hasIndex(filterIndexName)) {
			return new ArrayList<T>();
		}
		TreeSet<T> itemSet = itemSetMap.get(filterIndexName);
		if (fromItem==null && toItem==null) {
			return itemSet;
		} else if (fromItem==null) {
			return itemSet.headSet(toItem);
		} else if (toItem==null) {
			return itemSet.tailSet(fromItem);
		} else {
			return itemSet.subSet(fromItem, toItem);
		}
	}

}
