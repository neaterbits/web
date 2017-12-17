package com.test.web.document.html.oo;

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
