package treegraphics.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MultiComparator<T> implements Comparator<T> {
	
	protected Comparator<T> innerComparator;
	
	protected List<T> problematicItems = new ArrayList<T>();
	
	public MultiComparator(Comparator<T> innerComparator) {
		this.innerComparator = innerComparator;
	}

	@Override
	public int compare(T item1, T item2) {
		if (item1==item2) {
			return 0;
		}
		int result = innerComparator.compare(item1, item2);
		if (result==0) {
			if (item1 instanceof Identified && item2 instanceof Identified) {
				result = Integer.compare(((Identified)item1).getIdentifier(), ((Identified)item2).getIdentifier());
			} else {
				result = Integer.compare(System.identityHashCode(item1), System.identityHashCode(item2));
				if (result==0) {
					boolean contains1 = problematicItems.contains(item1);
					boolean contains2 = problematicItems.contains(item2);
					if (!contains1 && !contains2) {
						problematicItems.add(item1);
						problematicItems.add(item2);
						return -1;
					} else if (contains1) {
						problematicItems.add(item2);
						return -1;
					} else if (contains2) {
						problematicItems.add(item1);
						return 1;
					} else {
						boolean item1First = problematicItems.indexOf(item1)<problematicItems.indexOf(item2);
						return item1First?-1:1;
					}
				}
			}
		}
		return result;
	}
	
}

