package treegraphics.viewport;

import java.util.ArrayList;
import java.util.List;

import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

abstract public class AbstractViewport implements Viewport {

	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;

	protected List<DrawListener> drawListeners = new ArrayList<DrawListener>();

	@Override
	public Point getOrigin() {
		return origin;
	}

	@Override
	public double getZoom() {
		return zoom;
	}

	@Override
	public Rectangle getArea() {
		Dimension dimension = new Dimension(getWidth()/zoom, getHeight()/zoom);
		return new Rectangle(origin, dimension);
	}

	public Rectangle getFullArea() {
		Point leftTop = new Point(origin.getX()-(getXDisplacement()/zoom), origin.getY()-(getYDisplacement()/zoom));
		Dimension dimension = new Dimension(getFullWidth()/zoom, getFullHeight()/zoom);
		return new Rectangle(leftTop, dimension);
	}
	
	@Override
	public void addDrawListener(DrawListener drawListener) {
		drawListeners.add(drawListener);
	}

	@Override
	public void removeDrawListener(DrawListener drawListener) {
		drawListeners.remove(drawListener);
	}

	@Override
	public int getXDisplacement() {
		return 0;
	}

	@Override
	public int getYDisplacement() {
		return 0;
	}

	@Override
	public int getWidth() {
		return getFullWidth();
	}

	@Override
	public int getHeight() {
		return getFullHeight();
	}
	
	@Override
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		Point point =  new Point(((x+0.5)/zoom)+origin.getX()-(getXDisplacement()/zoom), ((y+0.5)/zoom)+origin.getY()-(getYDisplacement()/zoom));
		return getDrawablesAt(point);
	}

}
