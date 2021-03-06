package treegraphics.canvas;

public class Rectangle implements Shape {

	protected final Point point1;

	protected final Point point2;
	
	public Rectangle(Point point1, Point point2) {
		this.point1 = point1;
		this.point2 = point2;
	}

	public Rectangle(Point point, Dimension dimension) {
		this.point1 = point;
		this.point2 = new Point(point.getX()+dimension.getWidth(), point.getY()+dimension.getHeight());
	}

	public Rectangle(double x1, double y1, double x2, double y2) {
		this.point1 = new Point(x1, y1);
		this.point2 = new Point(x2, y2);
	}
	
	public Point getPoint1() {
		return point1;
	}
	
	public Point getPoint2() {
		return point2;
	}
	
	@Override
	public Rectangle getBounds() {
		return this;
	}

	@Override
	public boolean containsPoint(Point point) {
		double selfLeft = getLeft();
		double selfTop = getTop();
		double selfRight = getRight();
		double selfBottom = getBottom();
		double pointX = point.getX();
		double pointY = point.getY();
		return (
			pointX >= selfLeft
			&& pointX <= selfRight
			&& pointY >= selfTop
			&& pointY <= selfBottom
		);
	}
	
	public Point getLeftTop() {
		return new Point(getLeft(), getTop());
	}
	
	public Point getRightTop() {
		return new Point(getRight(), getTop());
	}
	
	public Point getLeftBottom() {
		return new Point(getLeft(), getBottom());
	}
	
	public Point getRightBottom() {
		return new Point(getRight(), getBottom());
	}
	
	public double getLeft() {
		return Math.min(point1.getX(), point2.getX());
	}
	
	public double getTop() {
		return Math.min(point1.getY(), point2.getY());
	}
	
	public double getRight() {
		return Math.max(point1.getX(), point2.getX());
	}

	public double getBottom() {
		return Math.max(point1.getY(), point2.getY());
	}
	
	public double getHorizontalVector() {
		return point2.getX()-point1.getX();
	}
	
	public double getVerticalVector() {
		return point2.getY()-point1.getY();
	}
	
	public double getWidth() {
		return getRight()-getLeft();
	}
	
	public double getHeight() {
		return getBottom()-getTop();
	}
	
	public Dimension getDimension() {
		return new Dimension(getHorizontalVector(), getVerticalVector());
	}
	
	public Dimension getAbsoluteDimension() {
		return new Dimension(getWidth(), getHeight());
	}
	
	public Rectangle getAbsolute() {
		return new Rectangle(getLeftTop(), getRightBottom());
	}

	public double getArea() {
		return getWidth()*getHeight();
	}

	public double getPerimeter() {
		return (getWidth()+getHeight())*2;
	}
	
	public boolean intersects(Rectangle other) {
		double selfLeft = getLeft();
		double selfTop = getTop();
		double selfRight = getRight();
		double selfBottom = getBottom();
		double otherLeft = other.getLeft();
		double otherTop = other.getTop();
		double otherRight = other.getRight();
		double otherBottom = other.getBottom();
		return (
			otherLeft < selfRight
			&& otherTop < selfBottom
			&& otherRight > selfLeft
			&& otherBottom > selfTop
		);
	}
	
	public boolean contains(Rectangle other) {
		double selfLeft= getLeft();
		double selfTop = getTop();
		double selfRight = getRight();
		double selfBottom = getBottom();
		double otherLeft= other.getLeft();
		double otherTop = other.getTop();
		double otherRight = other.getRight();
		double otherBottom = other.getBottom();
		return (
			otherLeft < selfRight && otherLeft >= selfLeft
			&& otherTop < selfBottom && otherTop >= selfTop
			&& otherRight > selfLeft && otherRight <= selfRight
			&& otherBottom > selfTop && otherBottom <= selfBottom
		);
	}
	
	public Rectangle getIntersectionWith(Rectangle other) {
		if (!this.intersects(other)) {
			return null;
		}
		double newLeft = Math.max(this.getLeft(), other.getLeft());
		double newTop= Math.max(this.getTop(), other.getTop());
		double newRight = Math.min(this.getRight(), other.getRight());
		double newBottom= Math.min(this.getBottom(), other.getBottom());
		return new Rectangle(new Point(newLeft, newTop), new Point(newRight, newBottom));
	}

	public Rectangle getExtendedWith(Rectangle other) {
		double newLeft = Math.min(this.getLeft(), other.getLeft());
		double newTop= Math.min(this.getTop(), other.getTop());
		double newRight = Math.max(this.getRight(), other.getRight());
		double newBottom= Math.max(this.getBottom(), other.getBottom());
		return new Rectangle(new Point(newLeft, newTop), new Point(newRight, newBottom));
	}
	
	@Override
	public String toString() {
		return "Rectangle("+point1+"; "+point2+")";
	}
	
}
