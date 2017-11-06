package com.test.web.layout;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElement;
import com.test.web.io.common.Tokenizer;
import com.test.web.layout.FontSettings;
import com.test.web.layout.LayoutAlgorithm;
import com.test.web.layout.PageLayer;
import com.test.web.layout.PageLayout;
import com.test.web.layout.PrintlnLayoutDebugListener;
import com.test.web.layout.ViewPort;
import com.test.web.parse.common.ParserException;
import com.test.web.render.common.IRenderFactory;
import com.test.web.render.common.IRenderer;
import com.test.web.render.common.ITextExtent;
import com.test.web.testdata.TestData;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseLayoutTest<HTML_ELEMENT, TOKENIZER extends Tokenizer> extends TestCase {
	
	protected abstract Document<HTML_ELEMENT> parseDocument(String html) throws ParserException;
	
	public void testLayout() throws IOException, ParserException {
		
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:100px;height:300px'>\n" +
				"This is a test text that will wrap after 100 pixels" +
				"</div>\n"
		);
		
		final Document<HTML_ELEMENT> doc = parseDocument(html);
		
		final IRenderFactory renderFactory = new IRenderFactory() {
			@Override
			public IRenderer createRenderer() {
				return new MockRenderer();
			}
		};
		
		final ViewPort viewPort = new ViewPort(800, 600);
		
		final ITextExtent textExtent = new MockTextExtent();
		
		final LayoutAlgorithm<HTML_ELEMENT, TOKENIZER> layoutAgorithm = new LayoutAlgorithm<>(
				textExtent,
				renderFactory,
				new FontSettings(),
				new PrintlnLayoutDebugListener(System.out));

		final CSSContext<HTML_ELEMENT> cssContext = new CSSContext<>();

		final PageLayout<HTML_ELEMENT> pageLayout = layoutAgorithm.layout(doc, viewPort, cssContext, null);
		
		assertThat(pageLayout.getLayers().size()).isEqualTo(1);
	
		final PageLayer<HTML_ELEMENT> layer = pageLayout.getLayers().get(0);
		
		final HTML_ELEMENT div = doc.getElementById("element_id");
		assertThat(div).isNotNull();

		assertThat(layer.getOuterBounds(div).getLeft()).isEqualTo(0);
		assertThat(layer.getOuterBounds(div).getTop()).isEqualTo(0);
		assertThat(layer.getOuterBounds(div).getWidth()).isEqualTo(100);
		assertThat(layer.getOuterBounds(div).getHeight()).isEqualTo(300);

		assertThat(layer.getInnerBounds(div).getLeft()).isEqualTo(0);
		assertThat(layer.getInnerBounds(div).getTop()).isEqualTo(0);
		assertThat(layer.getInnerBounds(div).getWidth()).isEqualTo(100);
		assertThat(layer.getInnerBounds(div).getHeight()).isEqualTo(300);
			
		assertThat(layer.getMargins(div).getTop()).isEqualTo(0);
		assertThat(layer.getMargins(div).getRight()).isEqualTo(0);
		assertThat(layer.getMargins(div).getBottom()).isEqualTo(0);
		assertThat(layer.getMargins(div).getLeft()).isEqualTo(0);
		
		assertThat(layer.getPadding(div).getTop()).isEqualTo(0);
		assertThat(layer.getPadding(div).getRight()).isEqualTo(0);
		assertThat(layer.getPadding(div).getBottom()).isEqualTo(0);
		assertThat(layer.getPadding(div).getLeft()).isEqualTo(0);
		
	}
	
	public void testElementWithoutWidth() {
		assertThat(true).isFalse();
	}
}
