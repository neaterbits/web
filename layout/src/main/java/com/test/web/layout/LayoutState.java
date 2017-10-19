package com.test.web.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.web.css.common.CSSContext;
import com.test.web.render.common.IFont;
import com.test.web.render.common.IRenderFactory;

// State maintained while doing layout, ie. while recursing the DOM
public final class LayoutState<ELEMENT> {
	// For finding size of text strings when using a particular font for rendering
	private final CSSContext<ELEMENT> cssContext;

	// We have to maintain a stack for computed elements, ElementLayout contains computed values for element at that level
	private final List<StackElement> stack;
	private int curDepth;

	// Cache of fonts used during layout
	private final Map<FontKey, IFont> fonts;
	
	// Resulting page layout dimensionsn are collected here
	private final PageLayout<ELEMENT> pageLayout;

	// Position of current display block
	private int curBlockYPos;

	
	public LayoutState(ViewPort viewPort, CSSContext<ELEMENT> cssContext) {
		this.cssContext = cssContext;

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
	
	PageLayout<ELEMENT> getPageLayout() {
		return pageLayout;
	}

	PageLayer<ELEMENT> addOrGetLayer(int index, IRenderFactory renderFactory) {
		return pageLayout.addOrGetLayer(index, renderFactory);
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
		}

		++ curDepth;
	
		return ret;
	}
	
	void pop() {
		-- curDepth;
	}
}
