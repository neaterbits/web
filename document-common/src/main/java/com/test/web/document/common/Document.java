package com.test.web.document.common;

import java.util.List;

/*
 * Represents document object model for HTML
 * 
 */

public interface Document<ELEMENT> {

	ELEMENT getElementById(String id);

	String [] getClasses(ELEMENT element);
	
	List<ELEMENT> getElementsWithClass(String _class);

	// Access methods for various elements
	String getScriptType(ELEMENT element);
}
