package treegraphics_awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import treegraphics.viewport.AbstractCachedViewport;

public class AwtCachedViewport extends AbstractCachedViewport implements AwtViewport {

	protected Component component;

	public AwtCachedViewport() {
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
				AwtCachedViewport.this.repaint(bufferedImage.createGraphics());
				g.drawImage(bufferedImage, 0, 0, width, height, 0, 0, width, height, null);
			}
			
		};
	}

	protected void repaint(Graphics2D g) {
		// TODO
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
	public void rebuild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
