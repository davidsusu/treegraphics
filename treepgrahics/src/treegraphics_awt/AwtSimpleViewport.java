package treegraphics_awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;

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
				AwtSimpleViewport.this.repaint(new Graphics2DCanvas(bufferedImage.createGraphics()));
				g.drawImage(bufferedImage, 0, 0, width, height, 0, 0, width, height, null);
			}
			
		};
	}

	@Override
	public Component getComponent() {
		return component;
	}
	
	@Override
	public int getFullWidth() {
		return component.getWidth();
	}

	@Override
	public int getFullHeight() {
		return component.getHeight();
	}

	@Override
	public void refresh() {
		component.repaint();
	}

}
