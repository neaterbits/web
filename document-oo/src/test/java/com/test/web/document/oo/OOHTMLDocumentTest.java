package com.test.web.document.oo;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.css.oo.OOCSSDocument;
import com.test.web.css.oo.OOCSSElement;
import com.test.web.document.test.BaseHTMLDocumentTest;
import com.test.web.io.common.CharInput;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;

public class OOHTMLDocumentTest extends BaseHTMLDocumentTest<OOTagElement, OOHTMLDocument>{

	private OOCSSDocument parseCSS(CharInput charInput, CSSContext<OOCSSElement> cssContext) throws IOException, ParserException {
		
		final OOCSSDocument styleDocument = new OOCSSDocument();
		final CSSParser<OOTokenizer, Void> cssParser = new CSSParser<>(charInput, styleDocument);

		// Just parse the CSS straight away
		cssParser.parseCSS();
		
		cssContext.addDocument(styleDocument);

		return styleDocument;
	}

	@Override
	protected OOHTMLDocument parseHTMLDocument(String html) throws ParserException {
		
		final CSSContext<OOCSSElement> cssContext = new CSSContext<>();
		
		return OOHTMLDocument.parseHTMLDocument(html, charInput -> parseCSS(charInput, cssContext));
	}
}
