package treegraphics.viewport;

import java.util.Collection;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;

public interface DrawableService {

	public void addDrawable(Drawable drawable);

	public void removeDrawable(Drawable drawable);
	
	public Collection<Drawable> getDrawables();
	
	public Collection<Drawable> getAffectedDrawables(Rectangle area);
	
	public Collection<Drawable> getAffectedDrawables(Point point);
	
	// TODO
	
}
