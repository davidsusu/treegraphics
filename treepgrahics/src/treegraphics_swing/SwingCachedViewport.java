package treegraphics_swing;

import javax.swing.JPanel;

import treegraphics_awt.AwtCachedViewport;

public class SwingCachedViewport extends AwtCachedViewport implements SwingViewport {

	@Override
	public JPanel getComponent() {
		return ((JPanel)component);
	}
	
}
