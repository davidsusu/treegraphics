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
				AwtCachedViewport.this.repaintScreen((Graphics2D)g);
			}
			
		};
	}

	protected void repaintScreen(Graphics2D g) {
		if (renderedBitmapNode==null) {
			refresh();
		}
		Rectangle area = getArea();
		BufferedImage image = ((Graphics2DBitmapNode)renderedBitmapNode).getImage();
		int width = image.getWidth();
		int height = image.getHeight();
		int x = (int)(renderedRectangle.getLeft()-area.getLeft());
		int y = (int)(renderedRectangle.getTop()-area.getTop());
		g.drawImage(image, x, y, x+width, y+height, 0, 0, width, height, null);
	}
	
	protected void repaintArea(Graphics2D g, Rectangle area) {
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
	public void refreshScreen() {
		component.repaint();
	}

	@Override
	protected BitmapNode createBitmap(int width, int height) {
		return new Graphics2DBitmapNode(width, height, null);
	}
	
	public class Graphics2DBitmapNode implements BitmapNode {

		protected Graphics2DBitmapNode parentBitmapNode;
		
		protected BufferedImage image;

		public Graphics2DBitmapNode(int width, int height, Graphics2DBitmapNode parentBitmapNode) {
			this.parentBitmapNode = parentBitmapNode;
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}

		@Override
		public BitmapNode createParent(int width, int height) {
			parentBitmapNode = new Graphics2DBitmapNode(width, height, null);
			return parentBitmapNode;
		}

		@Override
		public BitmapNode createChild(int width, int height) {
			return new Graphics2DBitmapNode(width, height, this);
		}

		@Override
		public void copyToParent(int left, int top, int width, int height, int parentLeft, int parentTop) {
			if (parentBitmapNode!=null) {
				Graphics2D g2d = parentBitmapNode.image.createGraphics();
				g2d.drawImage(image, left, top, width, height, parentLeft, parentTop, parentLeft+width, parentTop+height, null);
			}
		}

		@Override
		public Canvas getCanvas() {
			return new Graphics2DCanvas(image.createGraphics());
		}
		
		@Override
		public int getWidth() {
			return image.getWidth();
		}
		
		@Override
		public int getHeight() {
			return image.getHeight();
		}
		
		public BufferedImage getImage() {
			return image;
		}

	}
	
}
