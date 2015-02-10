package treegraphics.valuetree;

import java.util.ArrayList;
import java.util.List;

import treegraphics.util.CachedState;

abstract public class AbstractValue implements Value {

	protected boolean isExpired = true;
	
	final protected List<CachedState> expiredDependencies = new ArrayList<CachedState>();
	
	final protected List<CachedState> dependents = new ArrayList<CachedState>();

	@Override
	public ExpireEvent expireState() {
		ExpireEvent ev = new ExpireEvent(this);
		isExpired = true;
		for (CachedState dependent: dependents) {
			// FIXME
			//dependent.expireState(ev);
			dependent.expireState_old(this);
		}
		for (CachedState dependent: dependents) {
			dependent.onExpirationFinished(ev);
		}
		return ev;
	}

	@Override
	public void expireState(ExpireEvent ev) {
		isExpired = true;
		for (CachedState dependent: dependents) {
			// FIXME
			//dependent.expireState(ev);
			dependent.expireState_old(this);
		}
	}

	@Override
	public void onExpirationFinished(ExpireEvent ev) {
		for (CachedState dependent: dependents) {
			dependent.onExpirationFinished(ev);
		}
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

	protected void beforeGet() {
		if (isExpired) {
			reload();
			expiredDependencies.clear();
			isExpired = false;
		}
	}
	
	abstract protected void reload();

	
	
	
	
	

	// TODO: remove
	@Override
	public void expireState_old() {
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState_old(this);
		}
	}

	@Override
	public void expireState_old(CachedState cachedState) {
		expiredDependencies.add(cachedState);
		expireState_old();
	}
}
