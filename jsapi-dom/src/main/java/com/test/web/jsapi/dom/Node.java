package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;

public abstract class Node<
        ELEMENT,
        ATTRIBUTE,
        DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
        DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

	extends EventTarget<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> {

	protected Node() {
	    
	}

	protected Node(DOCUMENT_CONTEXT document) {
		super(document);
	}

	protected Node(DOCUMENT_CONTEXT document, ELEMENT element) {
		super(document, element);
	}
}
