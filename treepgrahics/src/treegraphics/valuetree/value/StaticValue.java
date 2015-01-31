package treegraphics.valuetree.value;

import treegraphics.valuetree.AbstractValue;
import treegraphics.valuetree.SourceValue;

public class StaticValue extends AbstractValue implements SourceValue {
	
	protected double value;
	
	public StaticValue(double value) {
		this.value = value;
	}
	
	public void set(double value) {
		this.value = value;
		expireState();
	}
	
	@Override
	public void freeFromDependecies() {
		// nothing to do
	}

	@Override
	protected void reload() {
		cachedValue = value;
	}

}
