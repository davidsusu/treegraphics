package treegraphics.viewport;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;

public interface Viewport {

	public void addDrawable(Drawable drawable);
	
	public void removeDrawable(Drawable drawable);

	public void setOrigin(Point point);

	public Point getOrigin();
	
	public void setZoom(double zoom);
	
	public double getZoom();

	public int getWidth();

	public int getHeight();
	
	public void rebuild();
	
	public void refresh();
	
}
