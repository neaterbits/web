package com.test.web.layout.algorithm;

/**
 * Whenever container element and current element (eg. in onElementStart()) are both block level elements
 * and the current element has no size set in CSS.
 * 
 * We compute the height of this element as the sum of block-level heights for the sub element and then also add that to the container.
 * Width of this element is set to the width of the container since block elements cover the whole width of the container unless otherwise specified.
 */
class CaseBlockWithinBlockBehaving_CSSSizeUnknown extends CaseBlockWithinBlockBehaving_Base {

	@Override
	<ELEMENT> void onBlockElementEnd(StackElement container, ELEMENT element, StackElement sub, LayoutUpdate state) {

		if (sub.hasUserSpecifiedWidth() || sub.hasUserSpecifiedHeight()) {
			throw new IllegalStateException();
		}

		// The width 
		final int height = sub.getCollectedBlockHeight();

		DimensionCases.BLOCK_CSS_SIZES_UNKNOWN.computeDimensions(sub.layoutStyles, container, sub, sub.resultingLayout);

		// block within block so will increase container size
		container.addToBlockElementHeight(height);
	}
}
