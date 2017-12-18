package com.test.web.jsapi.dom;

public class DOMDocument<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>>
		extends Node<ELEMENT, ATTRIBUTE, DOCUMENT> {

	public DOMDocument() {
		super();
	}
	
	DOMDocument(DOCUMENT document) {
		super(document);
	}

	public DOMDocument(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}
	
	public Element<ELEMENT, ATTRIBUTE, DOCUMENT>getElementById(String id) {
		final ELEMENT element = getDocument().getElementById(id);
		
		// TODO return the correct instance based on type
		return element == null ? null : new Element<>(getDocument(), element);
	}
}
