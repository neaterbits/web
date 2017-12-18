package com.test.web.document.html._long;


import com.test.web.document.html._long.LongHTMLDocument;
import com.test.web.document.html.test.BaseHTMLDocumentTest;
import com.test.web.parse.common.ParserException;

public class HTMLDocumentTest extends BaseHTMLDocumentTest<Integer, Integer, LongHTMLDocument> {

	@Override
	protected LongHTMLDocument parseHTMLDocument(String html) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html, null);
	}
}
