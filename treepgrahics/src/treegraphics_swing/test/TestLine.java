package treegraphics_swing.test;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.Identified;
import treegraphics.valuetree.SourceValue;
import treegraphics.valuetree.Value;

public class TestLine implements Drawable, TestMovableDrawable, Identified {

	final protected int id;
	
	protected Value x1Value;
	
	protected Value y1Value;
	
	protected Value x2Value;
	
	protected Value y2Value;
	
	protected Value zValue;
	
	protected Color color;
	
	public TestLine(Value x1Value, Value y1Value, Value x2Value, Value y2Value, Value zValue, Color color) {
		this.x1Value = x1Value;
		this.y1Value = y1Value;
		this.x2Value = x2Value;
		this.y2Value = y2Value;
		this.zValue = zValue;
		this.color = color;
		this.id = Identified.Id.getNext();
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.setColor(color);
		canvas.drawLine(new Point(x1Value.get(), y1Value.get()), new Point(x2Value.get(), y2Value.get()));
	}

	@Override
	public double getZ() {
		return zValue.get();
	}
	
	@Override
	public Rectangle getReservedRectangle() {
		return new Rectangle(new Point(x1Value.get(), y1Value.get()), new Point(x2Value.get(), y2Value.get()));
	}

	@Override
	public boolean isPointDominated(Point point) {
		if (!getReservedRectangle().containsPoint(point)) {
			return false;
		}
		double maxDistance = 0.5;
		double x1 = x1Value.get();
		double y1 = y1Value.get();
		double x2 = x2Value.get();
		double y2 = y2Value.get();
		double xComp = y2-y1;
		double yComp = x1-x2;
		double cComp = (y1*(x2-x1))+(x1*(y1-y2));
		double distance = Math.abs((xComp*point.getX())+(yComp*point.getY())+cComp)/Math.sqrt((xComp*xComp)+(yComp*yComp));
		return (distance<=maxDistance);
	}
	
	@Override
	public void moveTo(Point leftTopPoint) {
		if (
			x1Value instanceof SourceValue
			&& y1Value instanceof SourceValue
			&& x2Value instanceof SourceValue
			&& y2Value instanceof SourceValue
		) {
			double x1 = x1Value.get();
			double y1 = y1Value.get();
			double x2 = x2Value.get();
			double y2 = y2Value.get();
			double destX = leftTopPoint.getX();
			double destY = leftTopPoint.getY();
			double width = Math.abs(x2-x1);
			double height = Math.abs(y2-y1);
			if (x1>x2) {
				((SourceValue)x1Value).set(destX+width);
				((SourceValue)x2Value).set(destX);
			} else {
				((SourceValue)x1Value).set(destX);
				((SourceValue)x2Value).set(destX+width);
			}
			if (y1>y2) {
				((SourceValue)y1Value).set(destY+height);
				((SourceValue)y2Value).set(destY);
			} else {
				((SourceValue)y1Value).set(destY);
				((SourceValue)y2Value).set(destY+height);
			}
		}
	}
	
	@Override
	public int getIdentifier() {
		return id;
	}
	
}
