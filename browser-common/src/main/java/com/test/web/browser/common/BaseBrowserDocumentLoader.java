package com.test.web.browser.common;

import java.io.IOException;
import java.net.URL;

import com.test.web.css.common.CSSContext;
import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.io.common.LoadStream;
import com.test.web.io.common.Tokenizer;
import com.test.web.layout.FontSettings;
import com.test.web.layout.IElementRenderLayout;
import com.test.web.layout.LayoutAlgorithm;
import com.test.web.layout.PageLayout;
import com.test.web.layout.PrintlnLayoutDebugListener;
import com.test.web.layout.ViewPort;
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
import com.test.web.render.common.IBufferRenderFactory;
import com.test.web.render.common.IRenderer;
import com.test.web.render.common.ITextExtent;
import com.test.web.render.html.HTMLRenderer;
import com.test.web.render.html.IRenderDebugListener;
import com.test.web.render.html.PrintlnRenderDebugListener;

public abstract class BaseBrowserDocumentLoader<ELEMENT, TOKENIZER extends Tokenizer, DOCUMENT extends IDocumentParserListener<ELEMENT, TOKENIZER>>
		implements IBrowserDocumentLoader<ELEMENT> {

	private final IBufferRenderFactory renderFactory;
	private final ITextExtent textExtent;
	
	protected abstract DOCUMENT createDocument();
	
	protected abstract HTMLParser<ELEMENT, TOKENIZER> createParser(DOCUMENT document, IHTMLParserListener<ELEMENT, TOKENIZER> parserListener, LoadStream stream);
	
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
		
		
		// We add HTMLRenderer here so that we render in the parsing pass, however renderer may just be
		// an async queue. The renderers are added to the computed layout for each element
		// since elements may have different z-index.
		
		// On could also envision rendering happening directly for z-index 0, this all depends on
		// what renderer is set for the element layout during layout algorithm
		
		final IRenderDebugListener renderDebugListener = new PrintlnRenderDebugListener(System.out);
		
		final HTMLRenderer<ELEMENT> htmlRenderer = new HTMLRenderer<>(renderDebugListener);
		
		final PageLayout<ELEMENT> pageLayout = layoutAgorithm.layout(document, viewPort, cssContext, htmlRenderer, displayRenderer);
		
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
			
			// HTML renderer that will render to display
			final HTMLElementListener<ELEMENT, IElementRenderLayout> renderListener = new HTMLRenderer<>(new PrintlnRenderDebugListener(System.out));
			
			// This parser listener will look for external dependencies and add those to the loadqueue,
			// it will also forward parser events to the DOM and to the layout algorithm
			final DependencyCollectingParserListener<ELEMENT, TOKENIZER> parserListener
				= new DependencyCollectingParserListener<>(
						document,
						loadQueueAndStream.getQueue(),
						viewPort,
						textExtent,
						displayRenderer,
						renderFactory,
						fontSettings,
						renderListener);
			
			// Load and parse the document throught the dependency collecting parser listener
			final HTMLParser<ELEMENT, TOKENIZER> htmlParser = createParser(document, parserListener, loadQueueAndStream.getStream());
	
			// Start parsing on this thread
			// TODO perhaps move to new thread since this is the UI thread?
			htmlParser.parseHTMLFile();
		}
		finally {
			loadQueueAndStream.getStream().close();
		}
	}
}