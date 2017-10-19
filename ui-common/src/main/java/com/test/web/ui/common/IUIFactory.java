package com.test.web.ui.common;

public interface IUIFactory extends IUIElements {

	IUIWindow createWindow(String title, int width, int height, IWindowCloseListener closeListener);

	void mainLoop();
	
	void exitMainLoop();
}
