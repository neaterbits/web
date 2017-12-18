package com.test.web.layout.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.web.document.common.IDocumentBase;
import com.test.web.document.common.IElementListener;
import com.test.web.layout.common.FontKey;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.ILayoutContext;
import com.test.web.layout.common.ILayoutState;
import com.test.web.layout.common.ViewPort;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IDelayedRendererFactory;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.FontSpec;

// State maintained while doing layout, ie. while recursing the DOM
public final class LayoutState<
			ELEMENT,
			ELEMENT_TYPE,
			DOCUMENT extends IDocumentBase<ELEMENT, ELEMENT_TYPE, DOCUMENT>> 

		implements ILayoutState {
	
	private final ITextExtent textExtent;
	private final ViewPort viewPort;
	
	// For finding size of text strings when using a particular font for rendering
	private final ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext;

	// Optional listener to eg do rendering in the layout flow
	private final IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener;
	
	// We have to maintain a stack for computed elements, ElementLayout contains computed values for element at that level
	private final List<StackElement> stack;
	private int curDepth;

	// Cache of fonts used during layout
	private final Map<FontKey, IFont> fonts;
	
	// Resulting page layout dimensionsn are collected here
	private final PageLayout<ELEMENT> pageLayout;

	// Position of current display block
	private int curBlockYPos;
	
	public LayoutState(
			ITextExtent textExtent,
			ViewPort viewPort,
			ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> layoutContext,
			PageLayout<ELEMENT> pageLayout,
			IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> listener) {
		this.textExtent = textExtent;
		this.viewPort = viewPort;
		this.layoutContext = layoutContext;
		this.listener = listener;

		this.stack = new ArrayList<>();
		this.curDepth = 0;

		this.fonts = new HashMap<>();

		this.pageLayout = pageLayout;
		
		// TODO how does this work for other zIndex? Will they have their separate layout?
		this.curBlockYPos = 0;
		
		// Push intial element on stack, which is not a real element
		push(viewPort.getWidth(), viewPort.getHeight());
	}
	
	@Override
	public ViewPort getViewPort() {
		return viewPort;
	}

	ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> getLayoutContext() {
		return layoutContext;
	}
	
	PageLayout<ELEMENT> getPageLayout() {
		return pageLayout;
	}
	
	IElementListener<ELEMENT, ELEMENT_TYPE, DOCUMENT, IElementRenderLayout> getListener() {
		return listener;
	}

	PageLayer<ELEMENT> addOrGetLayer(int index, IDelayedRendererFactory renderFactory) {
		return pageLayout.addOrGetLayer(index, renderFactory);
	}

	StackElement getCur() {
		return curDepth == 0 ? null : stack.get(curDepth - 1);
	}
	
	StackElement push(int availableWidth, int availableHeight) {
		
		final StackElement ret;
		
		if (curDepth == stack.size()) {
			ret = new StackElement(curDepth, availableWidth, availableHeight);
			
			stack.add(ret);
		}
		else {
			// reuse existing
			ret = stack.get(curDepth);
			
	    	// make sure is cleared since reused
			ret.clear();
			ret.init(availableWidth, availableHeight);
		}

		++ curDepth;
	
		return ret;
	}
	
	void pop() {
		-- curDepth;
	}
	
	IFont getOrOpenFont(FontSpec spec, short style) {
		final FontKey fontKey = new FontKey(spec, style);
		
		IFont font = fonts.get(fontKey);
		
		if (font == null) {
			font = textExtent.getFont(spec.getFamily(), spec.getSize(), style);

			if (font == null) {
				throw new IllegalStateException("Failed to open font " + spec + " with style " + style);
			}
		}

		return font;
	}
	
	void close() {
		// Close all fonts
		for (IFont font : fonts.values()) {
			textExtent.closeFont(font);
		}

		this.fonts.clear();
	}
	
	int getDepth() {
		return curDepth;
	}
}