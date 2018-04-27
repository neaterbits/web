package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

/**
 * Whenever container element and current element (eg. in onElementStart()) are both block level elements
 * and the current element has no size set in CSS.
 * 
 * We compute the height of this element as the sum of block-level heights for the sub element and then also add that to the container.
 * Width of this element is set to the width of the container since block elements cover the whole width of the container unless otherwise specified.
 */
class CaseBlockWithinBlockBehaving_CSSSizeUnknown extends CaseBlockWithinBlockBehaving_Base {

	@Override
	<ELEMENT> void onElementEnd(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {

		if (sub.hasUserSpecifiedWidth() || sub.hasUserSpecifiedHeight()) {
			throw new IllegalStateException();
		}

		final int width  =  container.getAvailableWidth();

		// The width 
		final int height = sub.getCollectedBlockHeight();

		LayoutHelperWrappingBounds.computeDimensionsFromOuter(
			sub.layoutStyles.getDisplay(),
			container.getRemainingWidth(),  width,  false,
			container.getRemainingHeight(), height, false,
			sub.layoutStyles.getMargins(), sub.layoutStyles.getPadding(), sub.resultingLayout);

		// block within block so will increase container size
		container.addToBlockElementHeight(height);
	}
}
