package treegraphics.viewport;

import java.util.List;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.CachedState;

public interface DrawableService extends CachedState {

	public void addDrawable(Drawable drawable);

	public void removeDrawable(Drawable drawable);
	
	public List<Drawable> getDrawables();
	
	public List<Drawable> getAffectedDrawables(Rectangle area);
	
	public List<Drawable> getAffectedDrawables(Point point);

}
