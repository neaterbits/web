package com.test.web.browser.common;

import java.io.IOException;

import com.test.web.css._long.LongCSSDocument;
import com.test.web.css.common.CSSContext;
import com.test.web.document._long.LongHTMLDocument;
import com.test.web.document.html.common.IDocument;
import com.test.web.io._long.LongTokenizer;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.LoadStream;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.render.common.IBufferRendererFactory;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;

public class LongBrowserDocumentLoader
		extends BaseBrowserDocumentLoader<Integer, LongTokenizer, LongHTMLDocument, Integer, LongCSSDocument>
		implements IBrowserDocumentLoader<Integer, Integer> {

	public LongBrowserDocumentLoader(IDelayedRendererFactory rendererFactory,  IBufferRendererFactory bufferRendererFactory, ITextExtent textExtent, DebugListeners debugListeners) {
		super(rendererFactory, bufferRendererFactory, textExtent, debugListeners);
	}

	private LongCSSDocument parseCSS(CharInput charInput, CSSContext<Integer> cssContext) throws IOException, ParserException {
		
		final LongCSSDocument styleDocument = new LongCSSDocument();
		final CSSParser<LongTokenizer, Void> cssParser = new CSSParser<>(charInput, styleDocument);

		// Just parse the CSS straight away
		cssParser.parseCSS();

		cssContext.addDocument(styleDocument);

		return styleDocument;
	}

	@Override
	public IDocument<Integer> fromHTML(String html, CSSContext<Integer> cssContext) throws ParserException {
		return LongHTMLDocument.parseHTMLDocument(html, charInput -> parseCSS(charInput, cssContext));
	}

	@Override
	protected LongHTMLDocument createDocument() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	protected HTMLParser<Integer, LongTokenizer, LongCSSDocument> createParser(LongHTMLDocument document,
			IHTMLParserListener<Integer, LongTokenizer> parserListener, LoadStream stream, CSSContext<Integer> cssContext) {
		throw new UnsupportedOperationException("TODO");
	}
}
