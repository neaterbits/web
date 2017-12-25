package com.test.web.jsapi.dom;

import com.test.web.jsapi.common.dom.IDocumentContext;

public class Element<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>>
	extends Node<ELEMENT, ATTRIBUTE, DOCUMENT>{

	
	public Element() {

	}

	public Element(DOCUMENT document, ELEMENT element) {
		super(document, element);
	}

	public final NamedNodeMap<ELEMENT, ATTRIBUTE, DOCUMENT> getAttributes() {
		return new NamedNodeMap<>(getDocument(), getElement(), this);
	}

	public final void setAttribute(String name, String value) {
		getDocument().setAttributeValue(getElement(), name, value);
	}
}
