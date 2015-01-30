package treegraphics.indexedstore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// 
public class SortedListIndexedStore<T> implements IndexedStore<T> {

	protected List<T> items = new ArrayList<T>();
	
	@Override
	public void addIndex(String indexName, Comparator<T> comparator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeIndex(String indexName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasIndex(String indexName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasIndex(Comparator<T> comparator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addItem(T item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeItem(T item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasItem(T item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateItem(T item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getAll(String orderIndexName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getFiltered(String filterIndexName, T fromItem, boolean fromInclusive, T toItem, boolean toInclusive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getFiltered(String filterIndexName, T fromItem, boolean fromInclusive, T toItem, boolean toInclusive, String orderIndexName) {
		// TODO Auto-generated method stub
		return null;
	}

}
