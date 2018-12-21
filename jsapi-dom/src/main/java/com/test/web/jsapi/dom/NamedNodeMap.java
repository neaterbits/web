package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.jsengine.common.IJSObjectAsArray;

public final class NamedNodeMap<
        ELEMENT,
        ATTRIBUTE,
        DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
        DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>>

	extends DocumentAccess<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>

	implements IJSObjectAsArray {

	private final Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> ownerElement;

	NamedNodeMap(DOCUMENT_CONTEXT document, ELEMENT element, Element<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> ownerElement) {
		super(document, element);
		
		if (ownerElement == null) {
			throw new IllegalArgumentException("ownerElement == null");
		}
		
		this.ownerElement = ownerElement;
	}

	public final int getLength() {
		return getDocument().getNumAttributes(getElement());
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> getNamedItem(String name) {
		final ATTRIBUTE attribute = getDocument().getAttributeWithName(getElement(), name);
		
		return attribute == null ? null : new Attr<>(getDocument(), getElement(), attribute, ownerElement);
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> getNamedItemNS(String namespaceURI, String localName) {
		final ATTRIBUTE attribute = getDocument().getAttributeWithNameNS(getElement(), namespaceURI, localName);
		
		return attribute == null ? null : new Attr<>(getDocument(), getElement(), attribute, ownerElement);
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> setNamedItem(Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> attr) {
		final ATTRIBUTE existing = getDocument().getAttributeWithName(getElement(), attr.getName());
		
		throw new UnsupportedOperationException("TODO");
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> setNamedItemNS(Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> attr) {
		throw new UnsupportedOperationException("TODO");
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> setNamedItemNS(String namespaceURI, String localName) {
		final ATTRIBUTE attribute = getDocument().getAttributeWithNameNS(getElement(), namespaceURI, localName);
		
		return attribute == null ? null : new Attr<>(getDocument(), getElement(), attribute, ownerElement);
	}

	public final ATTRIBUTE removeNamedItem(String name) {
		return getDocument().removeAttribute(getElement(), name);
	}

	public final ATTRIBUTE removeNamedItem(String namespaceURI, String localName) {
		return getDocument().removeAttribute(getElement(), namespaceURI, localName);
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> item(int index) {
		
		final Attr<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT> attr;

		if (index < getArrayLength()) {
			final ELEMENT element = getElement();
			
			final ATTRIBUTE attribute = getDocument().getAttribute(element, index);
			
			attr = new Attr<>(getDocument(), element, attribute, ownerElement);
		}
		else {
			attr = null;
		}
		
		return attr;
	}

	
	@Override
	public final Object getArrayElem(int index) {
		return item(index);
	}

	@Override
	public final void setArrayElem(int index, Object value) {
		if (index < getArrayLength()) {
			getDocument().setAttributeValue(getElement(), index, (String)value);
		}
	}

	@Override
	public final long getArrayLength() {
		return getDocument().getNumAttributes(getElement());
	}
}
