package treegraphics.util;

// TODO: "mixin" for it
public interface CachedState {
	
	public void expireState();
	
	public void expireState(CachedState cachedState);
	
	public void registerDependent(CachedState cachedState);
	
	public void unregisterDependent(CachedState cachedState);
	
	public void freeFromDependecies();
	
}
