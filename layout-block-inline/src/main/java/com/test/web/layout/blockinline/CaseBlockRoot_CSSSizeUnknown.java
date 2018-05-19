package com.test.web.layout.blockinline;

public final class CaseBlockRoot_CSSSizeUnknown extends CaseBlockRoot_Base {

	@Override
	<ELEMENT> void onBlockElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, BlockInlineLayoutUpdate state) {
		
		DimensionCases.BLOCK_CSS_SIZES_UNKNOWN.computeDimensions(sub.getLayoutStyles(), container, sub, sub.resultingLayout);

		container.addToBlockElementHeight(sub.resultingLayout.getOuterBounds().getHeight());
		
	}
}
