package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;

/**
 * Generic implementation of DOM.
 * 
 * Returns a JS wrapper object that links to the Document
 *
 */
public class JSAPI_DOM<ELEMENT, ATTRIBUTE> {

	private final IDocument<ELEMENT, ATTRIBUTE> document;

	public JSAPI_DOM(IDocument<ELEMENT,ATTRIBUTE> document) {
		
		if (document == null) {
			throw new IllegalArgumentException("document == null");
		}

		this.document = document;
	}
}
