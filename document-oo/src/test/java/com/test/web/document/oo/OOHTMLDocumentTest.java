package com.test.web.document.oo;

import com.test.web.document.test.BaseHTMLDocumentTest;
import com.test.web.parse.common.ParserException;

public class OOHTMLDocumentTest extends BaseHTMLDocumentTest<OOTagElement, OOHTMLDocument>{

	@Override
	protected OOHTMLDocument parseHTMLDocument(String html) throws ParserException {
		return OOHTMLDocument.parseHTMLDocument(html, null);
	}
}
