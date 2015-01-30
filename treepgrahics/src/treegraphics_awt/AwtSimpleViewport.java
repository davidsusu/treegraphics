package treegraphics_awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.viewport.AbstractSimpleViewport;
import treegraphics.viewport.DrawableService;
import treegraphics.viewport.IndexedStoreDrawableService;

public class AwtSimpleViewport extends AbstractSimpleViewport {

	protected Component component;
	
	// FIXME
	protected DrawableService drawableService = new IndexedStoreDrawableService();
	
	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;
	
	public AwtSimpleViewport() {
		initComponent();
	}
	
	protected void initComponent() {
		this.component = new Panel() {
			
			private static final long serialVersionUID = 1L;
			
			protected BufferedImage bufferedImage = null;
			
			protected int previousWidth = 0;
			
			protected int previousHeight = 0;
			
			@Override
			public void update(Graphics g) {
				paint(g);
			}
			
			@Override
			public void paint(Graphics g) {
				int width = getWidth();
				int height = getHeight();
				if (bufferedImage==null || previousWidth!=width || previousHeight!=height) {
					bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					previousWidth = width;
					previousHeight = height;
				}
				AwtSimpleViewport.this.repaint(bufferedImage.createGraphics());
				g.drawImage(bufferedImage, 0, 0, width, height, 0, 0, width, height, null);
			}
			
		};
	}
	
	protected void repaint(Graphics2D g) {
		Canvas canvas = new Graphics2DCanvas(g);
		
		// FIXME
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(new Rectangle(0, 0, getWidth(), getHeight()));
		
		canvas.setOrigin(origin);
		canvas.setZoom(zoom);
		canvas.setAntialiasing(true);

		Rectangle area = getArea();
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(area);
		
		for (Drawable drawable: drawableService.getAffectedDrawables(area)) {
			drawable.draw(canvas);
		}
	}
	
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
