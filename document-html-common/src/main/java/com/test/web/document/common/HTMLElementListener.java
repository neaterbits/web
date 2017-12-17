package com.test.web.document.common;

public interface HTMLElementListener<ELEMENT, PARAM> {

	void onElementStart(IDocument<ELEMENT> document,  ELEMENT element, PARAM param);

	void onElementEnd(IDocument<ELEMENT> document,  ELEMENT element, PARAM param);

	void onText(IDocument<ELEMENT> document,  ELEMENT element, String text, PARAM param);
}
