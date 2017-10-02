package com.test.web.document.common;

public interface HTMLElementListener<ELEMENT> {

	void onElement(Document<ELEMENT> document,  ELEMENT element);
	
}
