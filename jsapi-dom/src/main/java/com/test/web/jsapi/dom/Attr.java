package com.test.web.jsapi.dom;

import com.test.web.jsapi.common.dom.IDocumentContext;

public final class Attr<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>>
	extends DocumentAccess<ELEMENT, ATTRIBUTE, DOCUMENT> {
	
	private final ATTRIBUTE attribute;
	private final Element<ELEMENT, ATTRIBUTE, DOCUMENT> ownerElement;
	
	Attr(DOCUMENT document, ELEMENT element, ATTRIBUTE attribute, Element<ELEMENT, ATTRIBUTE, DOCUMENT> ownerElement) {
		super(document, element);

		this.attribute = attribute;
		this.ownerElement = ownerElement;
	}

	public String getName() {
		return getDocument().getAttributeName(getElement(), attribute);
	}

	public String getNamespaceURI() {
		return getDocument().getAttributeNamespaceURI(getElement(), attribute);
	}

	public String getLocalName() {
		return getDocument().getAttributeLocalName(getElement(), attribute);
	}

	public String getPrefix() {
		return getDocument().getAttributePrefix(getElement(), attribute);
	}

	public String getValue() {
		return getDocument().getAttributeValue(getElement(), attribute);
	}

	public void setValue(String value) {
		getDocument().setAttributeValue(getElement(), attribute, value);
	}

	public Element<ELEMENT, ATTRIBUTE, DOCUMENT> getOwnerElement() {
		return ownerElement;
	}
}
