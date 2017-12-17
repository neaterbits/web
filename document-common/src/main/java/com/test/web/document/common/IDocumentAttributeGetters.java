package com.test.web.document.common;

public interface IDocumentAttributeGetters<ELEMENT> {

	int getNumAttributes(ELEMENT element);

	// -1 if not found
	int getIdxOfAttributeWithNameNS(ELEMENT element, String namespaceURI, String localName);

	int getIdxOfAttributeWithName(ELEMENT element, String name);

	String getAttributeName(ELEMENT element, int idx);
	
	String getAttributeNamespaceURI(ELEMENT element, int idx);

	String getAttributeLocalName(ELEMENT element, int idx);

	String getAttributePrefix(ELEMENT element, int idx);

	String getAttributeValue(ELEMENT element, int idx);
	
}
