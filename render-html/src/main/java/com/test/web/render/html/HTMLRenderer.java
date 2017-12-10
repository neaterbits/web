package com.test.web.render.html;

import com.test.web.document.common.Document;
import com.test.web.document.common.HTMLElement;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.layout.IBounds;
import com.test.web.layout.IElementLayout;
import com.test.web.layout.IElementRenderLayout;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IMarkRenderOperations;
import com.test.web.render.common.IRenderOperations;

public class HTMLRenderer<ELEMENT> implements HTMLElementListener<ELEMENT, IElementRenderLayout>{

	private final IRenderDebugListener debugListener;
	private final HTMLElementListener<ELEMENT, IElementLayout> listener; // typically display renderer

	// in case we have to render in onElementEnd()
	private int [] delayedRenderMarks;
	
	private int depth;
	
	public HTMLRenderer(IRenderDebugListener debugListener, HTMLElementListener<ELEMENT, IElementLayout> listener) {
		this.debugListener = debugListener;
		this.listener = listener;
		
		this.delayedRenderMarks = new int[100];
		this.depth = 0;
	}

	@Override
	public void onElementStart(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout layout) {
		
		if (debugListener != null) {
			debugListener.onElementStart(depth, document.getType(element), layout);
		}
		
		// Get renderer to use
		final IRenderOperations renderOps;
		final int renderMark;
		
		final IRenderOperations renderer;
		
		if ( ! layout.areBoundsComputed() ) {
			// We do not have dimensions yet so we must render later
			// Thus we just mark here and do the rendering at that point
			renderMark = layout.getRenderer().mark();
			
			//renderer = layout.getRenderer().getOperationsForMark(renderMark);
		}
		else {
			renderMark = IDelayedRenderer.MARK_NONE;

			renderElement(document, element, layout);
		}

		delayedRenderMarks[depth] = renderMark;

		++ depth;

		if (listener != null) {
			listener.onElementStart(document, element, layout);
		}
	}

	@Override
	public void onElementEnd(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout layout) {

		final int renderMark = delayedRenderMarks[depth];
		if (renderMark != IDelayedRenderer.MARK_NONE) {
			// We did not have bounds in onElementStart() so must render now
			if ( ! layout.areBoundsComputed() ) {
				throw new IllegalStateException("bounds not computed in onElementEnd()");
			}
			
			final IMarkRenderOperations renderer = layout.getRenderer().getOperationsForMark(renderMark);
			
			// render to marked position so that will be rendered to real image buffer in correct order later
			renderElement(document, element, layout);
			
			renderer.endOperationsForMark();
		}
		
		if (debugListener != null) {
			debugListener.onElementEnd(depth, document.getType(element), layout);
		}

		-- depth;
		
		if (listener != null) {
			listener.onElementEnd(document, element, layout);
		}
	}

	@Override
	public void onText(Document<ELEMENT> document, ELEMENT element, String text, IElementRenderLayout layout) {

		if (debugListener != null) {
			debugListener.onText(depth, layout, text);
		}

		renderText(text, layout);
		
		if (listener != null) {
			listener.onText(document, element, text, layout);
		}
	}
	
	private void renderElement(Document<ELEMENT> document, ELEMENT element, IElementRenderLayout layout) {
		
		final IDelayedRenderer renderer = layout.getRenderer();
		
		final int startOffset = renderer.getOffset();
		
		final HTMLElement type = document.getType(element);
		
		switch (type) {

		case HTML:
		case DIV:
		case SPAN:
			// should render background, border etc here
			break;
			
		case A:
			break;

		default:
			throw new UnsupportedOperationException("Unknown element " + type);
		}

		final int endOffset = renderer.getOffset();

		// cache offsets for queued rendering ops so we can replay only this element onto a buffer or display
		layout.setRenderQueueOffsets(startOffset, endOffset);
	}

	// render text at specified position within renderer
	private void renderText(String text, IElementRenderLayout layout) {
	
		final IBounds innerBounds = layout.getInnerBounds();

		// For now just render the text with default font
		layout.getRenderer().drawText(innerBounds.getLeft(), innerBounds.getTop(), text);
	}
}
