package com.test.web.layout;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.document._long.LongHTMLDocument;
import com.test.web.io._long.LongTokenizer;
import com.test.web.parse.common.ParserException;
import com.test.web.render.common.IRenderFactory;
import com.test.web.render.common.IRenderer;
import com.test.web.testdata.TestData;

import junit.framework.TestCase;

public class LayoutTest extends TestCase {
	
	public void testLayout() throws IOException, ParserException {
		
		final String html = TestData.wrap(
				"<div style='width:100px;height:300px'>\n" +
				"This is a test text that will wrap after 100 pixels" +
				"</div>\n"
		);
		
		final LongHTMLDocument doc = LongHTMLDocument.parseHTMLDocument(html);
		
		final IRenderFactory renderFactory = new IRenderFactory() {
			@Override
			public IRenderer createRenderer() {
				return new MockRenderer();
			}
		};
		
		final ViewPort viewPort = new ViewPort(800, 600);
		
		final ITextExtent textExtent = new MockTextExtent();
		
		final LayoutAlgorithm<Integer, LongTokenizer> layoutAgorithm = new LayoutAlgorithm<>(
				viewPort,
				textExtent,
				renderFactory);
		
		final CSSContext<Integer> cssContext = new CSSContext<>();

		doc.iterate(layoutAgorithm, cssContext);
		
	}
}
