package com.test.web.render.queue;

import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IDelayedRendererFactory;

public class QueueRendererFactory implements IDelayedRendererFactory {

	private final IRenderQueueDebugListener debugListener;
	
	public QueueRendererFactory(IRenderQueueDebugListener debugListener) {
		this.debugListener = debugListener;
	}

	@Override
	public IDelayedRenderer createRenderer() {
		return new RenderQueue(debugListener);
	}
}
