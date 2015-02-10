package treegraphics.valuetree.doublevalue;

import treegraphics.valuetree.AbstractDoubleValue;
import treegraphics.valuetree.SourceDoubleValue;

public class StaticValue extends AbstractDoubleValue implements SourceDoubleValue {
	
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
