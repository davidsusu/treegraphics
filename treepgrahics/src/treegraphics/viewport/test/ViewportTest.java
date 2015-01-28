package treegraphics.viewport.test;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Drawable;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.util.Identified;
import treegraphics.valuetree.Value;
import treegraphics.valuetree.value.AverageValue;
import treegraphics.valuetree.value.StaticValue;
import treegraphics.viewport.Viewport;

public class ViewportTest {
	
	public static void main(String[] args) {
		Viewport viewport = new Viewport();

		final StaticValue valueX1 = new StaticValue(100);
		final StaticValue valueY1 = new StaticValue(100);
		final StaticValue valueX2 = new StaticValue(300);
		final StaticValue valueY2 = new StaticValue(170);
		final Value valueX3 = new AverageValue(valueX1, valueX2);
		final Value valueY3 = new AverageValue(valueY1, valueY2);

		viewport.addDrawable(new TestDrawable(valueX1, valueY1, 10, new Color(255, 0, 0)));
		viewport.addDrawable(new TestDrawable(valueX2, valueY2, 10, new Color(0, 255, 0)));
		viewport.addDrawable(new TestDrawable(valueX3, valueY3, 10, new Color(0, 0, 255)));
		
		final JPanel panel = viewport.getPanel();
		panel.setPreferredSize(new java.awt.Dimension(800, 500));
		
		panel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent ev) {
				valueX2.set(ev.getX());
				valueY2.set(ev.getY());
				panel.repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent ev) {
				mouseMoved(ev);
			}
			
		});
		
		JFrame frame = new JFrame("Viewport test");
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static class TestDrawable implements Drawable, Identified {
		
		final protected int id;
		
		protected Value xValue;
		
		protected Value yValue;
		
		protected double radius;
		
		protected Color color;
		
		public TestDrawable(Value xValue, Value yValue, double radius, Color color) {
			this.xValue = xValue;
			this.yValue = yValue;
			this.radius = radius;
			this.color = color;
			this.id = Identified.Id.getNext();
		}
		
		@Override
		public void draw(Canvas canvas) {
			canvas.setColor(color);
			canvas.fillRectangle(getReservedRectangle());
		}

		@Override
		public Rectangle getReservedRectangle() {
			return new Rectangle(new Point(xValue.get()-radius, yValue.get()-radius), new Dimension(radius*2, radius*2));
		}
		
		@Override
		public int getIdentifier() {
			return id;
		}
		
	}
	
}
