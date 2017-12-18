package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;

public interface IDocumentContext<ELEMENT, ATTRIBUTE> extends IDocument<ELEMENT, ATTRIBUTE> {

	// Keep a map of all event target with listeners
	// for lookup when bubbling events and for possibly serializing listeners
	void addEventTargetNowWithListeners(EventTarget<ELEMENT, ATTRIBUTE, ? extends IDocumentContext<ELEMENT, ATTRIBUTE>> target);
	
	void removeEventTargetWithNoMoreListeners(EventTarget<ELEMENT, ATTRIBUTE, ? extends IDocumentContext<ELEMENT, ATTRIBUTE>> target);
}
