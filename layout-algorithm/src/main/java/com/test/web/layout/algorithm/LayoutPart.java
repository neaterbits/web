package com.test.web.layout.algorithm;

/**
 * Parts of layout that may have been computed.
 * Used for checking whether we have computed all
 */
enum LayoutPart {

	INNER_POSITION,
	OUTER_POSITION,
	INNER_WIDTH_HEIGHT,
	OUTER_WIDTH_HEIGHT,
	
	COUNT;

	// For quickly checking whether all parts of layout have been set
	static final int ALL_MASK = (1 << COUNT.ordinal()) - 1;
	
	public int flag() {
		return 1 << ordinal();
	}
}
