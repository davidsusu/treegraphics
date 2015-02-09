package treegraphics_awt.test;

import java.util.ArrayList;
import java.util.List;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.CachedState;
import treegraphics.util.Identified;
import treegraphics.valuetree.SourceDoubleValue;
import treegraphics.valuetree.DoubleValue;

public class TestPoint implements Drawable, TestMovableDrawable, Identified {
	
	final protected int id;
	
	protected DoubleValue xValue;
	
	protected DoubleValue yValue;

	protected DoubleValue zValue;
	
	protected double radius;
	
	protected Color color;

	protected boolean isExpired = true;

	final protected List<CachedState> expiredDependencies = new ArrayList<CachedState>();
	
	final protected List<CachedState> dependents = new ArrayList<CachedState>();
	
	public TestPoint(DoubleValue xValue, DoubleValue yValue, DoubleValue zValue, double radius, Color color) {
		this.xValue = xValue;
		this.yValue = yValue;
		this.zValue = zValue;
		this.xValue.registerDependent(this);
		this.yValue.registerDependent(this);
		this.zValue.registerDependent(this);
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
	public double getZ() {
		return zValue.get();
	}
	
	@Override
	public Rectangle getReservedRectangle() {
		return new Rectangle(new Point(xValue.get()-radius, yValue.get()-radius), new Dimension(radius*2, radius*2));
	}
	
	@Override
	public boolean isPointDominated(Point point) {
		return getReservedRectangle().containsPoint(point);
	}
	
	@Override
	public void moveTo(Point leftTopPoint) {
		if (
			xValue instanceof SourceDoubleValue
			&& yValue instanceof SourceDoubleValue
		) {
			((SourceDoubleValue)xValue).set(leftTopPoint.getX()+radius);
			((SourceDoubleValue)yValue).set(leftTopPoint.getY()+radius);
		}
	}
	
	@Override
	public int getIdentifier() {
		return id;
	}

	@Override
	public void expireState() {
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState(this);
		}
	}

	@Override
	public void expireState(CachedState cachedState) {
		expiredDependencies.add(cachedState);
		expireState();
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
		xValue.unregisterDependent(this);
		yValue.unregisterDependent(this);
		zValue.unregisterDependent(this);
	}

	@Override
	public String toString() {
		return "TestPoint("+xValue.get()+", "+yValue.get()+")";
	}
	
}