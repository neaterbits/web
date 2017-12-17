package com.test.web.render.html;

import com.test.web.document.html.common.HTMLElement;
import com.test.web.layout.common.IElementLayout;

public interface IRenderDebugListener {

	void onElementStart(int depth, HTMLElement element, IElementLayout layout);
	
	void onElementEnd(int depth, HTMLElement element, IElementLayout layout);
	
	void onText(int depth, IElementLayout layout, String text);
	
	void onSetRenderQueueOffsets(int depth, HTMLElement element, int startOffset, int endOffset);
}
