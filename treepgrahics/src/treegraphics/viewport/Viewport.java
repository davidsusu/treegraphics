package treegraphics.viewport;

import java.util.List;

import treegraphics.canvas.Canvas;
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

	public int getXDisplacement();

	public int getYDisplacement();

	public Rectangle getArea();

	public int getFullWidth();

	public int getFullHeight();

	public Rectangle getFullArea();

	public void refresh();

	public void rebuild();
	
	public List<Drawable> getDrawablesAt(Point point);
	
	public List<Drawable> getDrawablesAtPixel(int x, int y);

	public void addDrawListener(DrawListener drawListener);

	public void removeDrawListener(DrawListener drawListener);
	
	public interface DrawListener {

		public void beforeRefresh(Canvas canvas, Rectangle area);
		
		public void beforeDraw(Canvas canvas, Rectangle area);

		public void afterDraw(Canvas canvas, Rectangle area);

		public void afterRefresh(Canvas canvas, Rectangle area);
		
	}
}
