package com.test.web.jsapi.dom;

import com.test.web.jsapi.common.dom.IDocumentContext;

public abstract class DocumentAccess<ELEMENT, ATTRIBUTE, DOCUMENT extends IDocumentContext<ELEMENT, ATTRIBUTE>> {

	private DOCUMENT document;
	private ELEMENT element;

	public DocumentAccess() {

	}

	DocumentAccess(DOCUMENT document) {
		
		if (document == null) {
			throw new IllegalArgumentException("document == null");
		}

		this.document = document;
		this.element = null;
	}

	public DocumentAccess(DOCUMENT document, ELEMENT element) {
		
		if (document == null) {
			throw new IllegalArgumentException("document == null");
		}
		
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}

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
