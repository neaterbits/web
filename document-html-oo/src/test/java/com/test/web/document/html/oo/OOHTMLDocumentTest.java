package com.test.web.document.html.oo;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.css.oo.OOCSSDocument;
import com.test.web.css.oo.OOCSSRule;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.document.html.test.BaseHTMLDocumentTest;
import com.test.web.document.html.test.DocumentParser;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.ParserException;

public class OOHTMLDocumentTest extends BaseHTMLDocumentTest<OOTagElement, OOAttribute, OOHTMLDocument>{

	private OOCSSDocument parseCSS(CharInput charInput, Tokenizer tokenizer, CSSContext<OOCSSRule> cssContext) throws IOException, ParserException {
		
		final OOCSSDocument styleDocument = new OOCSSDocument();
		
		return DocumentParser.parseCSS(charInput, tokenizer, cssContext, styleDocument);
	}

	@Override
	protected OOHTMLDocument parseHTMLDocument(String html) throws ParserException {

		final CSSContext<OOCSSRule> cssContext = new CSSContext<>();
		
		return OOHTMLDocument.parseHTMLDocument(html, (charInput, tokenizer) -> parseCSS(charInput, tokenizer, cssContext));
	}
}
