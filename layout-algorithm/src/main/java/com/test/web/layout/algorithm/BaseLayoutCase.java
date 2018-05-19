package com.test.web.layout.algorithm;

import java.util.function.BiConsumer;

import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.render.common.IDelayedRenderer;

// Base class for layout cases
public abstract class BaseLayoutCase<STACK_ELEMENT extends LayoutStackElement, LAYOUT_UPDATE> {
	
	@FunctionalInterface
	public interface GetRenderer {
		IDelayedRenderer getRenderer(int zIndex);
	}

	protected <ELEMENT> void onElementStart(STACK_ELEMENT container, ELEMENT htmlElement, STACK_ELEMENT sub, LAYOUT_UPDATE state) {
		
	}

	protected <ELEMENT> void onElementEnd(STACK_ELEMENT container, ELEMENT htmlElement, STACK_ELEMENT sub, LAYOUT_UPDATE state) {
		
	}
	
	protected <ELEMENT >void onText(STACK_ELEMENT container, String text, TextUtil textUtil, LAYOUT_UPDATE state, GetRenderer getRenderer, BiConsumer<String, IElementRenderLayout> chunkListener) {
		
	}

	final String getName() {
		return getClass().getSimpleName();
	}
}
