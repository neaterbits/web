package com.test.web.browser.common;

import java.io.IOException;
import java.net.URL;

import com.test.web.document.common.Document;
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
		//  Load and display page using load queue, rendering while parsing
		try {
			documentLoader.load(url, uiCanvas.getWidth(), uiCanvas.getHeight(), uiCanvas);
		} catch (IOException | ParserException ex) {
			throw new IllegalStateException("Failed to load page", ex);
		}
	}

	void showHTML(String html) throws ParserException {
		final Document<ELEMENT> document = documentLoader.fromHTML(html);

		// Layout and render the document in one pass
		documentLoader.layout(document, uiCanvas.getWidth(), uiCanvas.getHeight(), uiCanvas);
	}
}
