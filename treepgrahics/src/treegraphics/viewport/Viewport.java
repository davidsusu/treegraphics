package treegraphics.viewport;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

public interface Viewport {

	public void addDrawable(Drawable drawable);
	
	public void removeDrawable(Drawable drawable);

	public void setOrigin(Point origin);

	public Point getOrigin();
	
	public void setZoom(double zoom);
	
	public double getZoom();

	public int getWidth();

	public int getHeight();

	public Rectangle getArea();
	
	public void rebuild();
	
	public void refresh();
	
}
