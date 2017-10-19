package com.test.web.ui.swing;

import javax.swing.JFrame;

import com.test.web.ui.common.IUIWIndow;

final class SwingUIWindow extends SwingContainer implements IUIWIndow {

	private final JFrame window;
	
	SwingUIWindow(String title) {
		super(new JFrame(title));
		this.window = (JFrame)getContainer();
	}
	
	void display() {
		window.pack();
		window.setVisible(true);
	}
}
