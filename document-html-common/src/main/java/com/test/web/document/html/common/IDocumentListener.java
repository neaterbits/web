package com.test.web.document.html.common;

// For updating UI based on changes applied from JS
public interface IDocumentListener<ELEMENT> {

	void onAttributeUpdated(ELEMENT element, HTMLAttribute attribute);
	
}