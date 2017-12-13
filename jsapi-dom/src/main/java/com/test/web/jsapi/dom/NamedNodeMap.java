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

	public int length() {
		return getDocument().getNumAttributes(getElement());
	}
	
	@Override
	public Object getArrayElem(int index) {
		return index < getArrayLength()
			? new Attr<>(getDocument(), getElement(), index, ownerElement)
			: null;
	}

	@Override
	public void setArrayElem(int index, Object value) {
		if (index < getArrayLength()) {
			getDocument().setAttributeValue(getElement(), index, (String)value);
		}
	}

	@Override
	public long getArrayLength() {
		return getDocument().getNumAttributes(getElement());
	}
}
