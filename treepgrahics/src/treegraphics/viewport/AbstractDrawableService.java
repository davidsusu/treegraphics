package treegraphics.viewport;

import java.util.ArrayList;
import java.util.List;

import treegraphics.util.CachedState;

abstract public class AbstractDrawableService implements DrawableService {

	protected boolean isExpired = true;

	protected final List<CachedState> expiredDependencies = new ArrayList<CachedState>();
	
	protected final List<CachedState> dependents = new ArrayList<CachedState>();

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
	
	
	
	
	
	
	
	
	
	// TODO: remove
	
	@Override
	public void expireState_old() {
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState_old(this);
		}
	}
	
}
