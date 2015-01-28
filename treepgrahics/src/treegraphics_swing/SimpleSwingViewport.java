package treegraphics_swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.viewport.Viewport;

public class SimpleSwingViewport implements Viewport {
	
	protected final JPanel panel;
	
	Set<Drawable> drawables = new HashSet<Drawable>();
	
	public SimpleSwingViewport() {
		this.panel = new JPanel() {
			
			public static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				Canvas canvas = new Graphics2DCanvas((Graphics2D)g);
				
				canvas.setColor(new Color(255, 255, 255));
				canvas.fillRectangle(new Rectangle(new Point(0, 0), new Dimension(getWidth(), getHeight())));
				
				for (Drawable drawable: drawables) {
					drawable.draw(canvas);
				}
			}
			
		};
	}
	
	// FIXME
	public JPanel getPanel() {
		return panel;
	}
	
	public void addDrawable(Drawable drawable) {
		drawables.add(drawable);
	}
	
	public void removeDrawable(Drawable drawable) {
		drawables.remove(drawable);
	}
	
	public void setX(int x) {
		// TODO
	}

	public int getX() {
		// TODO
		return 0;
	}
	
	public void setY(int x) {
		// TODO
	}

	public int getY() {
		// TODO
		return 0;
	}
	
	public int getWidth() {
		return panel.getWidth();
	}
	
	public int getHeight() {
		return panel.getHeight();
	}
	
	public void setZoom(double zoom) {
		// TODO: modify zoom with no rebuild (scale current bitmap)
	}
	
	public double getZoom() {
		// TODO
		return 0;
	}
	
	public void rebuild() {
		// TODO: rebuild bitmap, and if necessary then additionally rebuild the active area of the DrawableService
	}
	
	public void refresh() {
		panel.repaint();
	}
	
}

