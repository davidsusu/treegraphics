package canvas;

public class Dimension {

	protected final double width;

	protected final double height;
	
	public Dimension(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}

	public double getDiagonalLength() {
		return Math.sqrt((width*width)+(height*height));
	}
	
	public double getArea() {
		return width*height;
	}
	
	public double getPerimeter() {
		return (width+height)*2;
	}
	
	@Override
	public String toString() {
		return "Dimension("+width+"×"+height+")";
	}
	
}
