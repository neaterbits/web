package com.test.web.browser.common;

import java.util.ArrayList;
import java.util.List;

import com.test.web.ui.common.IUIWIndow;

public class BrowserWindow<ELEMENT> {

	private final IUIWIndow uiWindow;
	
	// One or more browser tabs, if only one then we will not show tabs in the UI but that is handled elsewhere
	private final List<BrowserTab<ELEMENT>> tabs;
	
	BrowserWindow(IUIWIndow uiWindow, BrowserTab<ELEMENT> initialTab) {
		
		if (uiWindow == null) {
			throw new IllegalArgumentException("uiWindow == null");
		}
		
		if (initialTab == null) {
			throw new IllegalArgumentException("initialTab == null");
		}
		
		this.uiWindow = uiWindow;
		this.tabs = new ArrayList<>();
		
		tabs.add(initialTab);
	}
}
