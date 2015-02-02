package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import treegraphics.canvas.Canvas;
import treegraphics.canvas.Color;
import treegraphics.canvas.Dimension;
import treegraphics.canvas.Point;
import treegraphics.canvas.Rectangle;
import treegraphics.valuetree.Value;
import treegraphics.valuetree.value.StaticValue;
import treegraphics_awt.test.TestLine;
import treegraphics_awt.test.TestMoveInteractionHandler;
import treegraphics_awt.test.TestPoint;
import treegraphics_swing.SwingCachedViewport;

public class ViewportTest3 {

	public static void main(String[] args) {
		SwingCachedViewport viewport = new SwingCachedViewport() {

			{
				addDrawListener(new DrawListener() {
					
					@Override
					public void beforeDraw(Canvas canvas, Rectangle area) {
					}
					
					@Override
					public void afterDraw(Canvas canvas, Rectangle area) {
						canvas.setColor(new Color(150, 50, 0));
						canvas.drawRectangle(new Rectangle(new Point(area.getLeft()+50, area.getTop()+50), new Dimension(area.getWidth()-100, area.getHeight()-100)));
					}
					
				});
					
			}
			
			@Override
			public int getWidth() {
				return 200;
			}

			@Override
			public int getHeight() {
				return 100;
			}

			@Override
			public int getXDisplacement() {
				return 300;
			}

			@Override
			public int getYDisplacement() {
				return 200;
			}
			
		};
		
		Value valueX1 = new StaticValue(100);
		Value valueY1 = new StaticValue(100);
		Value valueX2 = new StaticValue(500);
		Value valueY2 = new StaticValue(300);
		
		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX2, valueY2, new StaticValue(2), new Color(0, 0, 0)));

		viewport.addDrawable(new TestPoint(valueX1, valueY1, new StaticValue(2), 10, new Color(255, 0, 0)));
		viewport.addDrawable(new TestPoint(valueX2, valueY2, new StaticValue(2), 10, new Color(0, 0, 255)));
		
		final Component component = viewport.getComponent();
		component.setPreferredSize(new java.awt.Dimension(800, 500));
		
		final TestMoveInteractionHandler interactionHandler = new TestMoveInteractionHandler(viewport, component);
		interactionHandler.init();
		
		JFrame frame = new JFrame("Viewport test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(component, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
