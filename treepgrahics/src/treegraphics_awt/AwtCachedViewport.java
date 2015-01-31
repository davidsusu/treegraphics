package treegraphics_awt;

import java.awt.Component;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.viewport.AbstractCachedViewport;

public class AwtCachedViewport extends AbstractCachedViewport implements AwtViewport {

	protected Component component;

	@Override
	public Component getComponent() {
		return component;
	}
	
	@Override
	public void addDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOrigin(Point origin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getOrigin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setZoom(double zoom) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getZoom() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Rectangle getArea() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rebuild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Drawable> getDrawablesAt(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

}
