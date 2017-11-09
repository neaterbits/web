package com.test.web.browser.common;

import com.test.web.document.common.Document;
import com.test.web.layout.PageLayout;
import com.test.web.parse.common.ParserException;
import com.test.web.render.common.IRenderer;

public interface IBrowserDocumentLoader<ELEMENT> {

	/**
	 * Simply create a HTML document from a HTML String, HTML should not have links to external files since they will
	 * not be loaded.
	 * 
	 * @param html the document as a String
	 * 
	 * @return a parsed document
	 */
	
	Document<ELEMENT> fromHTML(String html) throws ParserException;

	/**
	 *  TODO this would probably have to be async ?
	 * @return layout of page
	 */
	PageLayout<ELEMENT> layout(Document<ELEMENT> document, int viewPortWidth, int viewPortHeight, IRenderer displayRenderer);
}
