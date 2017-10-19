package com.test.web.render.html;

import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.layout.IElementRenderLayout;

public class HTMLRenderer<ELEMENT> implements HTMLElementListener<ELEMENT, IElementRenderLayout>{

	@Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout param) {
		// Cannot call rendering here since element may not have proper layout yet
	}

	@Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout param) {

		final HTMLElement type = document.getType(element);
		
		switch (type) {

		default:
			throw new UnsupportedOperationException("Unknown element " + type);
		}
		
	}

	@Override
	public void onText(Document<ELEMENT> document, String text, IElementRenderLayout param) {
		// TODO Auto-generated method stub
	}
}
