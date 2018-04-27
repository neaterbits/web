package com.test.web.layout.algorithm;

import java.io.IOException;

import com.test.web.css.common.CSSContext;
import com.test.web.document.html.common.HTMLElement;
import com.test.web.document.html.common.IDocument;
import com.test.web.layout.algorithm.LayoutAlgorithm;
import com.test.web.layout.algorithm.PageLayer;
import com.test.web.layout.algorithm.PageLayout;
import com.test.web.layout.algorithm.PrintlnLayoutDebugListener;
import com.test.web.layout.common.HTMLLayoutContext;
import com.test.web.layout.common.ViewPort;
import com.test.web.parse.common.ParserException;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.render.html.FontSettings;
import com.test.web.render.queue.QueueRendererFactory;
import com.test.web.testdata.TestData;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseLayoutTest<HTML_ELEMENT, HTML_ATTRIBUTE> extends TestCase {
	
	protected abstract IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> parseDocument(String html) throws ParserException;
	
	public void testLayout() throws IOException, ParserException {
		
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:100px;height:300px'>\n" +
				"This is a test text that will wrap after 100 pixels" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);
		final HTML_ELEMENT div = doc.getElementById("element_id");

		assertThat(div).isNotNull();

		checkOuterBounds(layer, div, 0, 0, 100, 300);
		checkInnerBounds(layer, div, 0, 0, 100, 300);
		checkMargins(layer, div, 0, 0, 0, 0);
		checkPadding(layer, div, 0, 0, 0, 0);
	}

	public void testElementWithoutWidth() throws ParserException {
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:800px;height:300px'>\n" +
				" <span id=\"span1\" style=\"width:300px\">Span 1</span>" +
				" <span id=\"span2\">Span 2</span>" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);

		final HTML_ELEMENT span1 = doc.getElementById("span1");

		assertThat(span1).isNotNull();

		checkOuterBounds(layer, span1, 0, 0, 72, 12); // Even if width is specified for span, this is not used for inline elements
		checkInnerBounds(layer, span1, 0, 0, 72, 12); // Even if width is specified for span, this is not used for inline elements
		checkMargins(layer, span1, 0, 0, 0, 0);
		checkPadding(layer, span1, 0, 0, 0, 0);
		
		final HTML_ELEMENT span2 = doc.getElementById("span2");

		assertThat(span2).isNotNull();

		checkOuterBounds(layer, span2, 0, 0, 72, 12);
		checkInnerBounds(layer, span2, 0, 0, 72, 12);
		checkMargins(layer, span2, 0, 0, 0, 0);
		checkPadding(layer, span2, 0, 0, 0, 0);
	}
	
	public void testAutoMarginLeftDisplayInline() throws ParserException {
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:800px;height:300px'>\n" +
				" <span id=\"span1\" style=\"width:300px;margin-left:auto;margin-right:50px\">Span 1</span>" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);

		final HTML_ELEMENT span1 = doc.getElementById("span1");

		assertThat(span1).isNotNull();

		checkOuterBounds(layer, span1, 0, 0, 350, 600);
		checkInnerBounds(layer, span1, 0, 0, 300, 600);
		checkMargins(layer, span1, 0, 50, 0, 0);
		checkPadding(layer, span1, 0, 0, 0, 0);
	}

	
	public void testAutoMarginLeftDisplayBlock() throws ParserException {
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:800px;height:300px'>\n" +
				" <div id=\"div2\" style=\"width:300px;margin-left:auto;margin-right:50px\">Div 2</div>" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);

		final HTML_ELEMENT div2 = doc.getElementById("div2");

		assertThat(div2).isNotNull();

		checkOuterBounds(layer, div2, 0, 0, 800, 600);
		checkInnerBounds(layer, div2, 450, 0, 300, 600);
		checkMargins(layer, div2, 0, 50, 0, 450);
		checkPadding(layer, div2, 0, 0, 0, 0);
	}

	public void testAutoMarginRightDisplayInline() throws ParserException {
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:800px;height:300px'>\n" +
				" <span id=\"span1\" style=\"width:300px;margin-left:50px;margin-right:auto\">Span 1</span>" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);

		final HTML_ELEMENT span1 = doc.getElementById("span1");

		assertThat(span1).isNotNull();

		checkOuterBounds(layer, span1, 0, 0, 350, 600);
		checkInnerBounds(layer, span1, 50, 0, 300, 600);
		checkMargins(layer, span1, 0, 0, 0, 50);
		checkPadding(layer, span1, 0, 0, 0, 0);
	}

	
	public void testAutoMarginRightDisplayBlock() throws ParserException {
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:800px;height:300px'>\n" +
				" <div id=\"div2\" style=\"width:300px;margin-left:50px;margin-right:auto\">Div 2</div>" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);

		final HTML_ELEMENT div2 = doc.getElementById("div2");

		assertThat(div2).isNotNull();

		checkOuterBounds(layer, div2, 0, 0, 800, 600);
		checkInnerBounds(layer, div2, 50, 0, 300, 600);
		checkMargins(layer, div2, 0, 450, 0, 50);
		checkPadding(layer, div2, 0, 0, 0, 0);
	}

	public void testAutoMarginLeftRightDisplayInline() throws ParserException {
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:800px;height:300px'>\n" +
				" <span id=\"span1\" style=\"width:300px;margin-left:auto;margin-right:auto\">Span 1</span>" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);

		final HTML_ELEMENT span1 = doc.getElementById("span1");

		assertThat(span1).isNotNull();

		checkOuterBounds(layer, span1, 0, 0, 300, 600);
		checkInnerBounds(layer, span1, 0, 0, 300, 600);
		checkMargins(layer, span1, 0, 0, 0, 0);
		checkPadding(layer, span1, 0, 0, 0, 0);
	}

	
	public void testAutoMarginLeftRightDisplayBlock() throws ParserException {
		final String html = TestData.wrap(
				"<div id=\"element_id\" style='width:800px;height:300px'>\n" +
				" <div id=\"div2\" style=\"width:300px;margin-left:auto;margin-right:auto\">Div 2</div>" +
				"</div>\n"
		);
	
		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
		
		final PageLayer<HTML_ELEMENT> layer = layout(doc, 800, 600);

		final HTML_ELEMENT div2 = doc.getElementById("div2");

		assertThat(div2).isNotNull();

		checkOuterBounds(layer, div2, 0, 0, 800, 600);
		checkInnerBounds(layer, div2, 250, 0, 300, 600);
		checkMargins(layer, div2, 0, 250, 0, 250);
		checkPadding(layer, div2, 0, 0, 0, 0);
	}
	
	private PageLayer<HTML_ELEMENT> layout(String html, int viewPortWidth, int viewPortHeight) throws ParserException {

		final IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc = parseDocument(html);
	
		return layout(doc, viewPortWidth, viewPortHeight);
	}

	private PageLayer<HTML_ELEMENT> layout(IDocument<HTML_ELEMENT, HTML_ATTRIBUTE> doc, int viewPortWidth, int viewPortHeight) {
		
		final IDelayedRendererFactory renderFactory = new QueueRendererFactory(null);
		
		final ViewPort viewPort = new ViewPort(viewPortWidth, viewPortHeight);
		
		final ITextExtent textExtent = new MockTextExtent();
		
		final LayoutAlgorithm<HTML_ELEMENT, HTMLElement, IDocument<HTML_ELEMENT, HTML_ATTRIBUTE>> layoutAgorithm = new LayoutAlgorithm<>(
				textExtent,
				renderFactory,
				new FontSettings(),
				new PrintlnLayoutDebugListener<>(System.out));

		final CSSContext<HTML_ELEMENT> cssContext = new CSSContext<>();
		
		final PageLayout<HTML_ELEMENT> pageLayout = new PageLayout<>();
		
		 layoutAgorithm.layout(doc, viewPort, new HTMLLayoutContext<>(cssContext), pageLayout, null);
		
		assertThat(pageLayout.getLayers().size()).isEqualTo(1);
	
		final PageLayer<HTML_ELEMENT> layer = pageLayout.getLayers().get(0);
		
		return layer;
	}
	
	private void checkOuterBounds(PageLayer<HTML_ELEMENT> layer, HTML_ELEMENT element, int left, int top, int width, int height) {
		assertThat(layer.getOuterBounds(element).getLeft()).isEqualTo(left);
		assertThat(layer.getOuterBounds(element).getTop()).isEqualTo(top);
		assertThat(layer.getOuterBounds(element).getWidth()).isEqualTo(width);
		assertThat(layer.getOuterBounds(element).getHeight()).isEqualTo(height);
	}

	private void checkInnerBounds(PageLayer<HTML_ELEMENT> layer, HTML_ELEMENT element, int left, int top, int width, int height) {
		assertThat(layer.getInnerBounds(element).getLeft()).isEqualTo(left);
		assertThat(layer.getInnerBounds(element).getTop()).isEqualTo(top);
		assertThat(layer.getInnerBounds(element).getWidth()).isEqualTo(width);
		assertThat(layer.getInnerBounds(element).getHeight()).isEqualTo(height);
	}
	
	private void checkMargins(PageLayer<HTML_ELEMENT> layer, HTML_ELEMENT element, int top, int right, int bottom, int left) {
		assertThat(layer.getMargins(element).getTop()).isEqualTo(top);
		assertThat(layer.getMargins(element).getRight()).isEqualTo(right);
		assertThat(layer.getMargins(element).getBottom()).isEqualTo(bottom);
		assertThat(layer.getMargins(element).getLeft()).isEqualTo(left);
	}
	
	private void checkPadding(PageLayer<HTML_ELEMENT> layer, HTML_ELEMENT element, int top, int right, int bottom, int left) {
		assertThat(layer.getPadding(element).getTop()).isEqualTo(top);
		assertThat(layer.getPadding(element).getRight()).isEqualTo(right);
		assertThat(layer.getPadding(element).getBottom()).isEqualTo(bottom);
		assertThat(layer.getPadding(element).getLeft()).isEqualTo(left);
	}
}
