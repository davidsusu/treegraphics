package treegraphics_swing;

import java.awt.Graphics;

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
				// TODO
			}
			
		};
	}

	@Override
	public JPanel getComponent() {
		return ((JPanel)component);
	}
	
}
