package com.test.web.document.common;

public interface IDocumentAttributeGetters<ELEMENT, ATTRIBUTE> {

	int getNumAttributes(ELEMENT element);

	// -1 if not found
	ATTRIBUTE getIdxOfAttributeWithNameNS(ELEMENT element, String namespaceURI, String localName);

	ATTRIBUTE getIdxOfAttributeWithName(ELEMENT element, String name);
	
	ATTRIBUTE getAttribute(ELEMENT element, int idx);

	String getAttributeName(ELEMENT element, ATTRIBUTE attribute);
	
	String getAttributeNamespaceURI(ELEMENT element, ATTRIBUTE attribute);

	String getAttributeLocalName(ELEMENT element, ATTRIBUTE attribute);

	String getAttributePrefix(ELEMENT element, ATTRIBUTE attribute);

	String getAttributeValue(ELEMENT element, ATTRIBUTE attribute);
	
}
