package treegraphics.viewport;

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
import treegraphics.swingutil.Graphics2DCanvas;

public class Viewport {
	
	protected final JPanel panel;
	
	Set<Drawable> drawables = new HashSet<Drawable>();
	
	public Viewport() {
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
	
	public void setOrigin(Point origin) {
		// TODO: modifiy origin with no rebuild (move current bitmap)
	}

	public Point getOrigin() {
		// TODO
		return null;
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
