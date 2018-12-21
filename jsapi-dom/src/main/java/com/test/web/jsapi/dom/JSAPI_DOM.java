package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;

/**
 * Generic implementation of DOM.
 * 
 * Returns a JS wrapper object that links to the Document
 *
 */
public class JSAPI_DOM<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>> {

	private final DOCUMENT document;

	public JSAPI_DOM(DOCUMENT document) {
		
		if (document == null) {
			throw new IllegalArgumentException("document == null");
		}

		this.document = document;
	}
}
