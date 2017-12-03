package com.test.web.ui.swing;

import java.util.ArrayList;
import java.util.List;

import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.common.IUIWindow;
import com.test.web.ui.common.IWindowCloseListener;

public class SwingUIFactory implements IUIFactory {
	
	private final List<SwingUIWindow> windows = new ArrayList<>();
	
	@Override
	public IUIWindow createWindow(String title, int width, int height, IWindowCloseListener closeListener) {
		
		final SwingUIWindow uiWindow = new SwingUIWindow(title, width, height, closeListener);
		
		windows.add(uiWindow);
		
		return uiWindow;
	}

	@Override
	public void mainLoop() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				for (SwingUIWindow window : windows) {
					window.open();
				}
			}
		});
	}

	@Override
	public void exitMainLoop() {
		// TODO implement exit of mainloop
	}
}
