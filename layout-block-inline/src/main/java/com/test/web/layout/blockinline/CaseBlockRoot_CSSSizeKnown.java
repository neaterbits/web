package com.test.web.layout.blockinline;

public final class CaseBlockRoot_CSSSizeKnown extends CaseBlockRoot_Base {

	@Override
	<ELEMENT> void onBlockElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, BlockInlineLayoutUpdate state) {
		// Knows sub elements size already, can make some computations
		DimensionCases.BLOCK_CSS_SIZES_KNOWN.computeDimensions(
			sub.getLayoutStyles(),
			container,
			sub,
	//		state.getViewPort().getWidth(), state.getViewPort().getHeight(), // container width/height
	//		state.getViewPort().getWidth(), state.getViewPort().getHeight(), // container remaining width/height
			sub.resultingLayout);
	}
}
