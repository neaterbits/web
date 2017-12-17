package com.test.web.browser.common;

import java.io.IOException;
import java.net.URL;

import com.test.web.css.common.CSSContext;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.HTMLElementListener;
import com.test.web.document.html.common.IDocument;
import com.test.web.io.common.LoadStream;
import com.test.web.io.common.Tokenizer;
import com.test.web.layout.algorithm.LayoutAlgorithm;
import com.test.web.layout.algorithm.PageLayout;
import com.test.web.layout.common.HTMLLayoutContext;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.ViewPort;
import com.test.web.loadqueue.common.LoadQueue;
import com.test.web.loadqueue.common.LoadQueueAndStream;
import com.test.web.loadqueue.common.LoadScheduler;
import com.test.web.loadqueue.common.scheduler.ThreadedLoadScheduler;
import com.test.web.loadqueue.common.scheduler.URLStreamFactory;
import com.test.web.loadqueue.html.DependencyCollectingParserListener;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;
import com.test.web.parse.html.IDocumentParserListener;
import com.test.web.parse.html.IHTMLParserListener;
import com.test.web.render.common.IBufferRendererFactory;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.IRenderer;
import com.test.web.render.common.ITextExtent;
import com.test.web.render.html.DisplayRenderer;
import com.test.web.render.html.FontSettings;
import com.test.web.render.html.HTMLRenderer;
import com.test.web.render.html.IDToOffsetList;
import com.test.web.render.html.IRenderDebugListener;
import com.test.web.render.html.PrintlnRenderDebugListener;

public abstract class BaseBrowserDocumentLoader<HTML_ELEMENT, TOKENIZER extends Tokenizer, DOCUMENT extends IDocumentParserListener<HTML_ELEMENT, TOKENIZER>, CSS_ELEMENT, STYLE_DOCUMENT>
		implements IBrowserDocumentLoader<HTML_ELEMENT, CSS_ELEMENT> {

	private final IDelayedRendererFactory renderFactory;
	private final IBufferRendererFactory bufferRenderFactory;
	private final ITextExtent textExtent;

	private final DebugListeners<HTMLElement> debugListeners;

	protected abstract DOCUMENT createDocument();
	
	protected abstract HTMLParser<HTML_ELEMENT, TOKENIZER, STYLE_DOCUMENT> createParser(
			DOCUMENT document,
			IHTMLParserListener<HTML_ELEMENT, TOKENIZER> parserListener,
			LoadStream stream,
			CSSContext<CSS_ELEMENT>cssContext);
	
	public BaseBrowserDocumentLoader(IDelayedRendererFactory rendererFactory, IBufferRendererFactory bufferRendererFactory, ITextExtent textExtent, DebugListeners<HTMLElement> debugListeners) {

		if (rendererFactory == null) {
			throw new IllegalArgumentException("rendererFactory == null");
		}
		
		if (textExtent == null) {
			throw new IllegalArgumentException("textExtent == null");
		}
	
		this.renderFactory = rendererFactory;
		this.bufferRenderFactory = bufferRendererFactory;
		this.textExtent = textExtent;

		this.debugListeners = debugListeners;
	}

	@Override
	public final PageLayout<HTML_ELEMENT> layout(IDocument<HTML_ELEMENT> document, int viewPortWidth, int viewPortHeight, IRenderer displayRenderer) {

		final ViewPort viewPort = new ViewPort(viewPortWidth, viewPortHeight);
		
		final LayoutAlgorithm<HTML_ELEMENT, HTMLElement, IDocument<HTML_ELEMENT>, TOKENIZER> layoutAgorithm
			= new LayoutAlgorithm<>(
				textExtent,
				renderFactory,
				new FontSettings(),
				debugListeners.getLayoutListener());

		final CSSContext<HTML_ELEMENT> cssContext = new CSSContext<>();
		
		
		// We add HTMLRenderer here so that we render in the parsing pass, however renderer may just be
		// an async queue. The renderers are added to the computed layout for each element
		// since elements may have different z-index.
		
		// On could also envision rendering happening directly for z-index 0, this all depends on
		// what renderer is set for the element layout during layout algorithm
		
		final IRenderDebugListener renderDebugListener = new PrintlnRenderDebugListener(System.out);

		final PageLayout<HTML_ELEMENT> pageLayout = new PageLayout<>();
		
		final DisplayRenderer<HTML_ELEMENT> renderer = new DisplayRenderer<>(
				viewPort,
				pageLayout,
				displayRenderer,
				textExtent,
				new IDToOffsetList(), // TODO cache between invocations?
				debugListeners.getDisplayRendererListener());
		
		final HTMLRenderer<HTML_ELEMENT> htmlRenderer = new HTMLRenderer<>(renderDebugListener, renderer);
		
		final HTMLLayoutContext<HTML_ELEMENT> layoutContext = new HTMLLayoutContext<>(cssContext);
		
		layoutAgorithm.layout(document, viewPort, layoutContext, pageLayout, htmlRenderer);

		// We should have loaded document now so sync to display. TODO should probably be done elsewhere, ie in loadqueue so that we sync as document loads
		displayRenderer.sync();
		
		return pageLayout;
	}

	@Override
	public void load(URL url, int viewPortWidth, int viewPortHeight, IRenderer displayRenderer) throws IOException, ParserException {

		// Load document using loadqueue so that can backround-load items
		
		final LoadScheduler loadScheduler = new ThreadedLoadScheduler(new URLStreamFactory());
		
		final LoadQueueAndStream loadQueueAndStream = LoadQueue.createLoadQueue(url.toString(), loadScheduler);

		try {
			// TODO send in as parameter?
			final FontSettings fontSettings = new FontSettings();
			final ViewPort viewPort = new ViewPort(viewPortWidth, viewPortHeight);
	
			// Delegate i the document, ie the DOM
			final DOCUMENT document = createDocument();
			
			final PageLayout<HTML_ELEMENT> pageLayout = new PageLayout<>();
			
			final DisplayRenderer<HTML_ELEMENT> renderer = new DisplayRenderer<>(
					viewPort,
					pageLayout,
					displayRenderer,
					textExtent,
					new IDToOffsetList(), // TODO cache
					debugListeners.getDisplayRendererListener());
			
			// HTML renderer that will render to display
			final HTMLElementListener<HTML_ELEMENT, IElementRenderLayout> renderListener = new HTMLRenderer<>(new PrintlnRenderDebugListener(System.out), renderer);
			
			// This parser listener will look for external dependencies and add those to the loadqueue,
			// it will also forward parser events to the DOM and to the layout algorithm
			final DependencyCollectingParserListener<HTML_ELEMENT, TOKENIZER> parserListener
				= new DependencyCollectingParserListener<>(
						url, // TODO handle redirects eg to index.html
						document,
						loadQueueAndStream.getQueue(),
						viewPort,
						textExtent,
						renderFactory,
						fontSettings,
						pageLayout,
						renderListener,
						debugListeners.getLayoutListener());
			
			// All CSS definitions collected here
			final CSSContext<CSS_ELEMENT> cssContext = new CSSContext<>();
			
			// Load and parse the document throught the dependency collecting parser listener
			final HTMLParser<HTML_ELEMENT, TOKENIZER, STYLE_DOCUMENT> htmlParser = createParser(document, parserListener, loadQueueAndStream.getStream(), cssContext);
	
			// Start parsing on this thread
			// TODO perhaps move to new thread since this is the UI thread?
			htmlParser.parseHTMLFile();
		}
		finally {
			loadQueueAndStream.getStream().close();
		}
	}
}