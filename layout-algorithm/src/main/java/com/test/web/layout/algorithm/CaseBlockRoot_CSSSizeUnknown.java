package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

final class CaseBlockRoot_CSSSizeUnknown extends CaseBlockRoot_Base {

	@Override
	<ELEMENT> void onElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, ILayoutState state) {
		
		LayoutHelperWrappingBounds.computeDimensionsFromOuter(
						sub.layoutStyles,
						state.getViewPort().getWidth(), state.getViewPort().getHeight(), // container width/height
						state.getViewPort().getWidth(), state.getViewPort().getHeight(), // container remaining width/height
						sub.resultingLayout);
	}
}