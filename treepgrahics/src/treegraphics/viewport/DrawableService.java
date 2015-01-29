package treegraphics.viewport;

import java.awt.Rectangle;
import java.util.Collection;

import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;

public interface DrawableService {

	public void addDrawable(Drawable drawable);

	public void removeDrawable(Drawable drawable);

	public void setActiveArea(Rectangle area);

	public Rectangle getActiveArea();
	
	public Collection<Drawable> getDrawables();
	
	public Collection<Drawable> getAffectedDrawables(Rectangle area);
	
	public Collection<Drawable> getAffectedDrawables(Point point);
	
	// TODO
	
}
