package com.test.web.ui.swt;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.test.web.ui.common.IUIWindow;
import com.test.web.ui.common.IWindowCloseListener;
import com.test.web.ui.common.UILayoutData;

final class SWTUIWindow extends SWTContainer implements IUIWindow {

	private final Shell window;
	
	SWTUIWindow(Display display,String title, int width, int height, IWindowCloseListener closeListener) {
		super(new Shell(display));
		this.window = (Shell)getContainer();
		
		window.setSize(width, height);
		window.setLayout(new FillLayout());
		
		window.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (closeListener != null) {
					closeListener.onClosed(SWTUIWindow.this);
				}
			}
		});
	}

	@Override
	public void open() {
		window.open();
	}

	@Override
	void add(UILayoutData layoutData, Control component) {
		// Nothing to do
	}
}
