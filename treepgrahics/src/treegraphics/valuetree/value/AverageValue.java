package treegraphics.valuetree.value;

import java.util.ArrayList;
import java.util.List;

import treegraphics.valuetree.AbstractValue;
import treegraphics.valuetree.Value;

public class AverageValue extends AbstractValue {

	final List<Value> sources = new ArrayList<Value>();
	
	public AverageValue(Value firstSource, Value... sources) {
		initSources(firstSource, sources);
	}

	public void setSources(Value firstSource, Value... sources) {
		for (Value source: this.sources) {
			source.unregisterDependent(this);
		}
		this.sources.clear();
		initSources(firstSource, sources);
	}
	public void initSources(Value firstSource, Value... sources) {
		this.sources.add(firstSource);
		firstSource.registerDependent(this);
		for (Value source : sources) {
			this.sources.add(source);
			source.registerDependent(this);
		}
	}
	
	@Override
	protected void reload() {
		double sum = 0;
		for (Value source: sources) {
			sum += source.get();
		}
		cachedValue = sum/sources.size();
	}

	@Override
	public void freeFromDependecies() {
		for (Value source: sources) {
			source.unregisterDependent(this);
		}
	}
	
}
