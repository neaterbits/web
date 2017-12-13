package com.test.web.jsapi.dom;

public abstract class DocumentAccess<ELEMENT, DOCUMENT extends IDocumentContext<ELEMENT>> {

	private DOCUMENT document;
	private ELEMENT element;

	public DocumentAccess() {

	}

	public DocumentAccess(DOCUMENT document, ELEMENT element) {
		this.document = document;
		this.element = element;
	}

	protected final DOCUMENT getDocument() {
		return document;
	}

	protected final ELEMENT getElement() {
		return element;
	}
}
