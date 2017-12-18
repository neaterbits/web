package com.test.web.browser.common;

import java.io.IOException;
import java.net.URL;

import com.test.web.css.common.CSSContext;
import com.test.web.document.html.common.IDocument;
import com.test.web.parse.common.ParserException;
import com.test.web.ui.common.IUICanvas;

final class BrowserTab<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT> {

	private final IUICanvas uiCanvas;
	private final IBrowserDocumentLoader<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT> documentLoader;

	BrowserTab(IUICanvas uiCanvas, IBrowserDocumentLoader<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT> documentLoader) {
		
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

	void showHTML(String html, CSSContext<CSS_ELEMENT> cssContext) throws ParserException {
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> document = documentLoader.fromHTML(html, cssContext);

		// Layout and render the document in one pass
		documentLoader.layout(document, uiCanvas.getWidth(), uiCanvas.getHeight(), uiCanvas);
	}
}
