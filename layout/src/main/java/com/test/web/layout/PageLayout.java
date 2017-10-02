package com.test.web.layout;

import java.util.ArrayList;
import java.util.List;

/*
 * Page layout consists of a number of page layers, each page
 * being at a different z-index or similar, sorted in order
 * 
 */

public final class PageLayout<ELEMENT> {

	private final List<PageLayer<ELEMENT>> layers;
	
	public PageLayout() {
		this.layers = new ArrayList<>();
	}
	
	public  PageLayer<ELEMENT> addOrGetLayer(int index) {
		PageLayer<ELEMENT> found = null;
		
		for (PageLayer<ELEMENT> l : layers) {
			if (l.getIndex() == index) {
				found = l;
				break;
			}
		}
		
		if (found == null) {
			found = new PageLayer<>(index);
			layers.add(found);
		}
		
		return found;
	}
}
