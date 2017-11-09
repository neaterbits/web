package com.test.web.browser.common;

import com.test.web.css.common.CSSContext;
import com.test.web.document.common.Document;
import com.test.web.io.common.Tokenizer;
import com.test.web.layout.FontSettings;
import com.test.web.layout.LayoutAlgorithm;
import com.test.web.layout.PageLayout;
import com.test.web.layout.PrintlnLayoutDebugListener;
import com.test.web.layout.ViewPort;
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.IRenderer;
import com.test.web.render.common.ITextExtent;
import com.test.web.render.html.HTMLRenderer;

public abstract class BaseBrowserDocumentLoader<ELEMENT, TOKENIZER extends Tokenizer>
		implements IBrowserDocumentLoader<ELEMENT> {

	private final IBufferRenderFactory renderFactory;
	private final ITextExtent textExtent;
	
	public BaseBrowserDocumentLoader(IBufferRenderFactory renderFactory, ITextExtent textExtent) {

		if (renderFactory == null) {
			throw new IllegalArgumentException("renderFactory == null");
		}
		
		if (textExtent == null) {
			throw new IllegalArgumentException("textExtent == null");
		}
	
		this.renderFactory = renderFactory;
		this.textExtent = textExtent;
	}

	@Override
	public final PageLayout<ELEMENT> layout(Document<ELEMENT> document, int viewPortWidth, int viewPortHeight, IRenderer displayRenderer) {

		final ViewPort viewPort = new ViewPort(viewPortWidth, viewPortHeight);
		
		final LayoutAlgorithm<ELEMENT, TOKENIZER> layoutAgorithm = new LayoutAlgorithm<>(
				textExtent,
				renderFactory,
				new FontSettings(),
				new PrintlnLayoutDebugListener(System.out));

		final CSSContext<ELEMENT> cssContext = new CSSContext<>();
		
		
		// We add HTMLRenerer here so that we render in the parsing pass, however renderer may just be
		// an async queue. The renderers are added to the computed layout for each element
		// since elements may have different z-index.
		
		// On could also envision rendering happening directly for z-index 0, this all depends on
		// what renderer is set for the element layout during layout algorithm
		
		final HTMLRenderer<ELEMENT> htmlRenderer = new HTMLRenderer<>();
		
		final PageLayout<ELEMENT> pageLayout = layoutAgorithm.layout(document, viewPort, cssContext, htmlRenderer, displayRenderer);
		
		// We should have loaded document now so sync to display. TODO should probably be done elsewhere, ie in loadqueue so that we sync as document loads
		displayRenderer.sync();
		
		return pageLayout;
	}
}
