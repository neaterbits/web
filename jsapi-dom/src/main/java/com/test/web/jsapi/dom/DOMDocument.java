package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;

public class DOMDocument<
        ELEMENT,
        ATTRIBUTE,
        DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
        DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>
		extends Node<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> {

	public DOMDocument() {
		super();
	}
	
	DOMDocument(DOCUMENT_CONTEXT document) {
		super(document);
	}

	public DOMDocument(DOCUMENT_CONTEXT document, ELEMENT element) {
		super(document, element);
	}
	
	public Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> getElementById(String id) {
		final ELEMENT element = getDocument().getElementById(id);
		
		// TODO return the correct instance based on type
		return element == null ? null : new Element<>(getDocument(), element);
	}
}
