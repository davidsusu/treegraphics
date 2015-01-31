package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import treegraphics_awt.test.TestMoveInteractionHandler;
import treegraphics_swing.SwingCachedViewport;

public class ViewportTest3 {

	public static void main(String[] args) {
		SwingCachedViewport viewport = new SwingCachedViewport();
	
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
