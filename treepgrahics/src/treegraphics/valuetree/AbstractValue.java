package treegraphics.valuetree;

import java.util.List;
import java.util.ArrayList;

abstract public class AbstractValue implements Value {

	protected boolean isExpired = true;
	
	protected double cachedValue = 0;
	
	final protected List<CachedState> expiredDependencies = new ArrayList<CachedState>();
	
	final protected List<CachedState> dependents = new ArrayList<CachedState>();
	
	@Override
	public void expireState() {
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState(this);
		}
	}

	@Override
	public void expireState(CachedState cachedState) {
		expiredDependencies.add(cachedState);
		expireState();
	}

	@Override
	public void registerDependent(CachedState cachedState) {
		// FIXME
		if (!dependents.contains(cachedState)) {
			dependents.add(cachedState);
		}
	}

	@Override
	public void unregisterDependent(CachedState cachedState) {
		dependents.remove(cachedState);
	}

	@Override
	public double get() {
		if (isExpired) {
			reload();
			expiredDependencies.clear();
			isExpired = false;
		}
		return cachedValue;
	}
	
	abstract protected void reload();

}
