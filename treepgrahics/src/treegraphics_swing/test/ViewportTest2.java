package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import treegraphics.canvas.Color;
import treegraphics.valuetree.Value;
import treegraphics.valuetree.value.AverageValue;
import treegraphics.valuetree.value.StaticValue;
import treegraphics_swing.InteractionHandler;
import treegraphics_swing.SimpleSwingViewport;

public class ViewportTest2 {
	
	public static void main(String[] args) {
		SimpleSwingViewport viewport = new SimpleSwingViewport();

		final Value valueX1 = new StaticValue(300);
		final Value valueY1 = new StaticValue(300);
		final Value valueX2 = new StaticValue(500);
		final Value valueY2 = new StaticValue(100);

		final Value valueX3 = new StaticValue(400);
		final Value valueY3 = new StaticValue(400);
		final Value valueX4 = new StaticValue(600);
		final Value valueY4 = new StaticValue(200);

		final Value valueX5 = new AverageValue(valueX1, valueX2, valueX3, valueX4);
		final Value valueY5 = new AverageValue(valueY1, valueY2, valueY3, valueY4);

		viewport.addDrawable(new TestPoint(valueX5, valueY5, new StaticValue(1), 10, new Color(200, 100, 0)));

		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX2, valueY2, new StaticValue(2), new Color(0, 0, 0)));
		
		viewport.addDrawable(new TestPoint(valueX1, valueY1, new StaticValue(3), 10, new Color(255, 0, 0)));
		viewport.addDrawable(new TestPoint(valueX2, valueY2, new StaticValue(4), 10, new Color(0, 255, 0)));

		viewport.addDrawable(new TestLine(valueX3, valueY3, valueX4, valueY4, new StaticValue(5), new Color(0, 0, 0)));

		viewport.addDrawable(new TestPoint(valueX3, valueY3, new StaticValue(6), 10, new Color(0, 0, 255)));
		viewport.addDrawable(new TestPoint(valueX4, valueY4, new StaticValue(7), 10, new Color(255, 0, 255)));

		final Component component = viewport.getComponent();
		component.setPreferredSize(new java.awt.Dimension(800, 500));
		
		final InteractionHandler interactionHandler = new InteractionHandler(viewport, component);
		interactionHandler.init();
		
		JFrame frame = new JFrame("Viewport test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(component, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
}