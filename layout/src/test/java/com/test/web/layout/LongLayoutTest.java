package com.test.web.layout;

import com.test.web.document._long.LongHTMLDocument;
import com.test.web.document.html.common.IDocument;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.common.ParserException;

public class LongLayoutTest extends BaseLayoutTest<Integer, LongTokenizer> {
	
	@Override
	protected IDocument<Integer> parseDocument(String html) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html, null);
	}
}
