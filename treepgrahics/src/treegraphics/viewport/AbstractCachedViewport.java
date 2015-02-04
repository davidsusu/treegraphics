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

	protected DrawableService drawableCacheService = null;
	
	protected Rectangle drawableCacheRectangle = null;
	
	protected BitmapNode renderedBitmapNode = null;
	
	protected Rectangle renderedRectangle = new Rectangle(0, 0, 0, 0);
	
	protected double renderedZoom = 1;
	
	@Override
	public void addDrawable(Drawable drawable) {
		requireDrawableCache();
		drawableService.addDrawable(drawable);
		if (drawableCacheRectangle.intersects(drawable.getReservedRectangle())) {
			drawableCacheService.addDrawable(drawable);
			// TODO: redraw...
		}
	}

	@Override
	public void removeDrawable(Drawable drawable) {
		requireDrawableCache();
		drawableService.removeDrawable(drawable);
		// FIXME: talán jobb, ha ez az ifen kívül marad...
		drawableCacheService.removeDrawable(drawable);
		if (drawableCacheRectangle.intersects(drawable.getReservedRectangle())) {
			// TODO: redraw...
		}
	}

	@Override
	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	@Override
	public void refresh() {
		// FIXME
		if (renderedBitmapNode==null) {
			rerenderBitmapNode();
		}
		refreshScreen();
	}
	
	@Override
	public void rebuild() {
		// FIXME
		rerenderBitmapNode();
		refreshScreen();
	}

	@Override
	public List<Drawable> getDrawablesAt(Point point) {
		requireDrawableCache();
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

	protected Rectangle getOptimalRenderingArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()/2+(100/zoom), area.getHeight()/2+(100/zoom));
	}

	protected Rectangle getMaximumRenderingArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()+(100/zoom), area.getHeight()+(100/zoom));
	}
	
	protected Rectangle getMinimumDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*2+(100/zoom), area.getHeight()*2+(100/zoom));
	}

	protected Rectangle getOptimalDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*3+(100/zoom), area.getHeight()*3+(100/zoom));
	}

	protected Rectangle getMaximumDrawableCacheArea() {
		Rectangle area = getArea();
		return getPaddedRectangle(area, area.getWidth()*4+(100/zoom), area.getHeight()*4+(100/zoom));
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
	
	// FIXME: partial rerender...
	protected void rerenderBitmapNode() {
		rerenderBitmapNodeFully();
	}

	protected void rerenderBitmapNodeFully() {
		Rectangle targetArea = getOptimalRenderingArea();
		int bitmapWidth = (int)(targetArea.getWidth()*zoom);
		int bitmapHeight = (int)(targetArea.getHeight()*zoom);
		renderedBitmapNode = createBitmap(bitmapWidth, bitmapHeight);
		renderedRectangle = targetArea;
		renderedZoom = zoom;
		List<Drawable> drawables = drawableCacheService.getAffectedDrawables(targetArea);
		
		Canvas canvas = renderedBitmapNode.getCanvas();
		canvas.setZoom(zoom);
		canvas.setAntialiasing(true);
		
		Point bitmapOrigin = targetArea.getLeftTop();
		canvas.setOrigin(bitmapOrigin);
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(renderedRectangle);
		
		for (DrawListener drawListener: drawListeners) {
			drawListener.beforeDraw(canvas, renderedRectangle);
		}

		for (Drawable drawable: drawables) {
			drawable.draw(canvas);
		}
		
		for (DrawListener drawListener: drawListeners) {
			drawListener.afterDraw(canvas, renderedRectangle);
		}
	}
	
	protected void requireDrawableCache() {
		drawableCacheService = new IndexedStoreDrawableService();
		drawableCacheRectangle = getOptimalDrawableCacheArea();
		for (Drawable drawable: drawableService.getAffectedDrawables(drawableCacheRectangle)) {
			drawableCacheService.addDrawable(drawable);
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
