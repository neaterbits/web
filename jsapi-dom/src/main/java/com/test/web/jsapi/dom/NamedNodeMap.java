package com.test.web.jsapi.dom;

import com.test.web.jsengine.common.IJSObjectAsArray;

public final class NamedNodeMap<ELEMENT, DOCUMENT extends IDocumentContext<ELEMENT>>
	extends DocumentAccess<ELEMENT, DOCUMENT>
	implements IJSObjectAsArray {

	private final Element<ELEMENT, DOCUMENT> ownerElement;

	NamedNodeMap(DOCUMENT document, ELEMENT element, Element<ELEMENT, DOCUMENT> ownerElement) {
		super(document, element);
		
		if (ownerElement == null) {
			throw new IllegalArgumentException("ownerElement == null");
		}
		
		this.ownerElement = ownerElement;
	}

	public final int getLength() {
		return getDocument().getNumAttributes(getElement());
	}

	public final Attr<ELEMENT, DOCUMENT> getNamedItem(String name) {
		final int index = getDocument().getIdxOfAttributeWithName(getElement(), name);
		
		return index < 0 ? null : new Attr<>(getDocument(), getElement(), index, ownerElement);
	}

	public final Attr<ELEMENT, DOCUMENT> getNamedItemNS(String namespaceURI, String localName) {
		final int index = getDocument().getIdxOfAttributeWithNameNS(getElement(), namespaceURI, localName);
		
		return index < 0 ? null : new Attr<>(getDocument(), getElement(), index, ownerElement);
	}

	public final Attr<ELEMENT, DOCUMENT> item(int index) {
		return index < getArrayLength()
				? new Attr<>(getDocument(), getElement(), index, ownerElement)
				: null;
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
