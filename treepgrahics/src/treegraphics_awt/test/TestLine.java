package treegraphics_awt.test;

import java.util.ArrayList;
import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.CachedState;
import treegraphics.util.Identified;
import treegraphics.valuetree.SourceDoubleValue;
import treegraphics.valuetree.DoubleValue;

public class TestLine implements Drawable, TestMovableDrawable, Identified {

	final protected int id;
	
	protected DoubleValue x1Value;
	
	protected DoubleValue y1Value;
	
	protected DoubleValue x2Value;
	
	protected DoubleValue y2Value;
	
	protected DoubleValue zValue;
	
	protected Color color;

	protected boolean isExpired = true;
	
	final protected List<CachedState> dependents = new ArrayList<CachedState>();
	
	public TestLine(DoubleValue x1Value, DoubleValue y1Value, DoubleValue x2Value, DoubleValue y2Value, DoubleValue zValue, Color color) {
		this.x1Value = x1Value;
		this.y1Value = y1Value;
		this.x2Value = x2Value;
		this.y2Value = y2Value;
		this.zValue = zValue;
		this.x1Value.registerDependent(this);
		this.x2Value.registerDependent(this);
		this.y1Value.registerDependent(this);
		this.y2Value.registerDependent(this);
		this.zValue.registerDependent(this);
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
			x1Value instanceof SourceDoubleValue
			&& y1Value instanceof SourceDoubleValue
			&& x2Value instanceof SourceDoubleValue
			&& y2Value instanceof SourceDoubleValue
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
				((SourceDoubleValue)x1Value).set(destX+width);
				((SourceDoubleValue)x2Value).set(destX);
			} else {
				((SourceDoubleValue)x1Value).set(destX);
				((SourceDoubleValue)x2Value).set(destX+width);
			}
			if (y1>y2) {
				((SourceDoubleValue)y1Value).set(destY+height);
				((SourceDoubleValue)y2Value).set(destY);
			} else {
				((SourceDoubleValue)y1Value).set(destY);
				((SourceDoubleValue)y2Value).set(destY+height);
			}
		}
	}
	
	@Override
	public int getIdentifier() {
		return id;
	}

	@Override
	public ExpireEvent expireState() {
		ExpireEvent ev = new ExpireEvent(this);
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState(ev);
		}
		for (CachedState dependent: dependents) {
			dependent.onExpirationFinished(ev);
		}
		return ev;
	}
	
	@Override
	public void expireState(ExpireEvent ev) {
		isExpired = true;
		ev.push(this);
		for (CachedState dependent: dependents) {
			dependent.expireState(ev);
		}
		ev.pop(this);
	}
	
	@Override
	public void onExpirationFinished(ExpireEvent ev) {
		for (CachedState dependent: dependents) {
			dependent.onExpirationFinished(ev);
		}
	}
	
	@Override
	public void registerDependent(CachedState cachedState) {
		// FIXME
		if (!dependents.contains(cachedState)) {
			dependents.add(cachedState);
		}
	}

	@Override
	public void unregisterDependent(CachedState cachedState) {
		dependents.remove(cachedState);
	}

	@Override
	public void freeFromDependecies() {
		x1Value.unregisterDependent(this);
		y1Value.unregisterDependent(this);
		x2Value.unregisterDependent(this);
		y2Value.unregisterDependent(this);
		zValue.unregisterDependent(this);
	}
	
	@Override
	public String toString() {
		return "TestLine(("+x1Value.get()+", "+y1Value.get()+"); ("+x2Value.get()+", "+y2Value.get()+"))";
	}
	
	
}
