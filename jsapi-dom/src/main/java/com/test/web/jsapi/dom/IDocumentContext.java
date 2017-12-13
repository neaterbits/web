package com.test.web.jsapi.dom;

import com.test.web.document.common.IDocument;

public interface IDocumentContext<ELEMENT> extends IDocument<ELEMENT> {

	// Keep a map of all event target with listeners
	// for lookup when bubbling events and for possibly serializing listeners
	void addEventTargetNowWithListeners(EventTarget<ELEMENT, ? extends IDocumentContext<ELEMENT>> target);
	
	void removeEventTargetWithNoMoreListeners(EventTarget<ELEMENT, ? extends IDocumentContext<ELEMENT>> target);
}
