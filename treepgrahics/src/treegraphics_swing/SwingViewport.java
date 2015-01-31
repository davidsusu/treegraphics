package treegraphics_swing;

import javax.swing.JPanel;

import treegraphics_awt.AwtViewport;

public interface SwingViewport extends AwtViewport {

	@Override
	public JPanel getComponent();
	
}
