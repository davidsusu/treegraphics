package canvas;

public class Color {

	// TODO: constants (RED, GREEN, BLUE, YELLOW etc.)
	
	protected final int red;

	protected final int green;

	protected final int blue;

	protected final double alpha;
	
	public Color(int red, int green, int blue) {
		this(red, green, blue, 1);
	}
	
	public Color(int red, int green, int blue, double alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public int getRed() {
		return red;
	}
	
	public int getGreen() {
		return green;
	}
	
	public int getBlue() {
		return blue;
	}
	
	public double getAlpha() {
		return alpha;
	}
	
	public Color getWithoutAlpha() {
		return new Color(red, green, blue);
	}
	
	// TODO: darken, negate, grayscale etc.
	
}
