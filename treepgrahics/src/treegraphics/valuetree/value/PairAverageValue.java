package treegraphics.valuetree.value;

import treegraphics.valuetree.AbstractValue;
import treegraphics.valuetree.Value;

public class PairAverageValue extends AbstractValue {

	protected Value source1;
	
	protected Value source2;
	
	public PairAverageValue(Value source1, Value source2) {
		initSources(source1, source2);
	}

	public void setSources(Value source1, Value source2) {
		this.source1.unregisterDependent(this);
		this.source2.unregisterDependent(this);
		initSources(source1, source2);
	}
	
	protected void initSources(Value source1, Value source2) {
		this.source1 = source1;
		this.source2 = source2;
		source1.registerDependent(this);
		source2.registerDependent(this);
	}

	@Override
	protected void reload() {
		cachedValue = (source1.get()+source2.get())/2;
	}

	@Override
	public void freeFromDependecies() {
		source1.unregisterDependent(this);
		source2.unregisterDependent(this);
	}
	
}
