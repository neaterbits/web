package com.test.web.layout.algorithm;

import java.util.LinkedHashMap;

import com.test.web.layout.common.IBounds;
import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.IWrapping;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

/*
 * A layer within a layout, eg. defined by z-index
 */

public class PageLayer<ELEMENT> implements Comparable<PageLayer<ELEMENT>> {
	private final int index;
	private final LinkedHashMap<ELEMENT, ElementLayout> layouts;
	
	// The renderer used to render this layer
	private final IDelayedRenderer renderer;

	PageLayer(int index, IDelayedRenderer renderer) {
		this.index = index;
		this.layouts = new LinkedHashMap<>();
		this.renderer = renderer;
	}

	void add(ELEMENT element, ElementLayout layout) {
		if (element == null) {
			throw new IllegalArgumentException("element == null");
		}
		
		if (layout == null) {
			throw new IllegalArgumentException("layout == null");
		}
		
		if (layouts.put(element, layout) != null) {
			throw new IllegalStateException("Already has layout for element " + element);
		}
	}
	
	int getIndex() {
		return index;
	}
	
	IDelayedRenderer getRenderer() {
		return renderer;
	}
	
	private IElementLayout get(ELEMENT element) {
		return layouts.get(element);
	}
	
	// Bounds outside wrapping and padding
	IBounds getOuterBounds(ELEMENT element) {
		return get(element).getOuterBounds();
	}
	
	// Bounds of element itself
	IBounds getInnerBounds(ELEMENT element) {
		return get(element).getInnerBounds();
	}
	
	IWrapping getMargins(ELEMENT element) {
		return get(element).getMargins();
	}
	
	IWrapping getPadding(ELEMENT element) {
		return get(element).getPadding();
	}

	IFont getFont(ELEMENT element) {
		return get(element).getFont();
	}

	@Override
	public int compareTo(PageLayer<ELEMENT> o) {
		return Integer.compare(index, o.index);
	}
}
