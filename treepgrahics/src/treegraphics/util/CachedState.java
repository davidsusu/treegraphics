package treegraphics.util;

import java.util.LinkedList;

// TODO: "mixin" for it
public interface CachedState {
	
	public ExpireEvent expireState();
	
	public void expireState(ExpireEvent ev);

	public void onExpirationFinished(ExpireEvent ev);
	
	public void registerDependent(CachedState cachedState);
	
	public void unregisterDependent(CachedState cachedState);
	
	public void freeFromDependecies();
	
	public static class ExpireEvent implements Identified {
		
		protected int id = Identified.Id.getNext();
		
		protected final CachedState cachedState;
		
		protected final LinkedList<CachedState> stack = new LinkedList<CachedState>();
		
		public ExpireEvent(CachedState cachedState) {
			this.cachedState = cachedState;
			push(cachedState);
		}
		
		public int getIdentifier() {
			return id;
		}
		
		public CachedState getSource() {
			return cachedState;
		}

		public CachedState getLast() {
			return stack.getLast();
		}
		
		public boolean push(CachedState cachedState) {
			return stack.add(cachedState);
		}
		
		public boolean pop(CachedState cachedState) {
			if (stack.size()>1 && stack.getLast()==cachedState) {
				stack.removeLast();
				return true;
			} else {
				return false;
			}
		}
		
	}
	
}
