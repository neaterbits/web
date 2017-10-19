package com.test.web.browser.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.test.web.parse.common.ParserException;
import com.test.web.ui.common.IUICanvas;
import com.test.web.ui.common.IUIContainer;
import com.test.web.ui.common.IUIFactory;
import com.test.web.ui.common.IUIHBox;
import com.test.web.ui.common.IUIVBox;
import com.test.web.ui.common.IUIWIndow;

public class BrowserMain<ELEMENT> {

	private final IUIFactory uiFactory;
	private final IBrowserDocumentLoader<ELEMENT> documentLoader;
	private final List<BrowserWindow<ELEMENT>> windows;
	
	public BrowserMain(IUIFactory uiFactory, IBrowserDocumentLoader<ELEMENT> documentLoader) {
		
		if (uiFactory == null) {
			throw new IllegalArgumentException("uiFactory == null");
		}
		
		if (documentLoader == null) {
			throw new IllegalArgumentException("documentLoader == null");
		}

		this.uiFactory = uiFactory;
		this.documentLoader = documentLoader;
		this.windows = new ArrayList<>();
	}

	public BrowserTab<ELEMENT> loadInNewWindow(URL url) {
		final BrowserTab<ELEMENT> tab = openBrowserWindow();
		
		tab.loadURL(url);
		
		return tab;
	}
	
	public BrowserTab<ELEMENT> showInNewWindow(String html) throws ParserException {
		final BrowserTab<ELEMENT> tab = openBrowserWindow();
		
		tab.showHTML(html);
		
		return tab;
	}
	
	private BrowserTab<ELEMENT> openBrowserWindow() {
		final IUIWIndow window = uiFactory.createWindow("");

		// Browser URL line is a horizontal line containing url string line
		final IUIVBox vbox = window.createVBox();

		final IUIHBox inputHBox = vbox.createHBox();

		// The browser itself, will be created using a canvas
		final BrowserTab<ELEMENT> tab  = createBrowserTab(vbox);

		inputHBox.createString(changedValue -> {
			try {
				final URL url;

				url = new URL(changedValue);

				tab.loadURL(url);
			} catch (MalformedURLException e) {
				// TODO: Error dialog?
			}
		});

		final BrowserWindow<ELEMENT> browserWindow = new BrowserWindow<>(window,  tab);

		windows.add(browserWindow);
		
		return tab;
	}
	
	private BrowserTab<ELEMENT> createBrowserTab(IUIContainer container) {
		final IUICanvas canvas = container.createCanvas();
		
		return new BrowserTab<>(canvas, documentLoader);
	}
}
