package com.test.web.browser.common;

import java.net.URL;

import com.test.web.document.common.Document;
import com.test.web.layout.LayoutAlgorithm;
import com.test.web.parse.common.ParserException;
import com.test.web.ui.common.IUICanvas;

final class BrowserTab<ELEMENT> {

	private final IUICanvas uiCanvas;
	private final IBrowserDocumentLoader<ELEMENT> documentLoader;

	BrowserTab(IUICanvas uiCanvas, IBrowserDocumentLoader<ELEMENT> documentLoader) {
		
		if (uiCanvas == null) {
			throw new IllegalArgumentException("uiCanvas == null");
		}
		
		this.uiCanvas = uiCanvas;
		this.documentLoader = documentLoader;
	}
	
	void loadURL(URL url) {
		throw new UnsupportedOperationException("TODO");
	}
	
	void showHTML(String html) throws ParserException {
		final Document<ELEMENT> document = documentLoader.fromHTML(html);

		// Layout and render the document in one pass
		
	}
}
