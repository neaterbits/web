package com.test.web.jsapi.dom;

import com.test.web.document.html.common.IDocument;

final class DocumentContext<ELEMENT, ATTRIBUTE>
	extends BaseDocumentContext<ELEMENT, ATTRIBUTE>
	implements IDocumentContext<ELEMENT, ATTRIBUTE> {

	DocumentContext(IDocument<ELEMENT, ATTRIBUTE> delegate) {
		super(delegate);
	}

	@Override
	public void addEventTargetNowWithListeners(EventTarget<ELEMENT, ATTRIBUTE, ? extends IDocumentContext<ELEMENT, ATTRIBUTE>> target) {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException();
	}

	@Override
	public void removeEventTargetWithNoMoreListeners(EventTarget<ELEMENT, ATTRIBUTE, ? extends IDocumentContext<ELEMENT, ATTRIBUTE>> target) {
		// TODO Auto-generated method stub

		throw new UnsupportedOperationException();
	}
}
