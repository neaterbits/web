package com.test.web.layout.html;

import com.test.web.document.html._long.LongHTMLDocument;
import com.test.web.document.html.common.IDocument;
import com.test.web.parse.common.ParserException;

public class LongLayoutTest extends BaseLayoutTest<Integer, Integer> {
	
	@Override
	protected IDocument<Integer, Integer> parseDocument(String html) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html, null);
	}
}