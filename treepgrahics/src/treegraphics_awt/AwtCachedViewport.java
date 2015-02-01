package treegraphics_awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Rectangle;
import treegraphics.viewport.AbstractCachedViewport;

public class AwtCachedViewport extends AbstractCachedViewport implements AwtViewport {

	protected Component component;

	public AwtCachedViewport() {
		initComponent();
	}
	
	protected void initComponent() {
		this.component = new Panel() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void update(Graphics g) {
				paint(g);
			}
			
			@Override
			public void paint(Graphics g) {
				// TODO
			}
			
		};
	}

	protected void repaint(Graphics2D g, Rectangle area) {
		Canvas canvas = new Graphics2DCanvas(g);
		
		// FIXME
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(new Rectangle(0, 0, getWidth(), getHeight()));
		
		canvas.setOrigin(origin);
		canvas.setZoom(zoom);
		canvas.setAntialiasing(true);
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(area);
		
		for (Drawable drawable: drawableService.getAffectedDrawables(area)) {
			drawable.draw(canvas);
		}
	}
	
	@Override
	public Component getComponent() {
		return component;
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
	public void refresh() {
		// FIXME
	}

}
