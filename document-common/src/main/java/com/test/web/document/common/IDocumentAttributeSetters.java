package com.test.web.document.common;

public interface IDocumentAttributeSetters<ELEMENT, ATTRIBUTE> {

	void setAttributeValue(ELEMENT element, int idx, String value);

	void setAttributeValue(ELEMENT element, ATTRIBUTE attribute, String value);

	// Adds if not present
	void setAttributeValue(ELEMENT element, String namespaceURI, String name, String value);

	void removeAttribute(ELEMENT element, String namespaceURI, String name);

}
