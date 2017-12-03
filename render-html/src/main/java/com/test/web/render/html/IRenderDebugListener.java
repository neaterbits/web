package com.test.web.render.html;

import com.test.web.document.common.HTMLElement;
import com.test.web.layout.IElementLayout;

public interface IRenderDebugListener {

	void onElementStart(int depth, HTMLElement element, IElementLayout layout);
	
	void onElementEnd(int depth, HTMLElement element, IElementLayout layout);
	
	void onText(int depth, IElementLayout layout, String text);
}
