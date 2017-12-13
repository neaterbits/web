package com.test.web.jsapi.dom;

import com.test.web.document.common.Document;

/**
 * Generic implementation of DOM.
 * 
 * Returns a JS wrapper object that links to the Document
 *
 */
public class JSAPI_DOM<ELEMENT> {

	private final Document<ELEMENT> document;

	public JSAPI_DOM(Document<ELEMENT> document) {
		
		if (document == null) {
			throw new IllegalArgumentException("document == null");
		}

		this.document = document;
	}
}
