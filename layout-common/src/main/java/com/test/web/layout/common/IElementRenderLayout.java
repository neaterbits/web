package com.test.web.layout.common;

import com.test.web.render.common.IDelayedRenderer;

public interface IElementRenderLayout extends IElementLayout {

	// Get renderer from element layout since there may be different renderers for
	// elements eg. if rendered into separate background buffers
	IDelayedRenderer getRenderer();
	
	void setRenderQueueOffsets(int startOffset, int endOffset);
}
