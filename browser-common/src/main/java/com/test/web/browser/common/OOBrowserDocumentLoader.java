package com.test.web.browser.common;

import com.test.web.document.common.Document;
import com.test.web.document.oo.OOHTMLDocument;
import com.test.web.document.oo.OOTagElement;
import com.test.web.io.common.LoadStream;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.ITextExtent;

public class OOBrowserDocumentLoader
		extends BaseBrowserDocumentLoader<OOTagElement, OOTokenizer, OOHTMLDocument>
		implements IBrowserDocumentLoader<OOTagElement> {
	
	public OOBrowserDocumentLoader(IBufferRenderFactory renderFactory, ITextExtent textExtent) {
		super(renderFactory, textExtent);
	}

	@Override
	public Document<OOTagElement> fromHTML(String html) throws ParserException {
		return OOHTMLDocument.parseHTMLDocument(html);
	}

	@Override
	protected OOHTMLDocument createDocument() {
		return new OOHTMLDocument();
	}

	@Override
	protected HTMLParser<OOTagElement, OOTokenizer> createParser(OOHTMLDocument document, IHTMLParserListener<OOTagElement, OOTokenizer> parserListener, LoadStream stream) {
		return OOHTMLDocument.createParser(document, parserListener, stream);
	}
}
