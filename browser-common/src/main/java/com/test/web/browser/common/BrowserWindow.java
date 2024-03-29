package com.test.web.browser.common;

import java.util.ArrayList;
import java.util.List;

import com.test.web.document.html.common.IDocument;
import com.test.web.ui.common.IUIWindow;

public class BrowserWindow<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT extends IDocument<HTML_ELEMENT, HTML_ATTRIBUTE, DOCUMENT>> {

	private final IUIWindow uiWindow;
	
	// One or more browser tabs, if only one then we will not show tabs in the UI but that is handled elsewhere
	private final List<BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT>> tabs;
	
	BrowserWindow(IUIWindow uiWindow, BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT> initialTab) {
		
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
	
	boolean hasUIWindow(IUIWindow uiWindow) {
		
		if (uiWindow == null) {
			throw new IllegalArgumentException("uiWindow == null");
		}

		return this.uiWindow == uiWindow;
	}
}
