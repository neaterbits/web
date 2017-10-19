package com.test.web.ui.common;

public interface IUIFactory extends IUIElements {

	IUIWIndow createWindow(String title);

	void mainLoop();
	
}
