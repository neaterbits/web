package com.test.web.jsapi.common.dom;

import com.test.web.document.html.common.IDocument;

public interface IDocumentContext<
            ELEMENT,
            ATTRIBUTE,
            DOCUMENT extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT>,
            DOCUMENT_CONTEXT extends IDocumentContext<ELEMENT, ATTRIBUTE, DOCUMENT, DOCUMENT_CONTEXT>> 
    extends IDocument<ELEMENT, ATTRIBUTE, DOCUMENT> {

	// Keep a map of all event target with listeners
	// for lookup when bubbling events and for possibly serializing listeners

	void addEventTargetNowWithListeners(EventTargetElement<ELEMENT> target);
	
	void removeEventTargetWithNoMoreListeners(EventTargetElement<ELEMENT> target);
	
	boolean dispatchEvent(IEvent event, EventTargetElement<ELEMENT> target);
	
}
