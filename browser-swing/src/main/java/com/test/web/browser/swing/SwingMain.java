package com.test.web.browser.swing;

import com.test.web.browser.common.BrowserMain;
import com.test.web.browser.common.IBrowserDocumentLoader;
import com.test.web.browser.common.LongBrowserDocumentLoader;
import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.swing.SwingUIFactory;

public class SwingMain {

	public static void main(String [] args) {
		final IUIFactory uiFactory = new SwingUIFactory();
		final IBrowserDocumentLoader<Integer> documentLoader = new LongBrowserDocumentLoader();
		
		final BrowserMain<Integer> main = new BrowserMain<>(uiFactory, documentLoader);
		
		main.showStartPage();
		
		uiFactory.mainLoop();
	}
}
