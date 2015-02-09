package treegraphics.valuetree;

abstract public class AbstractDoubleValue extends AbstractValue implements DoubleValue {

	protected double cachedValue = 0;

	@Override
	public double get() {
		beforeGet();
		return cachedValue;
	}
	
}
