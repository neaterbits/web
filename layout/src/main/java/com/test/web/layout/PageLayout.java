package com.test.web.layout;

import java.util.ArrayList;
import java.util.List;

import com.test.web.render.common.IRenderFactory;

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
	
	public  PageLayer<ELEMENT> addOrGetLayer(int index, IRenderFactory renderFactory) {
		PageLayer<ELEMENT> found = null;
		
		for (PageLayer<ELEMENT> l : layers) {
			if (l.getIndex() == index) {
				found = l;
				break;
			}
		}
		
		if (found == null) {
			found = new PageLayer<>(index, renderFactory.createRenderer());
			layers.add(found);
		}
		
		return found;
	}
}
