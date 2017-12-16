package com.test.web.jsapi.dom;

public final class Attr<ELEMENT, DOCUMENT extends IDocumentContext<ELEMENT>>
	extends DocumentAccess<ELEMENT, DOCUMENT> {
	
	private final int idx;
	private final Element<ELEMENT, DOCUMENT> ownerElement;
	
	Attr(DOCUMENT document, ELEMENT element, int idx, Element<ELEMENT, DOCUMENT> ownerElement) {
		super(document, element);

		this.idx = idx;
		this.ownerElement = ownerElement;
	}

	public String getName() {
		return getDocument().getAttributeName(getElement(), idx);
	}

	public String getNamespaceURI() {
		return getDocument().getAttributeNamespaceURI(getElement(), idx);
	}

	public String getLocalName() {
		return getDocument().getAttributeLocalName(getElement(), idx);
	}

	public String getPrefix() {
		return getDocument().getAttributePrefix(getElement(), idx);
	}

	public String getValue() {
		return getDocument().getAttributeValue(getElement(), idx);
	}

	public void setValue(String value) {
		getDocument().setAttributeValue(getElement(), idx, value);
	}

	public Element<ELEMENT, DOCUMENT> getOwnerElement() {
		return ownerElement;
	}
}
