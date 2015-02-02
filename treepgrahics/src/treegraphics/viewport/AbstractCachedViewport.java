package treegraphics.viewport;

import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

abstract public class AbstractCachedViewport extends AbstractViewport {

	protected DrawableService drawableService = new IndexedStoreDrawableService();

	protected DrawableService drawableCacheService = new IndexedStoreDrawableService();
	
	protected Rectangle drawableCacheRectangle = new Rectangle(0, 0, 0, 0);
	
	protected BitmapNode renderedBitmapNode = null;
	
	protected Rectangle renderedRectangle = new Rectangle(0, 0, 0, 0);
	
	protected double renderedZoom = 1;
	
	@Override
	public void addDrawable(Drawable drawable) {
		drawableService.addDrawable(drawable);
		if (drawableCacheRectangle.intersects(drawable.getReservedRectangle())) {
			drawableCacheService.addDrawable(drawable);
			// TODO: redraw...
		}
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		drawableService.removeDrawable(drawable);
		// FIXME: talán jobb, ha ez az ifen kívül marad...
		drawableCacheService.removeDrawable(drawable);
		if (drawableCacheRectangle.intersects(drawable.getReservedRectangle())) {
			// TODO: redraw...
		}
	}

	@Override
	public void setOrigin(Point origin) {
		double x = origin.getX();
		double y = origin.getY();
		x = Math.round(x/zoom)*zoom;
		y = Math.round(y/zoom)*zoom;
		this.origin = new Point(x, y);
		refresh();
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
		refresh();
	}

	@Override
	public void refresh() {
		if (renderedBitmapNode==null || !drawableCacheRectangle.contains(getMinimumDrawableCacheArea()) || !getMaximumDrawableCacheArea().contains(drawableCacheRectangle)) {
			drawableCacheService.clear();
			List<Drawable> affectedDrawables = drawableService.getAffectedDrawables(getIdealDrawableCacheArea());
			for (Drawable drawable: affectedDrawables) {
				drawableCacheService.addDrawable(drawable);
			}
			fullRerender();
		} else if (zoom!=renderedZoom) {
			fullRerender();
		} else if (!renderedRectangle.contains(getMinimumRenderingArea()) || !getMaximumRenderingArea().contains(renderedRectangle)) {
			partialRerender();
		}
		refreshScreen();
	}

	@Override
	public List<Drawable> getDrawablesAt(Point point) {
		if (drawableCacheRectangle.containsPoint(point)) {
			return drawableCacheService.getAffectedDrawables(point);
		} else {
			return drawableService.getAffectedDrawables(point);
		}
	}

	@Override
	public List<Drawable> getDrawablesAtPixel(int x, int y) {
		Point point =  new Point(((x+0.5)/zoom)+origin.getX(), ((y+0.5)/zoom)+origin.getY());
		return getDrawablesAt(point);
	}

	protected Rectangle getMinimumRenderingArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()/2, area.getHeight()/2);
	}

	protected Rectangle getIdealRenderingArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()/2+100, area.getHeight()/2+100);
	}

	protected Rectangle getMaximumRenderingArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()+100, area.getHeight()+100);
	}
	
	protected Rectangle getMinimumDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*2+100, area.getHeight()*2+100);
	}

	protected Rectangle getIdealDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*3+100, area.getHeight()*3+100);
	}

	protected Rectangle getMaximumDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*4+100, area.getHeight()*4+100);
	}
	
	protected Rectangle getPaddedRectangle(Rectangle rectangle, double xPadding, double yPadding) {
		Point leftTop = new Point(rectangle.getLeft()-xPadding, rectangle.getTop()-yPadding);
		Dimension dimension = new Dimension(rectangle.getWidth()+(xPadding*2), rectangle.getHeight()+(yPadding*2));
		return new Rectangle(leftTop, dimension);
	}
	
	protected void drawToBitmapNode(Rectangle rectangle, BitmapNode bitmapNode) {
		List<Drawable> drawables = drawableCacheService.getAffectedDrawables(rectangle);
		Canvas canvas = bitmapNode.getCanvas();
		canvas.setOrigin(rectangle.getLeftTop());
		canvas.setZoom(zoom);
		for (Drawable drawable: drawables) {
			drawable.draw(canvas);
		}
	}
	
	protected void partialRerender() {
		// TODO
		fullRerender();
	}
	
	protected void fullRerender() {
		Rectangle targetArea = getIdealRenderingArea();
		int bitmapWidth = (int)(targetArea.getWidth()*zoom);
		int bitmapHeight = (int)(targetArea.getHeight()*zoom);
		renderedBitmapNode = createBitmap(bitmapWidth, bitmapHeight);
		renderedRectangle = targetArea;
		renderedZoom = zoom;
		List<Drawable> drawables = drawableCacheService.getAffectedDrawables(targetArea);
		
		Canvas canvas = renderedBitmapNode.getCanvas();
		canvas.setZoom(zoom);
		
		Point bitmapOrigin = targetArea.getLeftTop();
		canvas.setOrigin(bitmapOrigin);
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(renderedRectangle);
		
		for (Drawable drawable: drawables) {
			drawable.draw(canvas);
		}
	}

	abstract protected void refreshScreen();
	
	abstract protected BitmapNode createBitmap(int width, int height);
	
	protected interface BitmapNode {

		public BitmapNode createParent(int width, int height);

		public BitmapNode createChild(int width, int height);
		
		public void copyToParent(int left, int top, int width, int height, int parentLeft, int parentTop);
		
		public Canvas getCanvas();
		
		public int getWidth();
		
		public int getHeight();
		
	}
	
}
