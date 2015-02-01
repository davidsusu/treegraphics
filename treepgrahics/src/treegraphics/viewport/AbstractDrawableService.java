package treegraphics.viewport;

import java.util.ArrayList;
import java.util.List;

import treegraphics.util.CachedState;

abstract public class AbstractDrawableService implements DrawableService {

	protected boolean isExpired = true;

	protected final List<CachedState> expiredDependencies = new ArrayList<CachedState>();
	
	protected final List<CachedState> dependents = new ArrayList<CachedState>();

	@Override
	public void expireState() {
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState(this);
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
	
}
