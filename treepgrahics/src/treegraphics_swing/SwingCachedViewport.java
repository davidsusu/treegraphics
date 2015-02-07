package treegraphics_swing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import treegraphics_awt.AwtCachedViewport;

public class SwingCachedViewport extends AwtCachedViewport implements SwingViewport {

	public SwingCachedViewport() {
		initComponent();
	}
	
	protected void initComponent() {
		this.component = new JPanel() {
			
			public static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				SwingCachedViewport.this.repaintScreen((Graphics2D)g);
			}
			
		};
	}

	@Override
	public JPanel getComponent() {
		return ((JPanel)component);
	}
	
}
