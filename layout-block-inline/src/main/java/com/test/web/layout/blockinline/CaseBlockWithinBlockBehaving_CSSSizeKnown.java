package com.test.web.layout.blockinline;

public class CaseBlockWithinBlockBehaving_CSSSizeKnown<ELEMENT> extends CaseBlockWithinBlockBehaving_Base<ELEMENT> {

	@Override
	void onBlockElementStart(StackElement<ELEMENT> container, ELEMENT element, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		// Knows sub elements size already, can make some computations
		
		DimensionCases.BLOCK_CSS_SIZES_KNOWN.computeDimensions(sub.getLayoutStyles(), container, sub, sub.resultingLayout);

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
