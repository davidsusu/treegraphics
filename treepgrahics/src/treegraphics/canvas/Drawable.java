package treegraphics.canvas;

public interface Drawable {
	
	public void draw(Canvas canvas);
	
	public Rectangle getReservedRectangle();
	
	public boolean isPointDominated(Point point);
	
}
