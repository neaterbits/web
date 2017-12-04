package com.test.web.ui.swt;

import org.eclipse.swt.widgets.Display;

import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.common.IUIWindow;
import com.test.web.ui.common.IWindowCloseListener;

public class SWTUIFactory implements IUIFactory {

	private final Display display;
	private boolean runMainLoop;
	
	public SWTUIFactory(Display display) {
		this.display = display;
	}

	@Override
	public IUIWindow createWindow(String title, int width, int height, IWindowCloseListener closeListener) {
		
		final SWTUIWindow uiWindow = new SWTUIWindow(display, title, width, height, closeListener);
		
		return uiWindow;
	}

	@Override
	public void mainLoop() {
		
		this.runMainLoop = true;
		
		while (runMainLoop) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	@Override
	public void exitMainLoop() {
		this.runMainLoop = false;
	}
}
