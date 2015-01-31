package treegraphics.viewport;

import java.util.ArrayList;
import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.util.CachedState;

abstract public class AbstractDrawableService implements DrawableService {

	protected boolean isExpired = true;

	protected final List<CachedState> expiredDependencies = new ArrayList<CachedState>();
	
	protected final List<CachedState> dependents = new ArrayList<CachedState>();

	protected List<DrawableChangeListener> drawableChangeListeners = new ArrayList<DrawableChangeListener>();

	@Override
	public void expireState() {
		isExpired = true;
		for (CachedState dependent: dependents) {
			dependent.expireState(this);
		}
	}
	
	@Override
	public void registerDependent(CachedState cachedState) {
		// FIXME
		if (!dependents.contains(cachedState)) {
			dependents.add(cachedState);
		}
	}

	@Override
	public void unregisterDependent(CachedState cachedState) {
		dependents.remove(cachedState);
	}

	@Override
	public void addDrawableChangeListener(DrawableChangeListener drawableChangeListener) {
		drawableChangeListeners.add(drawableChangeListener);
	}

	@Override
	public void removeDrawableChangeListener(DrawableChangeListener drawableChangeListener) {
		drawableChangeListeners.remove(drawableChangeListener);
	}

	protected void fireDrawableChange(Drawable drawable) {
		for (DrawableChangeListener drawableChangeListener: drawableChangeListeners) {
			drawableChangeListener.drawableChanged(drawable);
		}
	}
	
}
