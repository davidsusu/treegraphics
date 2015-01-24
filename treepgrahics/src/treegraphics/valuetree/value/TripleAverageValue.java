package treegraphics.valuetree.value;

import treegraphics.valuetree.AbstractValue;
import treegraphics.valuetree.Value;

public class TripleAverageValue extends AbstractValue {

	protected Value source1;
	
	protected Value source2;
	
	protected Value source3;
	
	public TripleAverageValue(Value source1, Value source2, Value source3) {
		initSources(source1, source2, source3);
	}
	
	public void setSources(Value source1, Value source2, Value source3) {
		this.source1.unregisterDependent(this);
		this.source2.unregisterDependent(this);
		this.source3.unregisterDependent(this);
		initSources(source1, source2, source3);
	}
	
	protected void initSources(Value source1, Value source2, Value source3) {
		this.source1 = source1;
		this.source2 = source2;
		this.source3 = source3;
		source1.registerDependent(this);
		source2.registerDependent(this);
		source3.registerDependent(this);
	}

	@Override
	public void freeFromDependecies() {
		source1.unregisterDependent(this);
		source2.unregisterDependent(this);
		source3.unregisterDependent(this);
	}

	@Override
	protected void reload() {
		cachedValue = (source1.get()+source2.get()+source3.get())/3;
	}

}
