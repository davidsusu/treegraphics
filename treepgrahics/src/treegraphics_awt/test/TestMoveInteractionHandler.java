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
import treegraphics.viewport.Viewport;
import treegraphics_swing.SwingViewport;

public class TestMoveInteractionHandler {
	
	protected final Viewport viewport;
	
	protected final Component component;
	
	protected TestMovableDrawable dragMoveObject = null;
	
	protected Point dragMoveStartLeftTop = null;

	protected java.awt.Point dragStartPosition = null;

	protected java.awt.Point dragRebuildPosition = null;

	protected Point dragStartOrigin = null;
	
	protected TimeoutRebuilderThread rebuildThread = null;
	
	public TestMoveInteractionHandler(SwingViewport viewport, Component component) {
		this.component = component;
		this.viewport = viewport;
	}
	
	public void init() {

		component.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent ev) {
				if (ev.getButton()==MouseEvent.BUTTON1) {
					dragStartPosition = null;
					dragRebuildPosition = null;
					dragMoveObject = null;
					dragMoveStartLeftTop = null;
					dragStartOrigin = null;
					viewport.rebuild();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent ev) {
				if (ev.getButton()==MouseEvent.BUTTON1) {
					int mouseX = ev.getX();
					int mouseY = ev.getY();
					dragStartPosition = new java.awt.Point(mouseX, mouseY);
					dragRebuildPosition = dragStartPosition;
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
				int mouseX = ev.getX();
				int mouseY = ev.getY();
				int mouseRefreshXDiff = 0;
				int mouseRefreshYDiff = 0;
				if (dragStartPosition!=null) {
					int mouseXDiff = mouseX-(int)dragStartPosition.getX();
					int mouseYDiff = mouseY-(int)dragStartPosition.getY();
					mouseRefreshXDiff = mouseX-(int)dragRebuildPosition.getX();
					mouseRefreshYDiff = mouseY-(int)dragRebuildPosition.getY();
					double zoom = viewport.getZoom();
					if (dragMoveObject!=null && dragMoveStartLeftTop!=null) {
						dragMoveObject.moveTo(new Point((dragMoveStartLeftTop.getX()+(mouseXDiff/zoom)), (dragMoveStartLeftTop.getY()+(mouseYDiff/zoom))));
					} else if (dragStartOrigin!=null) {
						double originXDiff = mouseXDiff/zoom;
						double originYDiff = mouseYDiff/zoom;
						Point newOrigin = new Point(dragStartOrigin.getX()-originXDiff, dragStartOrigin.getY()-originYDiff);
						viewport.setOrigin(newOrigin);
					}
				}
				if (Math.abs(mouseRefreshXDiff)>100 || Math.abs(mouseRefreshYDiff)>100) {
					viewport.rebuild();
					dragRebuildPosition = new java.awt.Point(mouseX, mouseY);
				} else {
					viewport.refresh();
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
				int mouseDisplacedX = mouseX-viewport.getXDisplacement();
				int mouseDisplacedY = mouseY-viewport.getYDisplacement();
				Point oldOrigin = viewport.getOrigin();
				double rot = ev.getPreciseWheelRotation();
				double oldZoom = viewport.getZoom();
				double zoomScale = Math.pow(0.9, rot);
				double newZoom = oldZoom*zoomScale;
				double originXDiff = (mouseDisplacedX/newZoom)-(mouseDisplacedX/oldZoom);
				double originYDiff = (mouseDisplacedY/newZoom)-(mouseDisplacedY/oldZoom);
				Point newOrigin = new Point(oldOrigin.getX()-originXDiff, oldOrigin.getY()-originYDiff);
				viewport.setZoom(newZoom);
				viewport.setOrigin(newOrigin);

				TestMoveInteractionHandler.this.startRebuild();
				viewport.refresh();
				
				if (dragStartPosition!=null && dragStartOrigin!=null) {
					dragStartPosition = new java.awt.Point(ev.getX(), ev.getY());
					dragRebuildPosition = dragStartPosition;
					dragStartOrigin = viewport.getOrigin();
				}
			}
		});
		
	}
	
	protected void startRebuild() {
		if (rebuildThread==null || rebuildThread.isFinished()) {
			rebuildThread = new TimeoutRebuilderThread(viewport, 200);
			rebuildThread.start();
		}
	}
	
	protected class TimeoutRebuilderThread extends Thread {

		protected final int timeStep = 50;
		
		protected final Viewport viewport;

		protected final double timeout;
		
		protected double elapsedTime = 0;
		
		protected boolean finished = false;
		
		public TimeoutRebuilderThread(Viewport viewport, double timeout) {
			this.viewport = viewport;
			this.timeout = timeout;
		}
		
		public void run() {
			while (true) {
				try {
					sleep(timeStep);
				} catch (InterruptedException e) {
					break;
				}
				elapsedTime += timeStep;
				if (elapsedTime>=timeout) {
					doRebuild();
					finished = true;
					break;
				}
			}
		}
		
		public boolean isFinished() {
			return finished;
		}
		
		protected void doRebuild() {
			viewport.rebuild();
		}
		
	}
	
}