package treegraphics.viewport;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractViewport implements Viewport {

	protected List<DrawListener> drawListeners = new ArrayList<DrawListener>();

	@Override
	public void addDrawListener(DrawListener drawListener) {
		drawListeners.add(drawListener);
	}

	@Override
	public void removeDrawListener(DrawListener drawListener) {
		drawListeners.remove(drawListener);
	}
	
}
