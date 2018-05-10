package com.test.web.layout.common;

// For debugging elements
public interface ILayoutDebugListener<ELEMENT_TYPE> {

	void onElementStart(
			int depth,
			ELEMENT_TYPE element,
			String id,
			String tag,
			String [] classes);

	void onElementLayoutCase(int depth, ELEMENT_TYPE element, String layoutCaseName);

	// After applied CSS styles
	void onElementCSS(int depth, ILayoutStylesGetters layoutStyles);
	
	// After also applied anything from style attribute
	void onElementStyleAttribute(int depth, ILayoutStylesGetters layoutStyles);
	
	void onComputedWidth(int depth, int curAvailableWidth, int subAvailableWidth, int subCSSWidth, boolean hasCSSWidth);

	void onComputedHeight(int depth, int curAvailableHeight, int subAvailableHeight, int subCSSHeight, boolean hasCSSHeight);
	
	void onResultingLayoutAtStartTag(int depth, IElementLayout layout, String layoutCase);

	void onResultingLayoutAtEndTag(int depth, IElementLayout layout, String layoutCase);
	
	// after element
	void onElementEnd(int depth, ELEMENT_TYPE element, String layoutCaseName);
	
	// On started processing inline text
	void onTextStart(int depth, ELEMENT_TYPE containerElement, String text) ;

	// On processed one line or box of inline text
	void onTextLine(int depth, ELEMENT_TYPE containerElement, String lineText, IElementLayout layout) ;
	
	// After processing inline text
	void onTextEnd(int depth, ELEMENT_TYPE containerElement, String text);
	
	void onApplyLineBreakStart(int depth, int lineNo);

	void onApplyLineBreakEnd(int depth);
}
