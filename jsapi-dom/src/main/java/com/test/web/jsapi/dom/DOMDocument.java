package com.test.web.jsapi.dom;

public class DOMDocument<ELEMENT, DOCUMENT extends IDocumentContext<ELEMENT>>
		extends Node<ELEMENT, DOCUMENT> {

	public DOMDocument() {
		super();
	}

	public DOMDocument(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}
}
