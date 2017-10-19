package com.test.web.ui.swing;

import java.util.ArrayList;
import java.util.List;

import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.common.IUIWIndow;

public class SwingUIFactory implements IUIFactory {
	
	private final List<SwingUIWindow> windows = new ArrayList<>();
	
	@Override
	public IUIWIndow createWindow(String title) {
		
		final SwingUIWindow uiWindow = new SwingUIWindow(title);
		
		windows.add(uiWindow);
		
		return uiWindow;
	}

	@Override
	public void mainLoop() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				for (SwingUIWindow window : windows) {
					window.display();
				}
			}
		});
	}
}
