package com.test.web.document.common;

public interface IDocumentAttributeSetters<ELEMENT> {

	void setAttributeValue(ELEMENT element, int idx, String value);

	// Adds if not present
	void setAttributeValue(ELEMENT element, String namespaceURI, String name, String value);

	void removeAttribute(ELEMENT element, String namespaceURI, String name);

}
