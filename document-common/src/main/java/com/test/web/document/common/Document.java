package com.test.web.document.common;

/*
 * Represents document object model for HTML
 * 
 */

public interface Document<ELEMENT> {

	ELEMENT getElementById(String id);

	// Access methods for various elements
	String getScriptType(ELEMENT element);
}
