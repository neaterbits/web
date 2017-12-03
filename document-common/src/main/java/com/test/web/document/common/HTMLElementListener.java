package com.test.web.document.common;

public interface HTMLElementListener<ELEMENT, PARAM> {

	void onElementStart(Document<ELEMENT> document,  ELEMENT element, PARAM param);

	void onElementEnd(Document<ELEMENT> document,  ELEMENT element, PARAM param);

	void onText(Document<ELEMENT> document,  ELEMENT element, String text, PARAM param);
}
