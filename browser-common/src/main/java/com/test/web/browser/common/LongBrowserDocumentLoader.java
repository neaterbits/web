package com.test.web.browser.common;

import com.test.web.document._long.LongHTMLDocument;
import com.test.web.document.common.Document;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.ITextExtent;

public class LongBrowserDocumentLoader
		extends BaseBrowserDocumentLoader<Integer, LongTokenizer>
		implements IBrowserDocumentLoader<Integer> {

	public LongBrowserDocumentLoader(IBufferRenderFactory renderFactory, ITextExtent textExtent) {
		super(renderFactory, textExtent);
	}

	@Override
	public Document<Integer> fromHTML(String html) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html);
	}
}
