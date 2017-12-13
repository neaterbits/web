package com.test.web.jsapi.dom;

import com.test.web.document.common.IDocument;

final class DocumentContext<ELEMENT>
	extends BaseDocumentContext<ELEMENT>
	implements IDocumentContext<ELEMENT> {

	DocumentContext(IDocument<ELEMENT> delegate) {
		super(delegate);
	}

	@Override
	public void addEventTargetNowWithListeners(EventTarget<ELEMENT, ? extends IDocumentContext<ELEMENT>> target) {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException();
	}

	@Override
	public void removeEventTargetWithNoMoreListeners(EventTarget<ELEMENT, ? extends IDocumentContext<ELEMENT>> target) {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException();
	}
}
