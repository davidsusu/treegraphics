package treegraphics.viewport;

import java.util.Collection;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.CachedState;

public interface DrawableService extends CachedState {

	public void addDrawable(Drawable drawable);

	public void removeDrawable(Drawable drawable);
	
	public Collection<Drawable> getDrawables();
	
	public Collection<Drawable> getAffectedDrawables(Rectangle area);
	
	public Collection<Drawable> getAffectedDrawables(Point point);

	public void addDrawableChangeListener(DrawableChangeListener drawableChangeListener);

	public void removeDrawableChangeListener(DrawableChangeListener drawableChangeListener);
	
	public interface DrawableChangeListener {
		
		public void drawableChanged(Drawable drawable);
		
	}
	
}
