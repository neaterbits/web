package com.test.web.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.web.css.common.CSSContext;
import com.test.web.document.common.HTMLElementListener;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IRenderFactory;
import com.test.web.render.common.IRenderer;
import com.test.web.render.common.ITextExtent;
import com.test.web.types.FontSpec;

// State maintained while doing layout, ie. while recursing the DOM
public final class LayoutState<ELEMENT> {
	
	private final ITextExtent textExtent;
	private final IRenderer displayRenderer;
	
	// For finding size of text strings when using a particular font for rendering
	private final CSSContext<ELEMENT> cssContext;

	// Optional listener to eg do rendering in the layout flow
	private final HTMLElementListener<ELEMENT, IElementRenderLayout> listener;
	
	// We have to maintain a stack for computed elements, ElementLayout contains computed values for element at that level
	private final List<StackElement> stack;
	private int curDepth;

	// Cache of fonts used during layout
	private final Map<FontKey, IFont> fonts;
	
	// Resulting page layout dimensionsn are collected here
	private final PageLayout<ELEMENT> pageLayout;

	// Position of current display block
	private int curBlockYPos;
	
	public LayoutState(ITextExtent textExtent, ViewPort viewPort, IRenderer displayRenderer, CSSContext<ELEMENT> cssContext, HTMLElementListener<ELEMENT, IElementRenderLayout> listener) {
		this.textExtent = textExtent;
		this.displayRenderer = displayRenderer;
		this.cssContext = cssContext;
		this.listener = listener;

		this.stack = new ArrayList<>();
		this.curDepth = 0;

		this.fonts = new HashMap<>();

		this.pageLayout = new PageLayout<>();
		
		// TODO how does this work for other zIndex? Will they have their separate layout?
		this.curBlockYPos = 0;
		
		// Push intial element on stack
		push(viewPort.getViewPortWidth(), viewPort.getViewPortHeight());
	}
	
	CSSContext<ELEMENT> getCSSContext() {
		return cssContext;
	}
	
	IRenderer getDisplayRenderer()  {
		return displayRenderer;
	}
	
	PageLayout<ELEMENT> getPageLayout() {
		return pageLayout;
	}
	
	HTMLElementListener<ELEMENT, IElementRenderLayout> getListener() {
		return listener;
	}

	PageLayer<ELEMENT> addOrGetLayer(int index, IRenderFactory renderFactory) {
		return pageLayout.addOrGetLayer(index, displayRenderer, renderFactory);
	}

	StackElement getCur() {
		return curDepth == 0 ? null : stack.get(curDepth - 1);
	}
	
	StackElement push(int availableWidth, int availableHeight) {
		
		final StackElement ret;
		
		if (curDepth == stack.size()) {
			ret = new StackElement(availableWidth, availableHeight);
			
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
			font = textExtent.getFont(spec.getFamily(), spec.getName(), spec.getSize(), style);

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
