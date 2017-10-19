package com.test.web.browser.common;

import com.test.web.document._long.LongHTMLDocument;
import com.test.web.document.common.Document;
import com.test.web.layout.PageLayout;
import com.test.web.parse.common.ParserException;

public class LongBrowserDocumentLoader implements IBrowserDocumentLoader<Integer> {

	@Override
	public Document<Integer> fromHTML(String html) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html);
	}

	@Override
	public PageLayout<Integer> layout() {
		// TODO Auto-generated method stub
		return null;
	}
}
