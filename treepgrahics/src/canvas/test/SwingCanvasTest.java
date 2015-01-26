package canvas.test;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import canvas.Canvas;
import canvas.Color;
import canvas.Dimension;
import canvas.Drawable;
import canvas.Point;
import canvas.Rectangle;

public class SwingCanvasTest {

	public static void main(String[] args) {
		final ArrayList<RectangleDrawable> rectangleDrawables = new ArrayList<RectangleDrawable>();
		
		final RectangleDrawable mouseRectangleDrawable = new RectangleDrawable(new Rectangle(-10, -10, 0, 0), new Color(0, 0, 0));

		final Color catcherColor = new Color(0, 0, 170);
		final Color catcherHighlightColor = new Color(100, 100, 255);
		final Color mouseColor = new Color(150, 150, 150);
		final Color mouseIntersectColor = new Color(255, 90, 10);
		final Color mouseHighlightColor = new Color(255, 255, 10);
		
		JFrame frame = new JFrame("Canvas test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JPanel mainPanel = new JPanel() {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				AwtCanvas canvas = new AwtCanvas((Graphics2D)g);
				
				int width = getWidth();
				int height = getHeight();
				
				canvas.setColor(new Color(255, 255, 255));
				canvas.fillRectangle(new Rectangle(0, 0, width, height));

				Rectangle mouseRectangle = mouseRectangleDrawable.getReservedRectangle();
				boolean mouseRectangleContained = false;
				boolean mouseRectangleIntersected = false;
				for (RectangleDrawable rectangleDrawable : rectangleDrawables) {
					Rectangle rectangle = rectangleDrawable.getReservedRectangle();
					if (rectangle.contains(mouseRectangle)) {
						rectangleDrawable.setColor(catcherHighlightColor);
						mouseRectangleContained = true;
					} else if (rectangle.intersects(mouseRectangle)) {
						rectangleDrawable.setColor(catcherHighlightColor);
						mouseRectangleIntersected = true;
					} else {
						rectangleDrawable.setColor(catcherColor);
					}
				}
				if (mouseRectangleContained) {
					mouseRectangleDrawable.setColor(mouseHighlightColor);
				} else if (mouseRectangleIntersected) {
					mouseRectangleDrawable.setColor(mouseIntersectColor);
				} else {
					mouseRectangleDrawable.setColor(mouseColor);
				}
				
				for (RectangleDrawable rectangleDrawable: rectangleDrawables) {
					rectangleDrawable.draw(canvas);
				}
				
				mouseRectangleDrawable.draw(canvas);
			}
			
		};
		mainPanel.setPreferredSize(new java.awt.Dimension(800, 500));
		mainPanel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent ev) {
				Rectangle mouseArea = new Rectangle(new Point(ev.getX()-50, ev.getY()-30), new Dimension(100, 60));
				mouseRectangleDrawable.setRectangle(mouseArea);
				mainPanel.repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent ev) {
				mouseMoved(ev);
			}
			
		});
		mainPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent ev) {}
			
			@Override
			public void mousePressed(MouseEvent ev) {}
			
			@Override
			public void mouseExited(MouseEvent ev) {}
			
			@Override
			public void mouseEntered(MouseEvent ev) {}
			
			@Override
			public void mouseClicked(MouseEvent ev) {
				rectangleDrawables.add(new RectangleDrawable(new Rectangle(ev.getX()-100, ev.getY()-70, ev.getX()+100, ev.getY()+70), catcherColor));
				mainPanel.repaint();
			}
		});
		
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	protected static class AwtCanvas implements Canvas {

		protected Graphics2D g2d;
		
		public AwtCanvas(Graphics2D g2d) {
			this.g2d = g2d;
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
	
	protected static class RectangleDrawable implements Drawable {
		
		protected Rectangle rectangle;
		
		protected Color color;
		
		public RectangleDrawable(Rectangle rectangle, Color color) {
			this.rectangle = rectangle;
			this.color = color;
		}

		public void setRectangle(Rectangle rectangle) {
			this.rectangle = rectangle;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.setColor(color);
			canvas.fillRectangle(rectangle);
		}

		@Override
		public Rectangle getReservedRectangle() {
			return rectangle;
		}
		
	}
	
}
