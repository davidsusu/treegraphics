package treegraphics_swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.viewport.DrawableService;
import treegraphics.viewport.IndexedStoreDrawableService;
import treegraphics.viewport.Viewport;

public class SimpleSwingViewport implements Viewport {
	
	protected final Component component;
	
	protected DrawableService drawableService = new IndexedStoreDrawableService();
	
	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;
	
	public SimpleSwingViewport() {
		// FIXME
		this.component = new JPanel() {
			
			public static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				Canvas canvas = new Graphics2DCanvas((Graphics2D)g);

				// FIXME
				canvas.setColor(new Color(255, 255, 255));
				canvas.fillRectangle(new Rectangle(0, 0, SimpleSwingViewport.this.getWidth(), SimpleSwingViewport.this.getHeight()));
				
				canvas.setOrigin(origin);
				canvas.setZoom(zoom);
				canvas.setAntialiasing(true);

				Rectangle area = SimpleSwingViewport.this.getArea();
				
				canvas.setColor(new Color(255, 255, 255));
				canvas.fillRectangle(area);
				
				for (Drawable drawable: drawableService.getAffectedDrawables(area)) {
					drawable.draw(canvas);
				}
			}
			
		};
	}
	
	// FIXME
	public Component getComponent() {
		return component;
	}
	
	@Override
	public void addDrawable(Drawable drawable) {
		drawableService.addDrawable(drawable);
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		drawableService.removeDrawable(drawable);
	}

	@Override
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	@Override
	public Point getOrigin() {
		return origin;
	}

	@Override
	public int getWidth() {
		return component.getWidth();
	}

	@Override
	public int getHeight() {
		return component.getHeight();
	}

	@Override
	public Rectangle getArea() {
		//Dimension dimension = new Dimension(component.getWidth()/zoom, component.getHeight()/zoom);
		Dimension dimension = new Dimension((component.getWidth()-100)/zoom, (component.getHeight()-100)/zoom);
		return new Rectangle(origin, dimension);
	}
	
	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	@Override
	public double getZoom() {
		return zoom;
	}

	@Override
	public void rebuild() {
		// TODO: rebuild bitmap, and if necessary then additionally rebuild the active area of the DrawableService
		refresh();
	}

	@Override
	public void refresh() {
		component.repaint();
	}

	public List<Drawable> getDrawablesAt(Point point) {
		List<Drawable> drawablesAt = new ArrayList<Drawable>(drawableService.getAffectedDrawables(point));
		Collections.reverse(drawablesAt);
		return drawablesAt;
	}
	
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		Point virtualPoint =  new Point(((x+0.5)/zoom)+origin.getX(), ((y+0.5)/zoom)+origin.getY());
		return getDrawablesAt(virtualPoint);
	}
	
}

