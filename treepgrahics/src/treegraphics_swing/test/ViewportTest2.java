package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import treegraphics.canvas.Color;
import treegraphics.valuetree.value.StaticValue;
import treegraphics_swing.InteractionHandler;
import treegraphics_swing.SimpleSwingViewport;

public class ViewportTest2 {
	
	public static void main(String[] args) {
		SimpleSwingViewport viewport = new SimpleSwingViewport();

		final StaticValue valueX1 = new StaticValue(300);
		final StaticValue valueY1 = new StaticValue(300);
		final StaticValue valueX2 = new StaticValue(500);
		final StaticValue valueY2 = new StaticValue(100);
		
		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX2, valueY2, new Color(0, 0, 0)));
		
		viewport.addDrawable(new TestPoint(valueX1, valueY1, 10, new Color(255, 0, 0)));
		viewport.addDrawable(new TestPoint(valueX2, valueY2, 10, new Color(0, 255, 0)));
		
		final Component component = viewport.getComponent();
		component.setPreferredSize(new java.awt.Dimension(800, 500));
		
		final InteractionHandler interactionHandler = new InteractionHandler(viewport, component);
		interactionHandler.init();
		
		JFrame frame = new JFrame("Viewport test");
		frame.getContentPane().add(component, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
}