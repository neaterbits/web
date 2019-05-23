package com.test.web.browser.common;

import java.io.IOException;

import com.neaterbits.util.io.loadstream.LoadStream;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.ParserException;
import com.test.web.css.common.CSSContext;
import com.test.web.css.oo.OOCSSBase;
import com.test.web.css.oo.OOCSSDocument;
import com.test.web.css.oo.OOCSSRule;
import com.test.web.document.html.oo.OOAttribute;
import com.test.web.document.html.oo.OOHTMLDocument;
import com.test.web.document.html.oo.OOTagElement;
import com.test.web.parse.css.CSSParser;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.parse.html.util.ParseHTML;
import com.test.web.render.common.IBufferRendererFactory;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;

public class OOBrowserDocumentLoader
		extends BaseBrowserDocumentLoader<OOTagElement, OOAttribute, OOCSSBase, OOHTMLDocument, OOCSSRule, OOCSSDocument>
		implements IBrowserDocumentLoader<OOTagElement, OOAttribute, OOCSSRule, OOHTMLDocument> {
	
	public OOBrowserDocumentLoader(IDelayedRendererFactory rendererFactory, IBufferRendererFactory bufferedRendererFactory, ITextExtent textExtent, DebugListeners debugListeners) {
		super(rendererFactory, bufferedRendererFactory, textExtent, debugListeners);
	}
	
	private OOCSSDocument parseCSS(CharInput charInput, Tokenizer tokenizer, CSSContext<OOCSSRule> cssContext) throws IOException, ParserException {
		
		final OOCSSDocument styleDocument = new OOCSSDocument();
		final CSSParser<OOCSSBase> cssParser = new CSSParser<>(charInput, tokenizer, styleDocument);

		// Just parse the CSS straight away
		cssParser.parseCSS();
		
		cssContext.addDocument(styleDocument);

		return styleDocument;
	}

	@Override
	public OOHTMLDocument fromHTML(String html, CSSContext<OOCSSRule> cssContext) throws ParserException {
		return ParseHTML.parseOOHTMLDocument(html, (charInput, tokenizer) -> parseCSS(charInput, tokenizer, cssContext));
	}

	@Override
	protected OOHTMLDocument createDocument() {
		return new OOHTMLDocument();
	}

	@Override
	protected HTMLParser<OOTagElement, OOCSSDocument, OOCSSBase> createParser(
			OOHTMLDocument document,
			IHTMLParserListener<OOTagElement> parserListener,
			LoadStream stream,
			CSSContext<OOCSSRule> cssContext) {
		
		return ParseHTML.createParser(document, parserListener, stream, (charInput, tokenizer) -> parseCSS(charInput, tokenizer, cssContext));
	}
}
