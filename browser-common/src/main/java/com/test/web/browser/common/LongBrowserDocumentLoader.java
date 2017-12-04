package com.test.web.browser.common;

import com.test.web.document._long.LongHTMLDocument;
import com.test.web.document.common.Document;
import com.test.web.io._long.LongTokenizer;
import com.test.web.io.common.LoadStream;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.ITextExtent;

public class LongBrowserDocumentLoader
		extends BaseBrowserDocumentLoader<Integer, LongTokenizer, LongHTMLDocument>
		implements IBrowserDocumentLoader<Integer> {

	public LongBrowserDocumentLoader(IBufferRenderFactory renderFactory, ITextExtent textExtent) {
		super(renderFactory, textExtent);
	}

	@Override
	public Document<Integer> fromHTML(String html) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html);
	}

	@Override
	protected LongHTMLDocument createDocument() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	protected HTMLParser<Integer, LongTokenizer> createParser(LongHTMLDocument document,
			IHTMLParserListener<Integer, LongTokenizer> parserListener, LoadStream stream) {
		throw new UnsupportedOperationException("TODO");
	}
}
