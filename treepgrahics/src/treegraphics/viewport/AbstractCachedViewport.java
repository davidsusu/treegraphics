package treegraphics.viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.RectangleSplit;

abstract public class AbstractCachedViewport extends AbstractViewport {

	protected DrawableService drawableService = new IndexedStoreDrawableService();

	protected DrawableService drawableCacheService = null;
	
	protected Rectangle drawableCacheRectangle = null;
	
	protected BitmapNode renderedBitmapNode = null;
	
	protected Rectangle renderedRectangle = new Rectangle(0, 0, 0, 0);
	
	protected double renderedZoom = 1;
	
	protected int partialRerenderCount = 0;
	
	protected int maxPartialRerenderCount = 40;
	
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
		refreshScreen();
	}
	
	@Override
	public void rebuild() {
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

	protected Rectangle getMinimumRenderingArea() {
		Rectangle area = getArea();
		return getPaddedToPixelArea(getPaddedRectangle(area, area.getWidth()/2, area.getHeight()/2));
	}

	protected Rectangle getOptimalRenderingArea() {
		Rectangle area = getArea();
		return getPaddedToPixelArea(getPaddedRectangle(area, area.getWidth()/2+(100/zoom), area.getHeight()/2+(100/zoom)));
	}

	protected Rectangle getMaximumRenderingArea() {
		Rectangle area = getArea();
		return getPaddedToPixelArea(getPaddedRectangle(area, area.getWidth()+(100/zoom), area.getHeight()+(100/zoom)));
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

	protected Rectangle getPaddedToPixelArea(Rectangle rectangle) {
		double left = Math.floor(rectangle.getLeft()*zoom)/zoom;
		double top = Math.floor(rectangle.getTop()*zoom)/zoom;
		double right = Math.ceil(rectangle.getRight()*zoom)/zoom;
		double bottom = Math.ceil(rectangle.getBottom()*zoom)/zoom;
		return new Rectangle(new Point(left, top), new Point(right, bottom));
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
	
	protected void rerenderBitmapNode() {
		requireDrawableCache();
		if (
			!drawableCacheRectangle.contains(getMinimumDrawableCacheArea())
			|| !getMaximumDrawableCacheArea().contains(drawableCacheRectangle)
		) {
			drawableCacheRectangle = getOptimalDrawableCacheArea();
			drawableCacheService.clear();
			for (Drawable drawable: drawableService.getAffectedDrawables(drawableCacheRectangle)) {
				drawableCacheService.addDrawable(drawable);
			}
		}
		if (
			renderedBitmapNode==null
			|| zoom!=renderedZoom
			|| partialRerenderCount>maxPartialRerenderCount
		) {
			partialRerenderCount = 0;
			rerenderBitmapNodeFully();
		} else {
			partialRerenderCount++;
			rerenderBitmapNodePartially();
		}
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

	protected void rerenderBitmapNodePartially() {
		Rectangle targetArea = getOptimalRenderingArea();
		
		if (!targetArea.intersects(renderedRectangle)) {
			rerenderBitmapNodeFully();
			return;
		}
		
		RectangleSplit areaSplit = new RectangleSplit(targetArea, renderedRectangle);
		Rectangle topArea = areaSplit.getTopRectangle();
		Rectangle rightArea = areaSplit.getRightRectangle();
		Rectangle bottomArea = areaSplit.getBottomRectangle();
		Rectangle leftArea = areaSplit.getLeftRectangle();
		Rectangle intersectionArea = areaSplit.getCenterMiddleRectangle();
		
		int bitmapWidth = (int)(targetArea.getWidth()*zoom);
		int bitmapHeight = (int)(targetArea.getHeight()*zoom);
		BitmapNode newBitmapNode = renderedBitmapNode.createParent(bitmapWidth, bitmapHeight);
		
		Canvas canvas = newBitmapNode.getCanvas();
		canvas.setZoom(zoom);
		canvas.setAntialiasing(true);

		Point bitmapOrigin = targetArea.getLeftTop();
		canvas.setOrigin(bitmapOrigin);
		
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRectangle(targetArea);
		
		Set<Drawable> drawableSet = new HashSet<Drawable>();
		if (topArea!=null) {
			drawableSet.addAll(drawableCacheService.getAffectedDrawables(topArea));
		}
		if (rightArea!=null) {
			drawableSet.addAll(drawableCacheService.getAffectedDrawables(rightArea));
		}
		if (bottomArea!=null) {
			drawableSet.addAll(drawableCacheService.getAffectedDrawables(bottomArea));
		}
		if (leftArea!=null) {
			drawableSet.addAll(drawableCacheService.getAffectedDrawables(leftArea));
		}
		List<Drawable> drawables = new ArrayList<Drawable>(drawableSet);
		Collections.sort(drawables, new IndexedStoreDrawableService.ZDrawableComparator());
		
		for (DrawListener drawListener: drawListeners) {
			drawListener.beforeDraw(canvas, renderedRectangle);
		}
		
		for (Drawable drawable: drawables) {
			drawable.draw(canvas);
		}
		
		for (DrawListener drawListener: drawListeners) {
			drawListener.afterDraw(canvas, renderedRectangle);
		}
		
		double intersectionAreaLeft = intersectionArea.getLeft();
		double intersectionAreaTop = intersectionArea.getTop();
		double intersectionAreaRight = intersectionArea.getRight();
		double intersectionAreaBottom = intersectionArea.getBottom();
		int intersectionTargetLeft = (int)Math.round((intersectionAreaLeft-targetArea.getLeft())*zoom);
		int intersectionRenderedLeft = (int)Math.round((intersectionAreaLeft-renderedRectangle.getLeft())*zoom);
		int intersectionTargetTop = (int)Math.round((intersectionAreaTop-targetArea.getTop())*zoom);
		int intersectionRenderedTop = (int)Math.round((intersectionAreaTop-renderedRectangle.getTop())*zoom);
		int intersectionRenderedWidth = (int)Math.round(intersectionAreaRight-intersectionAreaLeft);
		int intersectionRenderedHeight = (int)Math.round(intersectionAreaBottom-intersectionAreaTop);
		renderedBitmapNode.copyToParent(
			intersectionRenderedLeft,
			intersectionRenderedTop,
			intersectionRenderedWidth,
			intersectionRenderedHeight,
			intersectionTargetLeft,
			intersectionTargetTop
		);
		
		renderedBitmapNode = newBitmapNode;
		renderedRectangle = targetArea;
		renderedZoom = zoom;
	}
	
	protected void requireDrawableCache() {
		if (drawableCacheService==null) {
			drawableCacheService = new IndexedStoreDrawableService();
			drawableCacheRectangle = getOptimalDrawableCacheArea();
			for (Drawable drawable: drawableService.getAffectedDrawables(drawableCacheRectangle)) {
				drawableCacheService.addDrawable(drawable);
			}
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
