package com.test.web.layout.algorithm;

import java.util.LinkedHashMap;

import com.test.web.layout.common.IBounds;
import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.layout.common.IWrapping;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

/**
 * A layer within a layout, eg. defined by z-index.
 * Implements Comparable so we can sort layers according to z-index.
 * Is also associated with a renderer so that the layer can be rendered onto something. Note the renderer
 * is a delayed renderer, ie. a queue of render operations so that render operations can be rearranged
 * eg. in case of an outer div with background for which height is unknown (can only render background when height is known)
 */

public final class PageLayer<ELEMENT> implements Comparable<PageLayer<ELEMENT>> {
	private final int index;
	private final LinkedHashMap<ELEMENT, IElementRenderLayout> layouts;
	
	// The renderer used to render this layer
	private final IDelayedRenderer renderer;

	PageLayer(int index, IDelayedRenderer renderer) {
		this.index = index;
		this.layouts = new LinkedHashMap<>();
		this.renderer = renderer;
	}

	void add(ELEMENT element, IElementRenderLayout layout) {
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
	public IBounds getOuterBounds(ELEMENT element) {
		return get(element).getOuterBounds();
	}
	
	// Bounds of element itself
	public IBounds getInnerBounds(ELEMENT element) {
		return get(element).getInnerBounds();
	}
	
	public IWrapping getMargins(ELEMENT element) {
		return get(element).getMargins();
	}
	
	public IWrapping getPadding(ELEMENT element) {
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
