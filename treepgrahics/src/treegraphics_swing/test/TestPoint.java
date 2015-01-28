package treegraphics_swing.test;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.Identified;
import treegraphics.valuetree.Value;

public class TestPoint implements Drawable, Identified {
	
	final protected int id;
	
	protected Value xValue;
	
	protected Value yValue;
	
	protected double radius;
	
	protected Color color;
	
	public TestPoint(Value xValue, Value yValue, double radius, Color color) {
		this.xValue = xValue;
		this.yValue = yValue;
		this.radius = radius;
		this.color = color;
		this.id = Identified.Id.getNext();
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.setColor(color);
		canvas.fillRectangle(getReservedRectangle());
	}

	@Override
	public Rectangle getReservedRectangle() {
		return new Rectangle(new Point(xValue.get()-radius, yValue.get()-radius), new Dimension(radius*2, radius*2));
	}
	
	@Override
	public int getIdentifier() {
		return id;
	}
	
}