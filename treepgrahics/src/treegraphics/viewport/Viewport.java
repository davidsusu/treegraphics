package treegraphics.viewport;

import treegraphics.canvas.Point;

public class Viewport {

	public Viewport() {
		// TODO: FIXME
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
	
}
