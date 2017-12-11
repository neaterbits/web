package com.test.web.browser.common;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.css.oo.OOCSSDocument;
import com.test.web.css.oo.OOCSSElement;
import com.test.web.document.common.Document;
import com.test.web.document.oo.OOHTMLDocument;
import com.test.web.document.oo.OOTagElement;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.LoadStream;
import com.test.web.io.oo.OOTokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.css.CSSParser;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.render.common.IBufferRendererFactory;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;

public class OOBrowserDocumentLoader
		extends BaseBrowserDocumentLoader<OOTagElement, OOTokenizer, OOHTMLDocument, OOCSSElement, OOCSSDocument>
		implements IBrowserDocumentLoader<OOTagElement, OOCSSElement> {
	
	public OOBrowserDocumentLoader(IDelayedRendererFactory rendererFactory, IBufferRendererFactory bufferedRendererFactory, ITextExtent textExtent, DebugListeners debugListeners) {
		super(rendererFactory, bufferedRendererFactory, textExtent, debugListeners);
	}
	
	private OOCSSDocument parseCSS(CharInput charInput, CSSContext<OOCSSElement> cssContext) throws IOException, ParserException {
		
		final OOCSSDocument styleDocument = new OOCSSDocument();
		final CSSParser<OOTokenizer, Void> cssParser = new CSSParser<>(charInput, styleDocument);

		// Just parse the CSS straight away
		cssParser.parseCSS();
		
		cssContext.addDocument(styleDocument);

		return styleDocument;
	}

	@Override
	public Document<OOTagElement> fromHTML(String html, CSSContext<OOCSSElement> cssContext) throws ParserException {
		return OOHTMLDocument.parseHTMLDocument(html, charInput -> parseCSS(charInput, cssContext));
	}

	@Override
	protected OOHTMLDocument createDocument() {
		return new OOHTMLDocument();
	}

	@Override
	protected HTMLParser<OOTagElement, OOTokenizer, OOCSSDocument> createParser(
			OOHTMLDocument document,
			IHTMLParserListener<OOTagElement, OOTokenizer> parserListener,
			LoadStream stream,
			CSSContext<OOCSSElement> cssContext) {
		
		return OOHTMLDocument.createParser(document, parserListener, stream, charInput -> parseCSS(charInput, cssContext));
	}
}
