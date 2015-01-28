package treegraphics_swing;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

public class Graphics2DCanvas implements Canvas {

	protected Graphics2D g2d;
	
	protected Point origin = new Point(0, 0);
	
	protected double zoom = 1;

	public Graphics2DCanvas(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void setOrigin(Point origin) {
		this.origin = origin;
		refreshTransform();
	}

	@Override
	public Point getOrigin() {
		return origin;
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
		refreshTransform();
	}

	@Override
	public double getZoom() {
		return zoom;
	}

	@Override
	public void resetTransform() {
		this.origin = new Point(0, 0);
		this.zoom = 1;
		g2d.setTransform(new AffineTransform());
	}

	@Override
	public Point transformPoint(Point point) {
		return new Point((point.getX()-origin.getX())*zoom, (point.getY()-origin.getY())*zoom);
	}

	@Override
	public Point transformBackPoint(Point point) {
		return new Point((point.getX()/zoom)+origin.getX(), (point.getY()/zoom)+origin.getY());
	}
	
	protected void refreshTransform() {
		g2d.setTransform(new AffineTransform());
		g2d.translate(-origin.getX(), -origin.getY());
		g2d.scale(zoom, zoom);
	}
	
	@Override
	public void setColor(Color color) {
		g2d.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue()));
	}

	@Override
	public void fillRectangle(Rectangle rectangle) {
		g2d.fillRect((int)rectangle.getLeft(), (int)rectangle.getTop(), (int)rectangle.getWidth(), (int)rectangle.getHeight());
	}
	
}
