package com.test.web.document._long;


import com.test.web.document.test.BaseHTMLDocumentTest;
import com.test.web.parse.common.ParserException;

public class HTMLDocumentTest extends BaseHTMLDocumentTest<Integer, LongHTMLDocument> {

	@Override
	protected LongHTMLDocument parseHTMLDocument(String html) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html, null);
	}
}
