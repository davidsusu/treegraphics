package treegraphics.viewport;

import java.awt.Rectangle;
import java.util.Collection;

import treegraphics.canvas.Drawable;

public interface DrawableService {

	public void addDrawable(Drawable drawable);

	public void removeDrawable(Drawable drawable);

	public void setActiveArea(Rectangle area);

	public Rectangle getActiveArea();
	
	public Collection<Drawable> getDrawables();
	
	public Collection<Drawable> getAffectedDrawables(Rectangle area);
	
	// TODO
	
}
