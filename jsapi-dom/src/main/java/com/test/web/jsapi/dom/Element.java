package com.test.web.jsapi.dom;

public class Element<ELEMENT, DOCUMENT extends IDocumentContext<ELEMENT>>
	extends Node<ELEMENT, DOCUMENT>{

	
	public Element() {

	}

	public Element(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}

	public final NamedNodeMap<ELEMENT, DOCUMENT> getAttributes() {
		return new NamedNodeMap<>(getDocument(), getElement(), this);
	}
}
