package com.test.web.jsapi.dom;

public abstract class Node<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>>
	extends EventTarget<ELEMENT, ATTRIBUTE, DOCUMENT>{

	Node() {
	}

	Node(DOCUMENT document) {
		super(document);
	}

	Node(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}
}
