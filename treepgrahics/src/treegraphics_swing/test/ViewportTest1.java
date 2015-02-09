package treegraphics_swing.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import treegraphics.canvas.Color;
import treegraphics.valuetree.DoubleValue;
import treegraphics.valuetree.value.AverageValue;
import treegraphics.valuetree.value.StaticValue;
import treegraphics_awt.test.TestLine;
import treegraphics_awt.test.TestPoint;
import treegraphics_swing.SwingSimpleViewport;

public class ViewportTest1 {
	
	public static void main(String[] args) {
		SwingSimpleViewport viewport = new SwingSimpleViewport();

		final StaticValue valueX1 = new StaticValue(100);
		final StaticValue valueY1 = new StaticValue(300);
		final StaticValue valueX2 = new StaticValue(300);
		final StaticValue valueY2 = new StaticValue(100);
		final StaticValue valueX3 = new StaticValue(400);
		final StaticValue valueY3 = new StaticValue(420);
		final DoubleValue valueX4 = new AverageValue(valueX1, valueX2, valueX3);
		final DoubleValue valueY4 = new AverageValue(valueY1, valueY2, valueY3);
		final DoubleValue valueX5 = new AverageValue(valueX1, valueX2);
		final DoubleValue valueY5 = new AverageValue(valueY1, valueY2);
		final DoubleValue valueX6 = new AverageValue(valueX1, valueX3);
		final DoubleValue valueY6 = new AverageValue(valueY1, valueY3);
		final DoubleValue valueX7 = new AverageValue(valueX2, valueX3);
		final DoubleValue valueY7 = new AverageValue(valueY2, valueY3);

		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX2, valueY2, new StaticValue(1), new Color(0, 0, 0)));
		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX3, valueY3, new StaticValue(2), new Color(0, 0, 0)));
		viewport.addDrawable(new TestLine(valueX2, valueY2, valueX3, valueY3, new StaticValue(3), new Color(0, 0, 0)));
		viewport.addDrawable(new TestLine(valueX1, valueY1, valueX7, valueY7, new StaticValue(4), new Color(0, 0, 0)));
		viewport.addDrawable(new TestLine(valueX2, valueY2, valueX6, valueY6, new StaticValue(5), new Color(0, 0, 0)));
		viewport.addDrawable(new TestLine(valueX3, valueY3, valueX5, valueY5, new StaticValue(6), new Color(0, 0, 0)));
		
		viewport.addDrawable(new TestPoint(valueX1, valueY1, new StaticValue(7), 10, new Color(255, 0, 0)));
		viewport.addDrawable(new TestPoint(valueX2, valueY2, new StaticValue(8), 10, new Color(0, 255, 0)));
		viewport.addDrawable(new TestPoint(valueX5, valueY5, new StaticValue(9), 10, new Color(255, 255, 0)));

		viewport.addDrawable(new TestPoint(valueX4, valueY4, new StaticValue(10), 10, new Color(0, 0, 0)));
		
		viewport.addDrawable(new TestPoint(valueX6, valueY6, new StaticValue(11), 10, new Color(255, 0, 255)));
		viewport.addDrawable(new TestPoint(valueX7, valueY7, new StaticValue(12), 10, new Color(0, 255, 255)));

		viewport.addDrawable(new TestPoint(valueX3, valueY3, new StaticValue(13), 10, new Color(0, 0, 255)));
		
		final Component panel = viewport.getComponent();
		panel.setPreferredSize(new java.awt.Dimension(800, 500));
		
		panel.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent ev) {
				valueX3.set(ev.getX());
				valueY3.set(ev.getY());
				panel.repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent ev) {
				mouseMoved(ev);
			}
			
		});
		
		JFrame frame = new JFrame("Viewport test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
		viewport.rebuild();
	}
	
}