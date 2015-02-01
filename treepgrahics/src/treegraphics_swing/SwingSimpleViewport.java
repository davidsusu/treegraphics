package treegraphics_swing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import treegraphics_awt.AwtSimpleViewport;

public class SwingSimpleViewport extends AwtSimpleViewport implements SwingViewport {
	
	public SwingSimpleViewport() {
		super();
	}
	
	@Override
	protected void initComponent() {
		this.component = new JPanel() {
			
			public static final long serialVersionUID = 1L;
			
			@Override
			protected void paintComponent(Graphics g) {
				SwingSimpleViewport.this.repaint((Graphics2D)g);
			}
			
		};
	}
	
	@Override
	public JPanel getComponent() {
		return ((JPanel)component);
	}
	
}

