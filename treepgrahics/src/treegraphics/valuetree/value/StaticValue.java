package treegraphics.valuetree.value;

import treegraphics.valuetree.AbstractValue;

public class StaticValue extends AbstractValue {
	
	protected double value;
	
	public StaticValue(double value) {
		this.value = value;
	}
	
	public void set(double value) {
		this.value = value;
		expireState();
	}
	
	@Override
	protected void reload() {
		cachedValue = value;
	}

	@Override
	public void freeFromDependecies() {
		// nothing to do
	}
	
}
