package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;

public class Element<
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

	extends Node<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> {

	
	public Element() {

	}

	public Element(DOCUMENT_CONTEXT document, ELEMENT element) {
		super(document, element);
	}

	public final NamedNodeMap<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> getAttributes() {
		return new NamedNodeMap<>(getDocument(), getElement(), this);
	}

	public final void setAttribute(String name, String value) {
		getDocument().setAttributeValue(getElement(), name, value);
	}
}
