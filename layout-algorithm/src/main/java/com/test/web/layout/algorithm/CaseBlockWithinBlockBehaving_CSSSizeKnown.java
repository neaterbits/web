package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

class CaseBlockWithinBlockBehaving_CSSSizeKnown extends CaseBlockWithinBlockBehaving_Base {

	@Override
	<ELEMENT>void onBlockElementStart(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {

		// Knows sub elements size already, can make some computations
		
		DimensionCases.BLOCK_CSS_SIZES_KNOWN.computeDimensions(sub.layoutStyles, container, sub, sub.resultingLayout);

		final int width = sub.resultingLayout.getInnerBounds().getWidth();
		final int height = sub.resultingLayout.getInnerBounds().getHeight();

		// set initially available and remaining
		sub.setAvailableWidth(width);
		sub.setRemainingWidth(width);
		sub.setAvailableHeight(height);
		sub.setRemainingHeight(height);

		// block within block so will increase container size
		container.addToBlockElementHeight(height);
	}
}
