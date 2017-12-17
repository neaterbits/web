package com.test.web.document.common;

public interface IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>> {

	ELEMENT_TYPE getType(ELEMENT element);

	<PARAM> void iterate(IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, PARAM> listener, PARAM param);

	<PARAM> void iterateFrom(ELEMENT element, IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, PARAM> listener, PARAM param);


	boolean isLayoutElement(ELEMENT_TYPE elementType);
	
	
	// TODO these are HTML specific
	String getId(ELEMENT element);
	
	String getTag(ELEMENT element);
	
	String [] getClasses(ELEMENT element);

}
