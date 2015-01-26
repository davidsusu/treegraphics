package treegraphics.canvas;

public interface Canvas {

	public void setOrigin(Point origin);
	
	public Point getOrigin();
	
	public void setZoom(double zoom);
	
	public double getZoom();
	
	public void resetTransform();
	
	public void setColor(Color color);
	
	public void fillRectangle(Rectangle rectangle);
	
	// TODO
	
}
