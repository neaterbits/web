package com.test.web.browser.common;

import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.render.html.IDisplayRendererDebugListener;
import com.test.web.render.html.IRenderDebugListener;
import com.test.web.render.queue.IRenderQueueDebugListener;

public final class DebugListeners<LAYOUT_ELEMENT_TYPE> {
	private final ILayoutDebugListener<LAYOUT_ELEMENT_TYPE> layoutListener;
	private final IRenderDebugListener htmlRenderListener;
	private final IRenderQueueDebugListener renderQueueListener;
	private final IDisplayRendererDebugListener displayRendererListener;
	
	public DebugListeners(ILayoutDebugListener<LAYOUT_ELEMENT_TYPE> layoutListener, IRenderDebugListener htmlRenderListener,
			IRenderQueueDebugListener renderQueueListener, IDisplayRendererDebugListener displayRendererListener) {
		this.layoutListener = layoutListener;
		this.htmlRenderListener = htmlRenderListener;
		this.renderQueueListener = renderQueueListener;
		this.displayRendererListener = displayRendererListener;
	}

	public ILayoutDebugListener<LAYOUT_ELEMENT_TYPE> getLayoutListener() {
		return layoutListener;
	}

	public IRenderDebugListener getHtmlRenderListener() {
		return htmlRenderListener;
	}

	public IRenderQueueDebugListener getRenderQueueListener() {
		return renderQueueListener;
	}

	public IDisplayRendererDebugListener getDisplayRendererListener() {
		return displayRendererListener;
	}
}
