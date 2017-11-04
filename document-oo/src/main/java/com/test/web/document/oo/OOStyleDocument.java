package com.test.web.document.oo;

import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.oo.BaseOOCSSDocument;
import com.test.web.css.oo.OOCSSElement;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.html.IHTMLStyleParserListener;

public class OOStyleDocument extends BaseOOCSSDocument
	implements IHTMLStyleParserListener<OOTagElement, OOTokenizer> {

	public OOStyleDocument() {
	}

	@Override
	public Void onEntityStart(CSSTarget entity, String id) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	@Override
	public void onEntityEnd(Void context) {
		throw new UnsupportedOperationException("Not required for styles elements");
	}

	// Called before starting parsing of a CSS element, allows us to map the
	// parsed element
	// to the HTML element that contains it
	@Override
	public void startParseStyleElement(OOTagElement htmlElement) {
		if (htmlElement.getStyleElement() != null) {
			throw new IllegalStateException("already has style element");
		}
		
		final OOCSSElement cssElement = new OOCSSElement("style_element");
		
		htmlElement.setStyleElement(cssElement);
	}
}
