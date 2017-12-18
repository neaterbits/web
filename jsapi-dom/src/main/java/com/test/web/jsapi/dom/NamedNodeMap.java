package com.test.web.jsapi.dom;

import com.test.web.jsengine.common.IJSObjectAsArray;

public final class NamedNodeMap<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>>
	extends DocumentAccess<ELEMENT, ATTRIBUTE, DOCUMENT>
	implements IJSObjectAsArray {

	private final Element<ELEMENT, ATTRIBUTE, DOCUMENT> ownerElement;

	NamedNodeMap(DOCUMENT document, ELEMENT element, Element<ELEMENT, ATTRIBUTE, DOCUMENT> ownerElement) {
		super(document, element);
		
		if (ownerElement == null) {
			throw new IllegalArgumentException("ownerElement == null");
		}
		
		this.ownerElement = ownerElement;
	}

	public final int getLength() {
		return getDocument().getNumAttributes(getElement());
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT> getNamedItem(String name) {
		final ATTRIBUTE attribute = getDocument().getIdxOfAttributeWithName(getElement(), name);
		
		return attribute == null ? null : new Attr<>(getDocument(), getElement(), attribute, ownerElement);
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT> getNamedItemNS(String namespaceURI, String localName) {
		final ATTRIBUTE attribute = getDocument().getIdxOfAttributeWithNameNS(getElement(), namespaceURI, localName);
		
		return attribute == null ? null : new Attr<>(getDocument(), getElement(), attribute, ownerElement);
	}

	public final Attr<ELEMENT, ATTRIBUTE, DOCUMENT> item(int index) {
		
		final Attr<ELEMENT, ATTRIBUTE, DOCUMENT> attr;

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
