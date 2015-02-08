package treegraphics.util;

import treegraphics.canvas.Rectangle;

public class RectangleSplit {

	protected final Rectangle baseRectangle;
	
	protected final Rectangle splitterRectangle;
	
	protected Rectangle leftTopRectangle = null;
	
	protected Rectangle centerTopRectangle = null;
	
	protected Rectangle rightTopRectangle = null;

	protected Rectangle leftMiddleRectangle = null;
	
	protected Rectangle centerMiddleRectangle = null;

	protected Rectangle rightMiddleRectangle = null;

	protected Rectangle leftBottomRectangle = null;

	protected Rectangle centerBottomRectangle = null;

	protected Rectangle rightBottomRectangle = null;
	
	public RectangleSplit(Rectangle baseRectangle, Rectangle splitterRectangle) {
		this.baseRectangle = baseRectangle;
		this.splitterRectangle = splitterRectangle;
		
		if (!baseRectangle.intersects(splitterRectangle)) {
			return;
		}
		
		centerMiddleRectangle = baseRectangle.getIntersectionWith(splitterRectangle);
		
		double baseLeft = baseRectangle.getLeft();
		double baseTop = baseRectangle.getTop();
		double baseRight = baseRectangle.getRight();
		double baseBottom = baseRectangle.getBottom();
		double splitterLeft = splitterRectangle.getLeft();
		double splitterTop = splitterRectangle.getTop();
		double splitterRight = splitterRectangle.getRight();
		double splitterBottom = splitterRectangle.getBottom();
		double innerLeft = centerMiddleRectangle.getLeft();
		double innerTop = centerMiddleRectangle.getTop();
		double innerRight = centerMiddleRectangle.getRight();
		double innerBottom = centerMiddleRectangle.getBottom();

		boolean hasInnerLeft = (splitterLeft>baseLeft);
		boolean hasInnerTop = (splitterTop>baseTop);
		boolean hasInnerRight = (splitterRight<baseRight);
		boolean hasInnerBottom = (splitterBottom<baseBottom);
		
		if (hasInnerTop) {
			if (hasInnerLeft) {
				leftTopRectangle = new Rectangle(baseLeft, baseTop, splitterLeft, splitterTop);
			}
			centerTopRectangle = new Rectangle(innerLeft, baseTop, innerRight, splitterTop);
			if (hasInnerRight) {
				rightTopRectangle = new Rectangle(splitterRight, baseTop, baseRight, splitterTop);
			}
		}
		
		if (hasInnerLeft) {
			leftMiddleRectangle = new Rectangle(baseLeft, innerTop, splitterLeft, innerBottom);
		}
		
		if (hasInnerRight) {
			rightMiddleRectangle = new Rectangle(splitterRight, innerTop, baseRight, innerBottom);
		}

		if (hasInnerBottom) {
			if (hasInnerLeft) {
				leftBottomRectangle = new Rectangle(baseLeft, splitterBottom, splitterLeft, baseBottom);
			}
			centerBottomRectangle = new Rectangle(innerLeft, splitterBottom, innerRight, baseBottom);
			if (hasInnerRight) {
				rightBottomRectangle = new Rectangle(splitterRight, innerBottom, baseRight, baseBottom);
			}
		}
		
	}
	
	public Rectangle getBaseRectangle() {
		return baseRectangle;
	}
	
	public Rectangle getSplitterRectangle() {
		return splitterRectangle;
	}
	
	public Rectangle getLeftTopRectangle() {
		return leftTopRectangle;
	}
	
	public Rectangle getCenterTopRectangle() {
		return centerTopRectangle;
	}
	
	public Rectangle getRightTopRectangle() {
		return rightTopRectangle;
	}

	public Rectangle getLeftMiddleRectangle() {
		return leftMiddleRectangle;
	}
	
	public Rectangle getCenterMiddleRectangle() {
		return centerMiddleRectangle;
	}

	public Rectangle getRightMiddleRectangle() {
		return rightMiddleRectangle;
	}

	public Rectangle getLeftBottomRectangle() {
		return leftBottomRectangle;
	}

	public Rectangle getCenterBottomRectangle() {
		return centerBottomRectangle;
	}

	public Rectangle getRightBottomRectangle() {
		return rightBottomRectangle;
	}
	
	public Rectangle getTopRectangle() {
		if (leftTopRectangle!=null && centerTopRectangle!=null) {
			return leftTopRectangle.getExtendedWith(centerTopRectangle);
		} else if (leftTopRectangle!=null) {
			return leftTopRectangle;
		} else if (centerTopRectangle!=null) {
			return centerTopRectangle;
		} else {
			return null;
		}
	}
	
	public Rectangle getRightRectangle() {
		if (rightTopRectangle!=null && rightMiddleRectangle!=null) {
			return rightTopRectangle.getExtendedWith(rightMiddleRectangle);
		} else if (rightTopRectangle!=null) {
			return rightTopRectangle;
		} else if (rightMiddleRectangle!=null) {
			return rightMiddleRectangle;
		} else {
			return null;
		}
	}
	
	public Rectangle getBottomRectangle() {
		if (rightBottomRectangle!=null && centerBottomRectangle!=null) {
			return rightBottomRectangle.getExtendedWith(centerBottomRectangle);
		} else if (rightBottomRectangle!=null) {
			return rightBottomRectangle;
		} else if (centerBottomRectangle!=null) {
			return centerBottomRectangle;
		} else {
			return null;
		}
	}
	
	public Rectangle getLeftRectangle() {
		if (leftBottomRectangle!=null && leftMiddleRectangle!=null) {
			return leftBottomRectangle.getExtendedWith(leftMiddleRectangle);
		} else if (leftBottomRectangle!=null) {
			return leftBottomRectangle;
		} else if (leftMiddleRectangle!=null) {
			return leftMiddleRectangle;
		} else {
			return null;
		}
	}
	
}
