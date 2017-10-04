package com.test.web.layout;

import java.util.ArrayList;
import java.util.List;

import com.test.web.render.common.IRenderer;

/*
 * A layer within a layout, eg. defined by z-index
 */

public class PageLayer<ELEMENT> implements Comparable<PageLayer<ELEMENT>> {
	private final int index;
	private final List<ElementLayout> layout;
	
	// The renderer used to render this layer
	private final IRenderer renderer;

	public PageLayer(int index, IRenderer renderer) {
		this.index = index;
		this.layout = new ArrayList<>();
		this.renderer = renderer;
	}

	
	int getIndex() {
		return index;
	}
	
	IRenderer getRenderer() {
		return renderer;
	}

	@Override
	public int compareTo(PageLayer<ELEMENT> o) {
		return Integer.compare(index, o.index);
	}
}
