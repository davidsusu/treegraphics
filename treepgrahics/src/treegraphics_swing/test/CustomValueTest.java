package treegraphics_swing.test;

import treegraphics.canvas.Color;
import treegraphics.valuetree.AbstractCustomValue;

public class CustomValueTest {

	public static void main(String[] args) {
		
		ColorStorageValue baseValue = new ColorStorageValue();
		
		ColorManipulatorValue dependValue = new ColorManipulatorValue();

		dependValue.setBase(baseValue);
		
		baseValue.setColor(new Color(255, 0, 0));
		System.out.println(dependValue.get());

		baseValue.setColor(new Color(100, 0, 0));
		System.out.println(dependValue.get());

		baseValue.setColor(new Color(150, 0, 0));
		System.out.println(dependValue.get());
		
	}
	
	protected static class ColorStorageValue extends AbstractCustomValue<Color> {
		
		Color color = null;
		
		@Override
		public void freeFromDependecies() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void reload() {
			cachedValue = color;
		}
		
		public void setColor(Color color) {
			this.expireState(this);
			this.color = color;
		}
		
	}
	
	protected static class ColorManipulatorValue extends AbstractCustomValue<Color> {
		
		AbstractCustomValue<Color> base = null;
		
		@Override
		public void freeFromDependecies() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void reload() {
			Color baseColor = base.get();
			int red = baseColor.getRed()/2;
			int green = baseColor.getRed();
			int blue = 20;
			cachedValue = new Color(red, green, blue);
		}
		
		public void setBase(AbstractCustomValue<Color> base) {
			if (this.base!=null) {
				this.base.unregisterDependent(this);
			}
			this.expireState(this);
			base.registerDependent(this);
			this.base = base;
		}
		
	}
}
