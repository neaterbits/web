package com.test.web.loadqueue.html;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.test.web.document.html._long.LongHTMLDocument;
import com.test.web.io._long.LongTokenizer;
import com.test.web.io._long.StringBuffers;
import com.test.web.layout.FontSettings;
import com.test.web.layout.PageLayout;
import com.test.web.loadqueue.common.LoadQueue;
import com.test.web.loadqueue.common.LoadQueueAndStream;
import com.test.web.loadqueue.common.IStreamFactory;
import com.test.web.loadqueue.common.scheduler.ThreadedLoadScheduler;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.html.HTMLParser;
import com.test.web.testdata.TestData;

import junit.framework.TestCase;


public class HTMLLoadQueueTest extends TestCase {

	// Test load queue to see that works with blocking
	
	public void testWihLoadQueue() throws IOException, ParserException {

		final String html = TestData.HTML;
		final String css = TestData.CSS;

		final ThreadedLoadScheduler loadScheduler = new ThreadedLoadScheduler(new IStreamFactory() {
			
			@Override
			public InputStream getStream(String url) throws IOException {
				final InputStream ret;
				if (url.equals("webpage.html")) {
					ret = new ByteArrayInputStream(html.getBytes());
				}
				else if (url.equals("test_stylesheet.css")) {
					ret = new ByteArrayInputStream(css.getBytes());
				}
				else {
					throw new IllegalArgumentException("Unknown URL " + url);
				}

				return ret;
			}
		});
		
		final LoadQueueAndStream loadQueue = LoadQueue.createLoadQueue("webpage.html", loadScheduler);
		
		final StringBuffers buffers = new StringBuffers(loadQueue.getStream());

		final LongHTMLDocument doc = new LongHTMLDocument(buffers);
		
		final DependencyCollectingParserListener<Integer, LongTokenizer> parserListener
			= new DependencyCollectingParserListener<>(null, doc, loadQueue.getQueue(), null, null, null, new FontSettings(), new PageLayout<>(), null, null);
		
		final HTMLParser<Integer, LongTokenizer, Void> parser = new HTMLParser<>(
				buffers,
				buffers,
				parserListener,
				doc.getStyleParserListener(),
				charInput -> null);
		
		parser.parseHTMLFile();
	}
}
