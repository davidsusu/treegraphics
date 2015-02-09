package treegraphics.valuetree;

abstract public class AbstractCustomValue<T> extends AbstractValue implements CustomValue<T> {

	protected T cachedValue = null;

	@Override
	public T get() {
		beforeGet();
		return cachedValue;
	}
	
}
