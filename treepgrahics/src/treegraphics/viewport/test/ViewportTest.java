package treegraphics.viewport.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.valuetree.Value;
import treegraphics.valuetree.value.AverageValue;
import treegraphics.valuetree.value.StaticValue;
import treegraphics.viewport.Viewport;

public class ViewportTest {
	
	public static void main(String[] args) {
		Viewport viewport = new Viewport();

		Value valueX1 = new StaticValue(100);
		Value valueY1 = new StaticValue(100);
		Value valueX2 = new StaticValue(300);
		Value valueY2 = new StaticValue(100);
		Value valueX3 = new AverageValue(valueX1, valueX2);
		Value valueY3 = new AverageValue(valueY1, valueY2);

		viewport.addDrawable(new TestDrawable(valueX1, valueY1, 10, new Color(0, 0, 0)));
		viewport.addDrawable(new TestDrawable(valueX2, valueY2, 10, new Color(0, 0, 0)));
		viewport.addDrawable(new TestDrawable(valueX3, valueY3, 10, new Color(0, 0, 0)));
		
		JPanel panel = viewport.getPanel();
		panel.setPreferredSize(new java.awt.Dimension(800, 500));
		
		JFrame frame = new JFrame("Viewport test");
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static class TestDrawable implements Drawable {

		protected Value xValue;
		
		protected Value yValue;
		
		protected double radius;
		
		protected Color color;
		
		public TestDrawable(Value xValue, Value yValue, double radius, Color color) {
			this.xValue = xValue;
			this.yValue = yValue;
			this.radius = radius;
			this.color = color;
		}
		
		@Override
		public void draw(Canvas canvas) {
			canvas.setColor(color);
			canvas.fillRectangle(getReservedRectangle());
		}

		@Override
		public Rectangle getReservedRectangle() {
			return new Rectangle(new Point(xValue.get(), yValue.get()), new Dimension(radius*2, radius*2));
		}
		
	}
	
}
