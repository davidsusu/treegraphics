package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Rectangle;
import treegraphics.valuetree.DoubleValue;
import treegraphics.valuetree.value.AverageValue;
import treegraphics.valuetree.value.StaticValue;
import treegraphics.viewport.Viewport;
import treegraphics_awt.test.TestLine;
import treegraphics_awt.test.TestMoveInteractionHandler;
import treegraphics_awt.test.TestPoint;
import treegraphics_swing.SwingSimpleViewport;

public class ViewportTest2 {
	
	public static void main(String[] args) {
		final SwingSimpleViewport viewport = new SwingSimpleViewport();
		
		viewport.addDrawListener(new Viewport.DrawListener() {

			@Override
			public void beforeRefresh(Canvas canvas, Rectangle area) {
				canvas.setColor(new Color(255, 0, 0));
				canvas.fillRectangle(viewport.getFullArea());
				
				canvas.setColor(new Color(255, 255, 255));
				canvas.fillRectangle(area);
				
				Rectangle drawRectangle = new Rectangle(250, 90, 600, 120);
				if (drawRectangle.intersects(area)) {
					canvas.setColor(new Color(50, 0, 200));
					canvas.fillRectangle(drawRectangle);
				}
			}
			
			@Override
			public void beforeDraw(Canvas canvas, Rectangle area) {
			}
			
			@Override
			public void afterDraw(Canvas canvas, Rectangle area) {
				canvas.setColor(new Color(200, 100, 0));
				canvas.fillRectangle(new Rectangle(10, 10, 300, 30));
			}
			
			@Override
			public void afterRefresh(Canvas canvas, Rectangle area) {
				canvas.setColor(new Color(100, 0, 100));
				canvas.fillRectangle(new Rectangle(10, 50, 200, 80));
			}
			
		});
		
		final DoubleValue valueX0 = new StaticValue(100);
		final DoubleValue valueY0 = new StaticValue(100);

		final DoubleValue valueX1 = new StaticValue(300);
		final DoubleValue valueY1 = new StaticValue(300);
		final DoubleValue valueX2 = new StaticValue(500);
		final DoubleValue valueY2 = new StaticValue(100);

		final DoubleValue valueX3 = new StaticValue(400);
		final DoubleValue valueY3 = new StaticValue(400);
		final DoubleValue valueX4 = new StaticValue(600);
		final DoubleValue valueY4 = new StaticValue(200);

		final DoubleValue valueX5 = new AverageValue(valueX1, valueX2, valueX3, valueX4);
		final DoubleValue valueY5 = new AverageValue(valueY1, valueY2, valueY3, valueY4);

		viewport.addDrawable(new TestPoint(valueX0, valueY0, new StaticValue(0), 10, new Color(150, 150, 150)));
		
		viewport.addDrawable(new TestPoint(valueX5, valueY5, new StaticValue(1), 10, new Color(200, 100, 0)));

		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX2, valueY2, new StaticValue(2), new Color(0, 0, 0)));
		
		viewport.addDrawable(new TestPoint(valueX1, valueY1, new StaticValue(3), 10, new Color(255, 0, 0)));
		viewport.addDrawable(new TestPoint(valueX2, valueY2, new StaticValue(4), 10, new Color(0, 255, 0)));

		viewport.addDrawable(new TestLine(valueX3, valueY3, valueX4, valueY4, new StaticValue(5), new Color(0, 0, 0)));

		viewport.addDrawable(new TestPoint(valueX3, valueY3, new StaticValue(6), 10, new Color(0, 0, 255)));
		viewport.addDrawable(new TestPoint(valueX4, valueY4, new StaticValue(7), 10, new Color(255, 0, 255)));
		
		final Component component = viewport.getComponent();
		component.setPreferredSize(new java.awt.Dimension(800, 500));
		
		final TestMoveInteractionHandler interactionHandler = new TestMoveInteractionHandler(viewport, component);
		interactionHandler.init();
		
		JFrame frame = new JFrame("Viewport test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(component, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
		viewport.rebuild();
	}
	
}