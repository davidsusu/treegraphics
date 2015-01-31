package treegraphics.util;

public interface Identified {
	
	public int getIdentifier();
	
	// a nested class for "simulate" static method getNextId()
	// (Java currently not supports static methods in interfaces)
	public final class Id {
		
		private static int nextId = 1;
		
		private Id() {
		}
		
		public static int getNext() {
			return nextId++;
		}
		
	}
	
}

