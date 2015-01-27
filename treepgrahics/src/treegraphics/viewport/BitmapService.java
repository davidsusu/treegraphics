package treegraphics.viewport;

import treegraphics.canvas.Canvas;

public interface BitmapService {

	public void createBitmap(int width, int height);
	
	public static interface Bitmap {
		
		public Canvas getCanvas();
		
		public Bitmap createParent(int width, int height);
		
		public Bitmap createChild(int width, int height);
		
		// FIXME
		public void copyToParent(int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2);

		// FIXME
		public void draw(int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2);
		
	}
	
}
