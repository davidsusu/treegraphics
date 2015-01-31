package treegraphics_awt.test;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics_swing.SwingSimpleViewport;

public class TestMoveInteractionHandler {
	
	protected final SwingSimpleViewport viewport;
	
	protected final Component component;
	
	protected TestMovableDrawable dragMoveObject = null;
	
	protected Point dragMoveStartLeftTop = null;

	protected java.awt.Point dragStartPosition = null;

	protected Point dragStartOrigin = null;
	
	public TestMoveInteractionHandler(SwingSimpleViewport viewport, Component component) {
		this.component = component;
		this.viewport = viewport;
	}
	
	public void init() {

		component.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent ev) {
				if (ev.getButton()==MouseEvent.BUTTON1) {
					dragStartPosition = null;
					dragMoveObject = null;
					dragMoveStartLeftTop = null;
					dragStartOrigin = null;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent ev) {
				if (ev.getButton()==MouseEvent.BUTTON1) {
					int mouseX = ev.getX();
					int mouseY = ev.getY();
					dragStartPosition = new java.awt.Point(mouseX, mouseY);
					List<Drawable> drawables = viewport.getDrawablesAtPixel(mouseX, mouseY);
					TestMovableDrawable movableDrawable = null;
					for (Drawable drawable: drawables) {
						if (drawable instanceof TestMovableDrawable) {
							movableDrawable = (TestMovableDrawable)drawable;
							break;
						}
					}
					if (movableDrawable!=null) {
						dragMoveObject = movableDrawable;
						dragMoveStartLeftTop = movableDrawable.getReservedRectangle().getLeftTop();
					} else {
						dragStartOrigin = viewport.getOrigin();
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent ev) {
			}
			
			@Override
			public void mouseEntered(MouseEvent ev) {
			}
			
			@Override
			public void mouseClicked(MouseEvent ev) {
			}
			
		});
		
		component.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent ev) {
			}
			
			@Override
			public void mouseDragged(MouseEvent ev) {
				if (dragStartPosition!=null) {
					int mouseXDiff = ev.getX()-(int)dragStartPosition.getX();
					int mouseYDiff = ev.getY()-(int)dragStartPosition.getY();
					double zoom = viewport.getZoom();
					if (dragMoveObject!=null && dragMoveStartLeftTop!=null) {
						dragMoveObject.moveTo(new Point((dragMoveStartLeftTop.getX()+(mouseXDiff/zoom)), (dragMoveStartLeftTop.getY()+(mouseYDiff/zoom))));
						viewport.rebuild();
						
					} else if (dragStartOrigin!=null) {
						double originXDiff = mouseXDiff/zoom;
						double originYDiff = mouseYDiff/zoom;
						Point newOrigin = new Point(dragStartOrigin.getX()-originXDiff, dragStartOrigin.getY()-originYDiff);
						viewport.setOrigin(newOrigin);
						viewport.rebuild();
					}
				}
			}
			
		});
		
		component.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent ev) {
				if (dragMoveObject!=null) {
					return;
				}
				int mouseX = ev.getX();
				int mouseY = ev.getY();
				Point oldOrigin = viewport.getOrigin();
				double rot = ev.getPreciseWheelRotation();
				double oldZoom = viewport.getZoom();
				double zoomScale = Math.pow(0.9, rot);
				double newZoom = oldZoom*zoomScale;
				double originXDiff = (mouseX/newZoom)-(mouseX/oldZoom);
				double originYDiff = (mouseY/newZoom)-(mouseY/oldZoom);
				Point newOrigin = new Point(oldOrigin.getX()-originXDiff, oldOrigin.getY()-originYDiff);
				viewport.setZoom(newZoom);
				viewport.setOrigin(newOrigin);
				viewport.rebuild();
				if (dragStartPosition!=null && dragStartOrigin!=null) {
					dragStartPosition = new java.awt.Point(ev.getX(), ev.getY());
					dragStartOrigin = viewport.getOrigin();
				}
			}
		});
		
	}
	
}