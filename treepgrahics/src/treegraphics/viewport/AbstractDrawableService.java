package treegraphics.viewport;

import java.util.ArrayList;
import java.util.List;

import treegraphics.util.CachedState;

abstract public class AbstractDrawableService implements DrawableService {

	protected boolean isExpired = true;
	
	protected final List<CachedState> dependents = new ArrayList<CachedState>();
	
	protected int lastExpireEventId = 0;
	
	@Override
	public ExpireEvent expireState() {
		ExpireEvent ev = new ExpireEvent(this);
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState(ev);
		}
		for (CachedState dependent: dependents) {
			dependent.onExpirationFinished(ev);
		}
		return ev;
	}
	
	@Override
	public void expireState(ExpireEvent ev) {
		isExpired = true;
		ev.push(this);
		for (CachedState dependent: dependents) {
			dependent.expireState(ev);
		}
		ev.pop(this);
	}
	
	@Override
	public void onExpirationFinished(ExpireEvent ev) {
		for (CachedState dependent: dependents) {
			dependent.onExpirationFinished(ev);
		}
		int evIdentifier = ev.getIdentifier();
		if (evIdentifier!=lastExpireEventId) {
			onExpirationFinishedOnce(ev);
		}
		lastExpireEventId = evIdentifier;
	}
	
	// FIXME: ezt valahogyan tovabbharitani a Viewportra...
	abstract protected void onExpirationFinishedOnce(ExpireEvent ev);
	
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
	
}
