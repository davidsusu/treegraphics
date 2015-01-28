package treegraphics_swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.viewport.Viewport;

public class SimpleSwingViewport implements Viewport {
	
	protected final Component component;
	
	protected List<Drawable> drawables = new ArrayList<Drawable>();
	
	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;
	
	public SimpleSwingViewport() {
		// FIXME
		this.component = new JPanel() {
			
			public static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				Canvas canvas = new Graphics2DCanvas((Graphics2D)g);
				canvas.setOrigin(origin);
				canvas.setZoom(zoom);
				canvas.setAntialiasing(true);
				
				canvas.setColor(new Color(255, 255, 255));
				canvas.fillRectangle(SimpleSwingViewport.this.getArea());
				
				for (Drawable drawable: drawables) {
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
		if (!drawables.contains(drawable)) {
			drawables.add(drawable);
		}
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		drawables.remove(drawable);
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
		Dimension dimension = new Dimension(component.getWidth()/zoom, component.getHeight()/zoom);
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
	
}

