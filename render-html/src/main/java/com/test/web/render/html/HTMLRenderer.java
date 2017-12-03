package com.test.web.render.html;

import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.layout.IBounds;
import com.test.web.layout.IElementRenderLayout;

public class HTMLRenderer<ELEMENT> implements HTMLElementListener<ELEMENT, IElementRenderLayout>{

	private final IRenderDebugListener debugListener;

	private int depth;
	
	public HTMLRenderer(IRenderDebugListener debugListener) {
		this.debugListener = debugListener;
		
		this.depth = 0;
	}

	@Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout layout) {
		
		if (debugListener != null) {
			debugListener.onElementStart(depth, document.getType(element), layout);
			++ depth;
		}
		
		renderElement(document, element, layout);
	}

	@Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout layout) {

		if (debugListener != null) {
			-- depth;
			debugListener.onElementEnd(depth, document.getType(element), layout);
		}
	}

	@Override
	public void onText(Document<ELEMENT> document, ELEMENT element, String text, IElementRenderLayout layout) {

		if (debugListener != null) {
			debugListener.onText(depth, layout, text);
		}

		renderText(text, layout);
	}
	
	private void renderElement(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout layout) {
		final HTMLElement type = document.getType(element);
		
		switch (type) {

		case HTML:
		case DIV:
		case SPAN:
			// should render background, border etc here
			break;

		default:
			throw new UnsupportedOperationException("Unknown element " + type);
		}
	}
	
	// render text at specified position within renderer
	private void renderText(String text, IElementRenderLayout layout) {
	
		final IBounds innerBounds = layout.getInnerBounds();

		// For now just render the text with default font
		layout.getRenderer().drawText(innerBounds.getLeft(), innerBounds.getTop(), text);
	}
}
