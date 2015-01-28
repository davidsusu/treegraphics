package treegraphics.viewport;

import treegraphics.canvas.Drawable;

public interface Viewport {

	public void addDrawable(Drawable drawable);
	
	public void removeDrawable(Drawable drawable);

	public void setX(int x);

	public int getX();

	public void setY(int y);

	public int getY();
	
	public void setZoom(double zoom);
	
	public double getZoom();

	public int getWidth();

	public int getHeight();
	
	public void rebuild();
	
	public void refresh();
	
}
