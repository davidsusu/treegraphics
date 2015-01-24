package treegraphics.valuetree.value;

import java.util.ArrayList;
import java.util.List;

import treegraphics.valuetree.AbstractValue;
import treegraphics.valuetree.Value;

public class WeightedAverageValue extends AbstractValue {

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

		protected final Value source;

		protected final double weight;
		
		public Entry(Value source) {
			this(source, 1);
		}

		public Entry(Value source, double weight) {
			if (weight<=0) {
				throw new RuntimeException("Weight must be greater than zero");
			}
			this.source = source;
			this.weight = weight;
		}
		
	}
	
}
