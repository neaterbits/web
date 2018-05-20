package com.test.web.layout.algorithm;

import java.util.function.BiConsumer;

import com.test.web.layout.common.IElementRenderLayout;
import com.test.web.render.common.IDelayedRenderer;

// Base class for layout cases
public abstract class BaseLayoutCase<ELEMENT, STACK_ELEMENT extends LayoutStackElement<ELEMENT>, LAYOUT_UPDATE> {
	
	@FunctionalInterface
	public interface GetRenderer {
		IDelayedRenderer getRenderer(int zIndex);
	}

	protected void onElementStart(STACK_ELEMENT container, ELEMENT htmlElement, STACK_ELEMENT sub, LAYOUT_UPDATE state) {
		
	}

	protected void onElementEnd(STACK_ELEMENT container, ELEMENT htmlElement, STACK_ELEMENT sub, LAYOUT_UPDATE state) {
		
	}
	
	protected void onText(STACK_ELEMENT container, String text, TextUtil textUtil, LAYOUT_UPDATE state, GetRenderer getRenderer, BiConsumer<String, IElementRenderLayout> chunkListener) {
		
	}

	final String getName() {
		return getClass().getSimpleName();
	}
}
