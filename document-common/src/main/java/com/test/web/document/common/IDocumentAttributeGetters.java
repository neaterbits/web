package com.test.web.document.common;

/**
 * Somewhat HTML independent access methods, eg. could be used for XML documents that also have element/attribute
 * 
 * @param <ELEMENT>
 * @param <ATTRIBUTE>
 */

public interface IDocumentAttributeGetters<ELEMENT, ATTRIBUTE> {

	int getNumAttributes(ELEMENT element);

	// -1 if not found
	ATTRIBUTE getAttributeWithNameNS(ELEMENT element, String namespaceURI, String localName);

	ATTRIBUTE getAttributeWithName(ELEMENT element, String name);
	
	ATTRIBUTE getAttribute(ELEMENT element, int idx);

	String getAttributeName(ELEMENT element, ATTRIBUTE attribute);
	
	String getAttributeNamespaceURI(ELEMENT element, ATTRIBUTE attribute);

	String getAttributeLocalName(ELEMENT element, ATTRIBUTE attribute);

	String getAttributePrefix(ELEMENT element, ATTRIBUTE attribute);

	String getAttributeValue(ELEMENT element, ATTRIBUTE attribute);
	
}
