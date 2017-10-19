package com.test.web.layout;

import com.test.web.render.common.IRenderer;

public interface IElementRenderLayout extends IElementLayout {

	// Get renderer from element layout since there may be different renderers for
	// elements eg. if rendered into separate background buffers
	IRenderer getRenderer();
	
}
