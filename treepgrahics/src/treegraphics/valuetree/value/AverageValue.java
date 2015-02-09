package treegraphics.valuetree.value;

import java.util.ArrayList;
import java.util.List;

import treegraphics.valuetree.AbstractDoubleValue;
import treegraphics.valuetree.DoubleValue;

public class AverageValue extends AbstractDoubleValue {

	final List<DoubleValue> sources = new ArrayList<DoubleValue>();
	
	public AverageValue(DoubleValue firstSource, DoubleValue... sources) {
		initSources(firstSource, sources);
	}

	public void setSources(DoubleValue firstSource, DoubleValue... sources) {
		for (DoubleValue source: this.sources) {
			source.unregisterDependent(this);
		}
		this.sources.clear();
		initSources(firstSource, sources);
	}
	public void initSources(DoubleValue firstSource, DoubleValue... sources) {
		this.sources.add(firstSource);
		firstSource.registerDependent(this);
		for (DoubleValue source : sources) {
			this.sources.add(source);
			source.registerDependent(this);
		}
	}
	
	@Override
	public void freeFromDependecies() {
		for (DoubleValue source: sources) {
			source.unregisterDependent(this);
		}
	}

	@Override
	protected void reload() {
		double sum = 0;
		for (DoubleValue source: sources) {
			sum += source.get();
		}
		cachedValue = sum/sources.size();
	}

}
