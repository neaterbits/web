package com.test.web.document.common;

public interface IDocumentAttributeSetters<ELEMENT, ATTRIBUTE> {

	ATTRIBUTE setAttributeValue(ELEMENT element, int idx, String value);

	ATTRIBUTE setAttributeValue(ELEMENT element, ATTRIBUTE attribute, String value);

	// Adds if not present
	ATTRIBUTE setAttributeValue(ELEMENT element, String name, String value);

	ATTRIBUTE setAttributeValue(ELEMENT element, String namespaceURI, String name, String value);

	ATTRIBUTE removeAttribute(ELEMENT element, String name);

	ATTRIBUTE removeAttribute(ELEMENT element, String namespaceURI, String localName);

}
