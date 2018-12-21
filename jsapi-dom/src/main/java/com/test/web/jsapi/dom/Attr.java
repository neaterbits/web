package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;

public final class Attr<
    ELEMENT,
    ATTRIBUTE,
    DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
    DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>


	extends DocumentAccess<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> {
	
	private final ATTRIBUTE attribute;
	private final Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> ownerElement;
	
	Attr(DOCUMENT_CONTEXT document, ELEMENT element, ATTRIBUTE attribute, Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> ownerElement) {
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

	public Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> getOwnerElement() {
		return ownerElement;
	}
}
