package com.test.web.layout.blockinline;

public final class CaseBlockRoot_CSSSizeUnknown<ELEMENT> extends CaseBlockRoot_Base<ELEMENT> {

	@Override
	void onBlockElementEnd(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {
		
		DimensionCases.BLOCK_CSS_SIZES_UNKNOWN.computeDimensions(sub.getLayoutStyles(), container, sub, sub.resultingLayout);

		container.addToBlockElementHeight(sub.resultingLayout.getOuterBounds().getHeight());
		
	}
}
