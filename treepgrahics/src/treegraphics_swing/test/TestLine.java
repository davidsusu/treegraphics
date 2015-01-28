package treegraphics_swing.test;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.Identified;
import treegraphics.valuetree.Value;

public class TestLine implements Drawable, Identified {

	final protected int id;
	
	protected Value x1Value;
	
	protected Value y1Value;
	
	protected Value x2Value;
	
	protected Value y2Value;
	
	protected Color color;
	
	public TestLine(Value x1Value, Value y1Value, Value x2Value, Value y2Value, Color color) {
		this.x1Value = x1Value;
		this.y1Value = y1Value;
		this.x2Value = x2Value;
		this.y2Value = y2Value;
		this.color = color;
		this.id = Identified.Id.getNext();
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.setColor(color);
		canvas.drawLine(new Point(x1Value.get(), y1Value.get()), new Point(x2Value.get(), y2Value.get()));
	}

	@Override
	public Rectangle getReservedRectangle() {
		return new Rectangle(new Point(x1Value.get(), y1Value.get()), new Point(x2Value.get(), y2Value.get()));
	}
	
	@Override
	public int getIdentifier() {
		return id;
	}
	
}
