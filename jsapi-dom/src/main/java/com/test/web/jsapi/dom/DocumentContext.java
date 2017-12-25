package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;
import com.test.web.jsapi.common.dom.IDocumentContext;
import com.test.web.jsapi.common.dom.IEventTarget;

final class DocumentContext<ELEMENT, ATTRIBUTE>
	extends BaseDocumentContext<ELEMENT, ATTRIBUTE>
	implements IDocumentContext<ELEMENT, ATTRIBUTE> {

	DocumentContext(IDocument<ELEMENT, ATTRIBUTE> delegate) {
		super(delegate);
	}

	@Override
	public void addEventTargetNowWithListeners(IEventTarget target) {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException();
	}

	@Override
	public void removeEventTargetWithNoMoreListeners(IEventTarget target) {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException();
	}
}
