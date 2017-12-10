package com.test.web.render.queue;

import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IDelayedRendererFactory;

public class QueueRendererFactory implements IDelayedRendererFactory {

	@Override
	public IDelayedRenderer createRenderer() {
		return new RenderQueue();
	}
}
