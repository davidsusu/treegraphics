package treegraphics.canvas.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;


public class SwingCanvasTest {

	public static void main(String[] args) {
		final ArrayList<RectangleDrawable> rectangleDrawables = new ArrayList<RectangleDrawable>();
		
		final RectangleDrawable mouseRectangleDrawable = new RectangleDrawable(new Rectangle(-10, -10, 0, 0), new Color(0, 0, 0));

		final Wrapper<Point> originWrapper = new Wrapper<Point>(new Point(0, 0));
		final Wrapper<Double> zoomWrapper = new Wrapper<Double>(1.0);
		
		final Color catcherColor = new Color(0, 0, 170);
		final Color catcherHighlightColor = new Color(100, 100, 255);
		final Color mouseColor = new Color(150, 150, 150);
		final Color mouseIntersectColor = new Color(255, 90, 10);
		final Color mouseHighlightColor = new Color(255, 255, 10);

		final Wrapper<Point> dragStartPointWrapper = new Wrapper<Point>(null);
		final Wrapper<Point> dragStartOriginWrapper = new Wrapper<Point>(originWrapper.getObject());
		
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

				// FIXME
				canvas.setOrigin(originWrapper.getObject());
				canvas.setZoom(zoomWrapper.getObject());
				
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
		final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent ev) {
				double zoom = zoomWrapper.getObject();
				Rectangle mouseArea = new Rectangle(new Point(
					((ev.getX()+originWrapper.getObject().getX()-50)/zoom),
					((ev.getY()+originWrapper.getObject().getY()-30)/zoom)
				), new Dimension(100/zoom, 60/zoom));
				mouseRectangleDrawable.setRectangle(mouseArea);
				mainPanel.repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent ev) {
				Point startPoint = dragStartPointWrapper.getObject();
				Point startOrigin = dragStartOriginWrapper.getObject();
				if (startPoint!=null && startOrigin!=null) {
					Point currentPoint = new Point(ev.getX(), ev.getY());
					Rectangle moveArea = new Rectangle(startPoint, currentPoint);
					double horizontalVector = moveArea.getHorizontalVector(); // FIXME
					double verticalVector = moveArea.getVerticalVector(); // FIXME
					originWrapper.setObject(new Point(startOrigin.getX()-horizontalVector, startOrigin.getY()-verticalVector));
				}
				mouseMoved(ev);
			}
			
		};
		
		mainPanel.addMouseMotionListener(mouseMotionListener);
		mainPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent ev) {
				dragStartPointWrapper.setObject(null);
			}
			
			@Override
			public void mousePressed(MouseEvent ev) {
				dragStartPointWrapper.setObject(new Point(ev.getX(), ev.getY()));
				dragStartOriginWrapper.setObject(originWrapper.getObject());
			}
			
			@Override
			public void mouseExited(MouseEvent ev) {}
			
			@Override
			public void mouseEntered(MouseEvent ev) {}
			
			@Override
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount()>1) {
					rectangleDrawables.clear();
					mainPanel.repaint();
				} else {
					double zoom = zoomWrapper.getObject();
					double x = (ev.getX()/zoom)+originWrapper.getObject().getX();
					double y = (ev.getY()/zoom)+originWrapper.getObject().getY();
					rectangleDrawables.add(new RectangleDrawable(new Rectangle(x-100, y-70, x+100, y+70), catcherColor));
					mainPanel.repaint();
				}
			}
		});
		mainPanel.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent ev) {
				dragStartPointWrapper.setObject(null);
				double diff = ev.getPreciseWheelRotation();
				zoomWrapper.setObject(zoomWrapper.getObject()*Math.pow(1.1, diff));
				// TODO: origin...
				mouseMotionListener.mouseMoved(new MouseEvent((Component)ev.getSource(), 0, ev.getWhen(), ev.getModifiers(), ev.getX(), ev.getY(), 0, 0, 1, false, 1));
			}
			
		});
		
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	protected static class AwtCanvas implements Canvas {

		protected Graphics2D g2d;
		
		protected Point origin = new Point(0, 0);
		
		protected double zoom = 1;
		
		public AwtCanvas(Graphics2D g2d) {
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
	
	protected static class Wrapper<T> {
		
		protected T object;
		
		public Wrapper(T object) {
			this.object = object;
		}

		public void setObject(T object) {
			this.object = object;
		}

		public T getObject() {
			return object;
		}
		
	}
	
}
