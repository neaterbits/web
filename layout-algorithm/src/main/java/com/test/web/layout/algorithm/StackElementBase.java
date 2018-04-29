package com.test.web.layout.algorithm;

import com.test.web.layout.common.LayoutStyles;

abstract class StackElementBase {

	private final int stackIdx;
	// Work area for singlethreaded algorithm, storing non threadsafe data here, but should be ok since HTML document parsing happens on one thread
	final LayoutStyles layoutStyles;

	StackElementBase(int stackIdx) {
		this.stackIdx = stackIdx;
		this.layoutStyles = new LayoutStyles();
	}

	final boolean isViewPort() {
		return this.stackIdx == 0;
	}
}
