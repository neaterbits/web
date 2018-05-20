package com.test.web.layout.blockinline;

/**
 * Whenever container element and current element (eg. in onElementStart()) are both block level elements
 * and the current element has no size set in CSS.
 * 
 * We compute the height of this element as the sum of block-level heights for the sub element and then also add that to the container.
 * Width of this element is set to the width of the container since block elements cover the whole width of the container unless otherwise specified.
 */
public final class CaseBlockWithinBlockBehaving_CSSSizeUnknown<ELEMENT> extends CaseBlockWithinBlockBehaving_Base<ELEMENT> {

	@Override
	void onBlockElementEnd(StackElement<ELEMENT> container, ELEMENT element, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		if (sub.hasUserSpecifiedWidth() || sub.hasUserSpecifiedHeight()) {
			throw new IllegalStateException();
		}

		// The width 
		final int height = sub.getCollectedBlockHeight();

		DimensionCases.BLOCK_CSS_SIZES_UNKNOWN.computeDimensions(sub.getLayoutStyles(), container, sub, sub.resultingLayout);

		// block within block so will increase container size
		container.addToBlockElementHeight(height);
	}
}
