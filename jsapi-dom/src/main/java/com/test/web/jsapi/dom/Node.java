package com.test.web.jsapi.dom;

public abstract class Node<ELEMENT, DOCUMENT extends IDocumentContext<ELEMENT>>
	extends EventTarget<ELEMENT, DOCUMENT>{

	Node() {
	}

	Node(DOCUMENT document) {
		super(document);
	}

	Node(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}
}
