package com.test.web.document.common;

public interface IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT
		extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>, PARAM> {

	void onElementStart(DOCUMENT document,  ELEMENT element, PARAM param);

	void onElementEnd(DOCUMENT document,  ELEMENT element, PARAM param);

	void onText(DOCUMENT document,  ELEMENT element, String text, PARAM param);
}
