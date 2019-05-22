package com.test.web.layout.html;

import com.neaterbits.util.parse.ParserException;
import com.test.web.document.html._long.LongHTMLDocument;
import com.test.web.parse.html.util.ParseHTML;

public class LongLayoutTest extends BaseLayoutTest<Integer, Integer, LongHTMLDocument> {
	
	@Override
	protected LongHTMLDocument parseDocument(String html) throws ParserException {
		return ParseHTML.parseLongHTMLDocument(html, null);
	}
}
