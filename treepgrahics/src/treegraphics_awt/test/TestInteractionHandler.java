package treegraphics_awt.test;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.viewport.Viewport;

public class TestInteractionHandler {
	
	protected final Viewport viewport;
	
	protected final Component component;
	
	protected TestMovableDrawable dragMoveObject = null;
	
	protected Point dragMoveStartLeftTop = null;

	protected java.awt.Point dragStartPosition = null;

	protected java.awt.Point dragRebuildPosition = null;

	protected Point dragStartOrigin = null;
	
	protected TimeoutRebuilderThread rebuildThread = null;
	
	public TestInteractionHandler(Viewport viewport, Component component) {
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

				TestInteractionHandler.this.startRebuild();
				viewport.refresh();
				
				if (dragStartPosition!=null && dragStartOrigin!=null) {
					dragStartPosition = new java.awt.Point(ev.getX(), ev.getY());
					dragRebuildPosition = dragStartPosition;
					dragStartOrigin = viewport.getOrigin();
				}
			}
		});
		
		component.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent ev) {
			}
			
			@Override
			public void keyReleased(KeyEvent ev) {
			}
			
			@Override
			public void keyPressed(KeyEvent ev) {
				Point origin = viewport.getOrigin();
				double zoom = viewport.getZoom();
				switch (ev.getKeyCode()) {
					case 37:
						viewport.setOrigin(new Point(origin.getX()-(50/zoom), origin.getY()));
						viewport.rebuild();
					break;
					case 38:
						viewport.setOrigin(new Point(origin.getX(), origin.getY()-(50/zoom)));
						viewport.rebuild();
					break;
					case 39:
						viewport.setOrigin(new Point(origin.getX()+(50/zoom), origin.getY()));
						viewport.rebuild();
					break;
					case 40:
						viewport.setOrigin(new Point(origin.getX(), origin.getY()+(50/zoom)));
						viewport.rebuild();
					break;
					
					// TODO: screen center as zoom origin
					case 49:
						viewport.setZoom(1);
						viewport.rebuild();
						break;
					case 50:
						viewport.setZoom(2);
						viewport.rebuild();
						break;
					case 51:
						viewport.setZoom(3);
						viewport.rebuild();
						break;
					case 68:
						viewport.setZoom(viewport.getZoom()*0.9);
						viewport.rebuild();
						break;
					case 69:
						viewport.setZoom(viewport.getZoom()/0.9);
						viewport.rebuild();
						break;
				}
			}
			
		});
		
		component.setFocusable(true);
		
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
