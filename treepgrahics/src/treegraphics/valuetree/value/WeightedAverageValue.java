package treegraphics.valuetree.value;

import java.util.ArrayList;
import java.util.List;

import treegraphics.valuetree.AbstractDoubleValue;
import treegraphics.valuetree.DoubleValue;

public class WeightedAverageValue extends AbstractDoubleValue {

	final List<Entry> sourceEntries = new ArrayList<Entry>();
	
	public WeightedAverageValue(Entry firstSourceEntry, Entry... sourceEntries) {
		initSources(firstSourceEntry, sourceEntries);
	}
	
	public void setSources(Entry firstSourceEntry, Entry... sourceEntries) {
		for (Entry sourceEntry: this.sourceEntries) {
			sourceEntry.source.unregisterDependent(this);
		}
		this.sourceEntries.clear();
		initSources(firstSourceEntry, sourceEntries);
	}
	
	protected void initSources(Entry firstSourceEntry, Entry... sourceEntries) {
		this.sourceEntries.add(firstSourceEntry);
		firstSourceEntry.source.registerDependent(this);
		for (Entry sourceEntry: sourceEntries) {
			this.sourceEntries.add(sourceEntry);
			sourceEntry.source.registerDependent(this);
		}
	}

	@Override
	public void freeFromDependecies() {
		for (Entry sourceEntry: sourceEntries) {
			sourceEntry.source.unregisterDependent(this);
		}
	}

	@Override
	protected void reload() {
		double cardinality = 0;
		double sum = 0;
		for (Entry sourceEntry: sourceEntries) {
			cardinality += sourceEntry.weight;
			sum += sourceEntry.weight*sourceEntry.source.get();
		}
		cachedValue = sum/cardinality;
	}

	public static class Entry {

		protected final DoubleValue source;

		protected final double weight;
		
		public Entry(DoubleValue source) {
			this(source, 1);
		}

		public Entry(DoubleValue source, double weight) {
			if (weight<=0) {
				throw new RuntimeException("Weight must be greater than zero");
			}
			this.source = source;
			this.weight = weight;
		}
		
	}
	
}
