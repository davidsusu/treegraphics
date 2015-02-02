package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import treegraphics.canvas.Color;
import treegraphics.valuetree.Value;
import treegraphics.valuetree.value.StaticValue;
import treegraphics_awt.test.TestLine;
import treegraphics_awt.test.TestMoveInteractionHandler;
import treegraphics_awt.test.TestPoint;
import treegraphics_swing.SwingCachedViewport;

public class ViewportTest3 {

	public static void main(String[] args) {
		SwingCachedViewport viewport = new SwingCachedViewport();
		
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
