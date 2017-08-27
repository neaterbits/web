package com.test.web.document.common;

import java.util.List;

/*
 * Represents document object model for HTML
 * 
 */

public interface Document<ELEMENT> {

	ELEMENT getElementById(String id);
	
	String getId(ELEMENT element);

	String [] getClasses(ELEMENT element);
	
	List<ELEMENT> getElementsWithClass(String _class);

	int getNumElements(ELEMENT element);

	List<ELEMENT> getElementsWithType(HTMLElement type);

	// Access methods for various elements
	String getScriptType(ELEMENT element);

	String getLinkRel(ELEMENT element);
	String getLinkType(ELEMENT element);
	String getLinkHRef(ELEMENT element);
}
