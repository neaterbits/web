package com.test.web.layout;

import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.document.common.HTMLElement;

// For debugging elements
public interface ILayoutDebugListener {

	void onElementStart(
			int depth,
			HTMLElement element,
			String id,
			String tag,
			String [] classes);

	// After applied CSS styles
	void onElementCSS(int depth, CSSLayoutStyles layoutStyles);
	
	// After also applied anything from style attribute
	void onElementStyleAttribute(int depth, CSSLayoutStyles layoutStyles);
	
	void onComputedWidth(int depth, int width);

	void onComputedHeight(int depth, int height);

	// after element
	void onElementEnd(int depth, HTMLElement element);
}
