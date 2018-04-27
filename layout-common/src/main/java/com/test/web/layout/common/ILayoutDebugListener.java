package com.test.web.layout.common;

// For debugging elements
public interface ILayoutDebugListener<ELEMENT_TYPE> {

	void onElementStart(
			int depth,
			ELEMENT_TYPE element,
			String id,
			String tag,
			String [] classes);

	// After applied CSS styles
	void onElementCSS(int depth, ILayoutStylesGetters layoutStyles);
	
	// After also applied anything from style attribute
	void onElementStyleAttribute(int depth, ILayoutStylesGetters layoutStyles);
	
	void onComputedWidth(int depth, int curAvailableWidth, int subAvailableWidth, int subCSSWidth, boolean hasCSSWidth);

	void onComputedHeight(int depth, int curAvailableHeight, int subAvailableHeight, int subCSSHeight, boolean hasCSSHeight);
	
	void onResultingLayoutAtStartTag(int depth, IElementLayout layout, String layoutCase);

	void onResultingLayoutAtEndTag(int depth, IElementLayout layout, String layoutCase);
	
	// after element
	void onElementEnd(int depth, ELEMENT_TYPE element);
}
