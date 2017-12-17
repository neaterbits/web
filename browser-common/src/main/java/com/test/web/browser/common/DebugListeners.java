package com.test.web.browser.common;

import com.test.web.layout.common.ILayoutDebugListener;
import com.test.web.render.html.IDisplayRendererDebugListener;
import com.test.web.render.html.IRenderDebugListener;
import com.test.web.render.queue.IRenderQueueDebugListener;

public class DebugListeners {
	private final ILayoutDebugListener layoutListener;
	private final IRenderDebugListener htmlRenderListener;
	private final IRenderQueueDebugListener renderQueueListener;
	private final IDisplayRendererDebugListener displayRendererListener;
	
	public DebugListeners(ILayoutDebugListener layoutListener, IRenderDebugListener htmlRenderListener,
			IRenderQueueDebugListener renderQueueListener, IDisplayRendererDebugListener displayRendererListener) {
		this.layoutListener = layoutListener;
		this.htmlRenderListener = htmlRenderListener;
		this.renderQueueListener = renderQueueListener;
		this.displayRendererListener = displayRendererListener;
	}

	public ILayoutDebugListener getLayoutListener() {
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
