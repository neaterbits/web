package com.test.web.render.html;

import com.test.web.document.common.IDocument;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.layout.IBounds;
import com.test.web.layout.IElementLayout;
import com.test.web.layout.IElementRenderLayout;
import com.test.web.layout.IPageLayout;
import com.test.web.layout.PageLayout;
import com.test.web.layout.ViewPort;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFontLookup;
import com.test.web.render.common.IRenderer;

// Listens to HTMLRenderer and renders all elements visible in viewport onto display buffer
public class DisplayRenderer<ELEMENT> implements HTMLElementListener<ELEMENT, IElementLayout>{

	private final ViewPort viewPort;
	private final IPageLayout pageLayout;
	private final IRenderer displayRenderer;
	private final IFontLookup fontLookup;
	private final IDToOffsetList idToOffsetList;
	
	private final IDisplayRendererDebugListener debugListener;
	
	// Vertical position of document displayed in viewport
	private int viewPortX;

	// Vertical position of document displayed in viewport
	private int viewPortY;

	private int depth;

	public DisplayRenderer(ViewPort viewPort, IPageLayout pageLayout, IRenderer displayRenderer, IFontLookup fontLookup, IDToOffsetList idToOffsetList, IDisplayRendererDebugListener debugListener) {

		if (viewPort == null) {
			throw new IllegalArgumentException("viewPort == null");
		}
		
		if (pageLayout == null) {
			throw new IllegalArgumentException("pageLayout == null");
		}

		if (displayRenderer == null) {
			throw new IllegalArgumentException("displayRenderer == null");
		}
		
		if (fontLookup == null) {
			throw new IllegalArgumentException("fontLookup == null");
		}

		if (idToOffsetList == null) {
			throw new IllegalArgumentException("idToOffsetList == null");
		}

		this.viewPort = viewPort;
		this.pageLayout = pageLayout;
		this.displayRenderer = displayRenderer;
		this.fontLookup = fontLookup;
		this.idToOffsetList = idToOffsetList;
		this.debugListener = debugListener;

		this.viewPortX = 0;
		this.viewPortY = 0;

		this.depth = 0;
	}
	

	@Override
	public void onElementStart(IDocument<ELEMENT> document, ELEMENT element, IElementLayout layout) {
		// Nothing to do here, we only sync when we are certain that have layout dimensions
		// We figure startoffset for last elements before viewport start
		
		++ depth;
	}

	@Override
	public void onElementEnd(IDocument<ELEMENT> document, ELEMENT element, IElementLayout layout) {

		// If overlaps with viewport, we will buffer offsets
		final IBounds bounds = layout.getAbsoluteBounds();
	
		if (       bounds.getLeft() >= viewPortX && bounds.getLeft() < viewPortX + viewPort.getWidth()
		      && bounds.getTop() >= viewPortY && bounds.getTop()  < viewPortY + viewPort.getHeight()) {
			
			if (debugListener != null) {
				debugListener.onVisibleElement(depth, document.getType(element), layout.getRenderQueueStartOffset(), layout.getRenderQueueEndOffset());
			}
			
			// overlaps with viewport, add rendering queue offsets to list
			this.idToOffsetList.add(layout.getZIndex(), layout.getRenderQueueStartOffset(), layout.getRenderQueueEndOffset());
		}
		
		// TODO How to detect when we have passed all visible elements and can sync to display?
		// Cannot go by left/top since this may be a nested element and another container element which has not been called yet may still overlap
		
		// safe bet is a display:block element at level 1 in the document. If it starts below viewport, then we are ready to display
		// same for display:inline or inline-block elements at the same level
		
		// TODO might just sync incrementally as well
		-- depth;
		
		if (depth == 0) {
			
			if (debugListener != null) {
				debugListener.onAllVisibleProcessed_StartReplay(depth);
			}
			
			// write all layers to display renderer
			pageLayout.forEachLayerSortedOnZIndex((zIndex, renderQueue) -> {
				
				if (debugListener != null) {
					debugListener.onAllVisibleProcessed_ReplayLayer(depth, zIndex);
				}

				idToOffsetList.replaySorted(renderQueue, displayRenderer, fontLookup);
			});

			if (debugListener != null) {
				debugListener.onAllVisibleProcessed_ReplayDone(depth);
			}

			// sync in case rendered via double buffering
			displayRenderer.sync();

			if (debugListener != null) {
				debugListener.onAllVisibleProcessed_DisplaySynced(depth);
			}
		}
	}

	@Override
	public void onText(IDocument<ELEMENT> document, ELEMENT element, String text, IElementLayout layout) {
		
		final IBounds bounds = layout.getAbsoluteBounds();
		
		if (   bounds.getLeft() >= viewPortX && bounds.getLeft() < viewPortX + viewPort.getWidth()
	      && bounds.getTop() >= viewPortY && bounds.getTop()  < viewPortY + viewPort.getHeight()) {
		
				if (debugListener != null) {
					debugListener.onVisibleText(depth, document.getType(element), text, layout.getRenderQueueStartOffset(), layout.getRenderQueueEndOffset());
				}
				
				// overlaps with viewport, add rendering queue offsets to list
				this.idToOffsetList.add(layout.getZIndex(), layout.getRenderQueueStartOffset(), layout.getRenderQueueEndOffset());
			}	
		}
}
