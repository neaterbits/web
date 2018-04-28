package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

abstract class CaseInlineWithinBlockBehaving_CSSSizeUnknown_Base
	extends CaseInlineWithinBlockBehaving_Base {

	@Override
	<ELEMENT>void onElementEnd(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {
		// Reached element end, at this point we ought to know the size of this element
		// as sub of sub-elements.
		
		// Make sure inline height is computed for end-tag
		// TODO does not work to call this here sub.computeInlineHeightAndClearInlineData();
		
		// Set width and height to whatever is computed from sub element and add to current
		DimensionCases.INLINE_CSS_SIZES_UNKNOWN.computeDimensions(
				sub.layoutStyles,
				container,
				sub,
				sub.resultingLayout);
		}
}
