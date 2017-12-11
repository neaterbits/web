package com.test.web.render.html;

import com.test.web.document.common.HTMLElement;

public interface IDisplayRendererDebugListener {
	
	void onVisibleElement(int depth, HTMLElement element, int startOffset, int endOffset);
	
	void onAllVisibleProcessed_StartReplay(int depth);
	
	void onAllVisibleProcessed_ReplayLayer(int depth, int zIndex);

	void onAllVisibleProcessed_ReplayDone(int depth);

	void onAllVisibleProcessed_DisplaySynced(int depth);
}
