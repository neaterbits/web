package com.test.web.document.oo;

abstract class OODocumentElement {

	OODocumentElement parent;
	OODocumentElement last;
	OODocumentElement next;
	
	
	OODocumentElement() {
		
	}

	final boolean isContainer() {
		return this instanceof OOContainerElement;
	}
	
	final boolean isHTML() {
		return this instanceof OOTagElement;
	}
}
