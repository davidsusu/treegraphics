package treegraphics.valuetree;

public interface SourceCustomValue<T> extends CustomValue<T> {

	// FIXME: return ExpireEvent?
	public void set(T value);

	// TODO: set(value, ev)
	
}
