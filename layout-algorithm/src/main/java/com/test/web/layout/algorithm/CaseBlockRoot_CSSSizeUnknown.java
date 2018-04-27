package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

final class CaseBlockRoot_CSSSizeUnknown extends CaseBlockRoot_Base {

	@Override
	<ELEMENT> void onElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, ILayoutState state) {
		
		DimensionCases.BLOCK_CSS_SIZES_UNKNOWN.computeDimensions(sub.layoutStyles, container, sub, sub.resultingLayout);

		container.addToBlockElementHeight(sub.resultingLayout.getOuterBounds().getHeight());
		
	}
}
