package com.test.web.browser.common;

import java.io.IOException;
import java.net.URL;

import com.test.web.css.common.CSSContext;
import com.test.web.document.html.common.IDocument;
import com.test.web.layout.common.page.PageLayout;
import com.test.web.parse.common.ParserException;
import com.test.web.render.common.IRenderer;

public interface IBrowserDocumentLoader<HTML_ELEMENT, HTML_ATTRIBUTE, CSS_ELEMENT, DOCUMENT extends IDocument<HTML_ELEMENT, HTML_ATTRIBUTE, DOCUMENT>> {

	/**
	 * Simply create a HTML document from a HTML String, HTML should not have links to external files since they will
	 * not be loaded.
	 * 
	 * @param html the document as a String
	 * 
	 * @return a parsed document
	 */
	
	DOCUMENT fromHTML(String html, CSSContext<CSS_ELEMENT> cssContext) throws ParserException;

	/**
	 *  TODO this would probably have to be async ?
	 * @return layout of page
	 */
	PageLayout<HTML_ELEMENT> layout(DOCUMENT document, int viewPortWidth, int viewPortHeight, IRenderer displayRenderer);
	
	
	/**
	 * Load URL asynchronously, displaying it using supplied display renderer
	 * 
	 * @param url
	 */
	
	void load(URL url, int viewPortWidth, int viewPortHeight, IRenderer displayRenderer) throws IOException, ParserException;
}
