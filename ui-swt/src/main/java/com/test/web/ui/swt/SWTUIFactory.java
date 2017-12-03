package com.test.web.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.common.IUIWindow;
import com.test.web.ui.common.IWindowCloseListener;

public class SWTUIFactory implements IUIFactory {

	private final Display display;
	
	private final List<SWTUIWindow> windows = new ArrayList<>();
	
	public SWTUIFactory(Display display) {
		this.display = display;
	}

	@Override
	public IUIWindow createWindow(String title, int width, int height, IWindowCloseListener closeListener) {
		
		final IWindowCloseListener closeListener2 = new IWindowCloseListener() {
			
			@Override
			public void onClosed(IUIWindow uiWindow) {
				if (closeListener != null) {
					closeListener.onClosed(uiWindow);
				}

				windows.remove(uiWindow);
			}
		};
		
		final SWTUIWindow uiWindow = new SWTUIWindow(display, title, width, height, closeListener2);
		
		windows.add(uiWindow);
		
		return uiWindow;
	}

	@Override
	public void mainLoop() {
		
		while (!windows.isEmpty()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	@Override
	public void exitMainLoop() {
		// TODO implement exit of mainloop
	}
}
