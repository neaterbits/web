package com.test.web.render.html;

import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.layout.PageLayer;
import com.test.web.render.common.IRenderOperations;

public class HTMLRenderer<ELEMENT> implements HTMLElementListener<ELEMENT, Void>{

	private final PageLayer<ELEMENT> pageLayer;
	private final IRenderOperations renderOps;

	public HTMLRenderer(PageLayer<ELEMENT> pageLayer, IRenderOperations renderOps) {
		this.pageLayer = pageLayer;
		this.renderOps = renderOps;
	}
	
	@Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, Void param) {
		// Cannot call rendering here since element may not have proper layout yet
	}

	@Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, Void param) {

		final HTMLElement type = document.getType(element);
		
		switch (type) {

		default:
			throw new UnsupportedOperationException("Unknown element " + type);
		}
		
	}

	@Override
	public void onText(Document<ELEMENT> document, String text, Void param) {
		// TODO Auto-generated method stub
		
	}
}
