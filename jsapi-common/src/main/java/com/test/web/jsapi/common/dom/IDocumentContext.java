package com.test.web.jsapi.common.dom;

import com.test.web.document.html.common.IDocument;

public interface IDocumentContext<ELEMENT, ATTRIBUTE> extends IDocument<ELEMENT, ATTRIBUTE> {

	// Keep a map of all event target with listeners
	// for lookup when bubbling events and for possibly serializing listeners
	void addEventTargetNowWithListeners(IEventTarget target);
	
	void removeEventTargetWithNoMoreListeners(IEventTarget target);
}
