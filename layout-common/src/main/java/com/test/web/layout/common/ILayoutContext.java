package com.test.web.layout.common;

public interface ILayoutContext<ELEMENT, ELEMENT_TYPE, DOCUMENT> {

	void computeLayoutStyles(
			DOCUMENT document,
			ELEMENT element,
			IFontSettings<ELEMENT_TYPE> fontSettings,
			LayoutStyles result,
			int debugDepth,
			ILayoutDebugListener<ELEMENT_TYPE> debugListener);
	
	
	/**
	 * Get known width/height for an element as width << 32 | height or -1 if unknown
	 * @param document
	 * @param element
	 * @return width << 32 | height
	 */
	
	long getKnownWidthHeight(
			DOCUMENT document,
			ELEMENT element);
}
