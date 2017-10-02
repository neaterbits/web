package com.test.web.layout;

import java.util.ArrayList;
import java.util.List;

/*
 * A layer within a layout, eg. defined by z-index
 */

public class PageLayer<ELEMENT> implements Comparable<PageLayer<ELEMENT>> {
	private final int index;
	private final List<ElementLayout> layout;

	public PageLayer(int index) {
		this.index = index;
		this.layout = new ArrayList<>();
	}

	
	int getIndex() {
		return index;
	}

	@Override
	public int compareTo(PageLayer<ELEMENT> o) {
		return Integer.compare(index, o.index);
	}
}
