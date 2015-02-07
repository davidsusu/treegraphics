package treegraphics_awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Point;
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
		// TODO
		if (renderedBitmapNode==null) {
			// FIXME
			return;
		}
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		g.setColor(new java.awt.Color(255, 255, 255));
		g.fillRect(getXDisplacement(), getYDisplacement(), getWidth(), getHeight());
		
		BufferedImage image = ((Graphics2DBitmapNode)renderedBitmapNode).getImage();
		
		int targetX = (int)(Math.round((renderedRectangle.getLeft()-origin.getX())*zoom))+getXDisplacement();
		int targetWidth = (int)(renderedBitmapNode.getWidth()*zoom/renderedZoom);
		int targetY = (int)(Math.round((renderedRectangle.getTop()-origin.getY())*zoom))+getYDisplacement();
		int targetHeight = (int)(renderedBitmapNode.getHeight()*zoom/renderedZoom);

		int xDisplacement = getXDisplacement();
		int yDisplacement = getYDisplacement();
		
		Canvas fullCanvas = new Graphics2DCanvas((Graphics2D)(g.create()));
		Point displacedOrigin = new Point(origin.getX()-(xDisplacement/zoom), origin.getY()-(yDisplacement/zoom));
		fullCanvas.setOrigin(displacedOrigin);
		fullCanvas.setZoom(zoom);
		fullCanvas.setAntialiasing(true);

		Rectangle area = getArea();
		
		for (DrawListener drawListener: drawListeners) {
			drawListener.beforeRefresh(fullCanvas, area);
		}
		
		g.drawImage(image, targetX, targetY, targetX+targetWidth, targetY+targetHeight, 0, 0, image.getWidth(), image.getHeight(), null);

		for (DrawListener drawListener: drawListeners) {
			drawListener.afterRefresh(fullCanvas, area);
		}
		
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
