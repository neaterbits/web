package com.test.web.layout.common.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import com.test.web.layout.common.IPageLayout;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IDelayedRendererFactory;

/*
 * Page layout consists of a number of page layers, each page
 * being at a different z-index or similar, sorted in order
 * 
 */

public final class PageLayout<ELEMENT> implements IPageLayout {

	private final List<PageLayer<ELEMENT>> layers;
	
	public PageLayout() {
		this.layers = new ArrayList<>();
	}
	
	public PageLayer<ELEMENT> addOrGetLayer(int index,  IDelayedRendererFactory renderFactory) {
		PageLayer<ELEMENT> found = null;
		
		for (PageLayer<ELEMENT> l : layers) {
			if (l.getIndex() == index) {
				found = l;
				break;
			}
		}
		
		if (found == null) {
			final IDelayedRenderer renderer = renderFactory.createRenderer();
			
			found = new PageLayer<>(index, renderer);

			layers.add(found);
		}
		
		return found;
	}
	
	public List<PageLayer<ELEMENT>> getLayers() {
		return Collections.unmodifiableList(layers);
	}

	@Override
	public void forEachLayerSortedOnZIndex(BiConsumer<Integer, IDelayedRenderer> consumer) {
		
		// not many layers so just make a copy and sort now
		final List<PageLayer<ELEMENT>> layersCopy = new ArrayList<>(layers);
		
		Collections.sort(layersCopy, (l1, l2) -> Integer.compare(l1.getIndex(), l2.getIndex()));
		
		for (PageLayer<ELEMENT> l : layersCopy) {
			consumer.accept(l.getIndex(), l.getRenderer());
		}
	}
}
