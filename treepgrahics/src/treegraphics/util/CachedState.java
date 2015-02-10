package treegraphics.util;

// TODO: "mixin" for it
public interface CachedState {
	
	public ExpireEvent expireState();
	
	public void expireState(ExpireEvent ev);

	public void onExpirationFinished(ExpireEvent ev);
	
	public void registerDependent(CachedState cachedState);
	
	public void unregisterDependent(CachedState cachedState);
	
	public void freeFromDependecies();
	
	

	// TODO: remove
	public void expireState_old();
	public void expireState_old(CachedState cachedState);
	
	
	
	public static class ExpireEvent implements Identified {
		
		protected int id = Identified.Id.getNext();
		
		protected final CachedState cachedState;
		
		public ExpireEvent(CachedState cachedState) {
			this.cachedState = cachedState;
		}
		
		public int getIdentifier() {
			return id;
		}
		
		public CachedState getCachedStateObject() {
			return cachedState;
		}
		
	}
	
}
