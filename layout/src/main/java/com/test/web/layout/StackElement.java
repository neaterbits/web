package com.test.web.layout;

import com.test.web.css.common.CSSLayoutStyles;

// Stack element on the layout stack, we add information here at time of start tag
// and then just fetch that information when getting to end tag
final class StackElement {
	// available width and height at this level.
	// width is given by the viewport
	private int availableWidth;
	private int availableHeight;
	
	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	final CSSLayoutStyles layoutStyles;
	
	// The resulting layout after computation
	final ElementLayout resultingLayout;

	StackElement(int availableWidth, int availableHeight) {
		
		this.availableWidth = availableWidth;
		this.availableHeight = availableHeight;
		
		this.layoutStyles = new CSSLayoutStyles();
		this.resultingLayout = new ElementLayout();
	}

	int getAvailableWidth() {
		return availableWidth;
	}

	void setAvailableWidth(int availableWidth) {
		this.availableWidth = availableWidth;
	}

	int getAvailableHeight() {
		return availableHeight;
	}

	void setAvailableHeight(int availableHeight) {
		this.availableHeight = availableHeight;
	}

	CSSLayoutStyles getLayoutStyles() {
		return layoutStyles;
	}

	ElementLayout getResultingLayout() {
		return resultingLayout;
	}
}

