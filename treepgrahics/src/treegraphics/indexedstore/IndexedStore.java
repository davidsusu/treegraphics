package treegraphics.indexedstore;

import java.util.Comparator;
import java.util.List;

public interface IndexedStore<T> {

	public void addIndex(String indexName, Comparator<T> comparator);
	
	public void removeIndex(String indexName);
	
	public boolean hasIndex(String indexName);
	
	public boolean hasIndex(Comparator<T> comparator);
	
	public void addItem(T item);
	
	public void removeItem(T item);
	
	public boolean hasItem(T item);

	public List<T> getAll();

	public List<T> getAll(String orderIndexName);
	
	public List<T> getFiltered(String filterIndexName, T fromItem, T toItem);

	public List<T> getFiltered(String filterIndexName, T fromItem, T toItem, String orderIndexName);
	
}
