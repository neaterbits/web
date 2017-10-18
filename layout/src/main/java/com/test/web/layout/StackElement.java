package com.test.web.layout;

import com.test.web.css.common.CSSLayoutStyles;

// Stack element on the layout stack, we add information here at time of start tag
// and then just fetch that information when getting to end tag
// Mutable so can be reused within stack
final class StackElement {
	// available width and height at this level, ie. the max dimension that the element at this level can have
	// width is given by the viewport at the window level mainly for width but also for height if specified as 100%
	private int availableWidth;
	private int availableHeight;
	
	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	final CSSLayoutStyles layoutStyles;
	
	// The resulting layout after computation of width and height
	final ElementLayout resultingLayout;

	// Max height for elements in this block, we'll advance position with this many
	private int maxBlockElementHeight;

	StackElement(int availableWidth, int availableHeight) {
		
		init(availableWidth, availableHeight);
		
		this.layoutStyles = new CSSLayoutStyles();
		this.resultingLayout = new ElementLayout();
	}
	
	void init(int availableWidth, int availableHeight) {
		this.availableWidth = availableWidth;
		this.availableHeight = availableHeight;

		this.maxBlockElementHeight = 0;
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

	int getMaxBlockElementHeight() {
		return maxBlockElementHeight;
	}

	void setMaxBlockElementHeight(int maxBlockElementHeight) {
		this.maxBlockElementHeight = maxBlockElementHeight;
	}
}

