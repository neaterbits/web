package com.test.web.jsapi.dom;

import com.test.web.document.common.IDocument;
import com.test.web.document.common.IDocumentNavigation;
import com.test.web.document.common.IDocumentUpdate;

public interface IDocumentContext<ELEMENT>
		extends IDocumentNavigation<ELEMENT>,
				     IDocumentUpdate<ELEMENT>,
				     IDocument<ELEMENT> {

	// Keep a map of all event target with listeners
	// for lookup when bubbling events and for possibly serializing listeners
	void addEventTargetNowWithListeners(EventTarget<ELEMENT, ? extends IDocumentContext<ELEMENT>> target);
	
	void removeEventTargetWithNoMoreListeners(EventTarget<ELEMENT, ? extends IDocumentContext<ELEMENT>> target);
}
