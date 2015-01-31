package treegraphics.canvas;

import treegraphics.util.CachedState;

public interface Drawable extends CachedState {
	
	public void draw(Canvas canvas);
	
	public double getZ();
	
	public Rectangle getReservedRectangle();
	
	public boolean isPointDominated(Point point);
	
}
