package treegraphics.canvas;

public interface Canvas {

	public void setOrigin(Point origin);
	
	public Point getOrigin();
	
	public void setZoom(double zoom);
	
	public double getZoom();
	
	public void resetTransform();
	
	public Point transformPoint(Point point);
	
	public Point transformBackPoint(Point point);

	public void setAntialiasing(boolean enabled);
	
	public void setColor(Color color);

	public void fillRectangle(Rectangle rectangle);

	public void drawLine(Point point1, Point point2);
	
	// TODO
	
}
