package com.test.web.jsapi.dom;

import com.test.web.jsapi.common.dom.IDocumentContext;

public abstract class Node<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>>
	extends EventTarget<ELEMENT, ATTRIBUTE, DOCUMENT>{

	protected Node() {
	}

	protected Node(DOCUMENT document) {
		super(document);
	}

	protected Node(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}
}
