package com.test.web.ui.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.test.web.ui.common.IUIWindow;
import com.test.web.ui.common.IWindowCloseListener;

final class SwingUIWindow extends SwingContainer implements IUIWindow {

	private final JFrame window;
	
	SwingUIWindow(String title, int width, int height, IWindowCloseListener closeListener) {
		super(new JFrame(title));
		this.window = (JFrame)getContainer();
		
		window.setSize(width, height);
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (closeListener != null) {
					closeListener.onClosed(SwingUIWindow.this);
				}
			}
		});
	}
	
	void display() {
		//window.pack();
		window.setVisible(true);
	}
}
