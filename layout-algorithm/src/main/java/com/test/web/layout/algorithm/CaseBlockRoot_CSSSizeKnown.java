package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

final class CaseBlockRoot_CSSSizeKnown extends CaseBlockRoot_Base {

	@Override
	<ELEMENT> void onElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, ILayoutState state) {
		// Knows sub elements size already, can make some computations

		LayoutHelperWrappingBounds.computeDimensionsFromKnownCSSDims(
			sub.layoutStyles,
			state.getViewPort().getWidth(), state.getViewPort().getHeight(), // container width/height
			state.getViewPort().getWidth(), state.getViewPort().getHeight(), // container remaining width/height
			sub.resultingLayout);
	}

}
