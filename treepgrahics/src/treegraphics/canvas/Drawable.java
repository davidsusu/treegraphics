package treegraphics.canvas;

public interface Drawable {
	
	public void draw(Canvas canvas);
	
	public double getZ();
	
	public Rectangle getReservedRectangle();
	
	public boolean isPointDominated(Point point);
	
}
