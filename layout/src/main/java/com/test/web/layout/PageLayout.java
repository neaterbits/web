package com.test.web.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.test.web.render.common.IRenderFactory;
import com.test.web.render.common.IRenderer;

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
	
	public  PageLayer<ELEMENT> addOrGetLayer(int index, IRenderer displayRenderer, IRenderFactory renderFactory) {
		PageLayer<ELEMENT> found = null;
		
		for (PageLayer<ELEMENT> l : layers) {
			if (l.getIndex() == index) {
				found = l;
				break;
			}
		}
		
		if (found == null) {
			// if z-index 0, we can render directly onto display (which might be an offscreen buffer for doublebuffering as well)
			
			final IRenderer renderer = index == 0
					? displayRenderer
					: renderFactory.createRenderer();
			
			found = new PageLayer<>(index, renderer);

			layers.add(found);
		}
		
		return found;
	}
	
	public List<PageLayer<ELEMENT>> getLayers() {
		return Collections.unmodifiableList(layers);
	}
}
