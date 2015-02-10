package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Rectangle;
import treegraphics.valuetree.doublevalue.StaticValue;
import treegraphics.viewport.Viewport;
import treegraphics_awt.test.TestLine;
import treegraphics_awt.test.TestInteractionHandler;
import treegraphics_awt.test.TestPoint;
import treegraphics_swing.SwingCachedViewport;

public class ViewportTest3 {

	public static void main(String[] args) {
		final SwingCachedViewport viewport = new SwingCachedViewport();
		
		viewport.addDrawListener(new Viewport.DrawListener() {

			@Override
			public void beforeRefresh(Canvas canvas, Rectangle area) {
				canvas.setColor(new Color(255, 0, 0));
				canvas.fillRectangle(viewport.getFullArea());
				
				canvas.setColor(new Color(255, 255, 255));
				canvas.fillRectangle(area);
				
				canvas.setColor(new Color(90, 90, 90));
				canvas.fillRectangle(new Rectangle(155, 130, 170, 145));
			}
			
			@Override
			public void beforeDraw(Canvas canvas, Rectangle area) {
				canvas.setColor(new Color(150, 170, 250));
				canvas.fillRectangle(new Rectangle(145, 120, 160, 135));
			}
			
			@Override
			public void afterDraw(Canvas canvas, Rectangle area) {
				canvas.setColor(new Color(0, 255, 255));
				canvas.fillRectangle(new Rectangle(100, 100, 115, 115));
			}
			
			@Override
			public void afterRefresh(Canvas canvas, Rectangle area) {
				canvas.setColor(new Color(50, 170, 0));
				canvas.fillRectangle(new Rectangle(125, 110, 140, 125));
			}
			
		});
		
		
		StaticValue valueX1 = new StaticValue(100);
		StaticValue valueY1 = new StaticValue(100);
		StaticValue valueX2 = new StaticValue(500);
		StaticValue valueY2 = new StaticValue(300);
		
		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX2, valueY2, new StaticValue(1), new Color(0, 0, 0)));

		viewport.addDrawable(new TestPoint(valueX1, valueY1, new StaticValue(2), 10, new Color(255, 0, 0)));
		viewport.addDrawable(new TestPoint(valueX2, valueY2, new StaticValue(3), 10, new Color(0, 0, 255)));
		
		final Component component = viewport.getComponent();
		component.setPreferredSize(new java.awt.Dimension(800, 500));
		
		final TestInteractionHandler interactionHandler = new TestInteractionHandler(viewport, component);
		interactionHandler.init();
		
		JFrame frame = new JFrame("Viewport test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(component, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
		viewport.rebuild();
	}

}
