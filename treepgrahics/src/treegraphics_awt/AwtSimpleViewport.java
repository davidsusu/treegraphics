package treegraphics_awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Rectangle;
import treegraphics.viewport.AbstractSimpleViewport;

public class AwtSimpleViewport extends AbstractSimpleViewport implements AwtViewport {

	protected Component component;
	
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
		component.repaint();
	}

}
