package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

public abstract class CaseInlineWithinBlockBehaving_CSSSizeKnown_Base
	extends CaseInlineWithinBlockBehaving_Base {

	@Override
	<ELEMENT>void onElementStart(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {
		// add to current textline
		final int width  = LayoutHelperUnits.computeWidthPx (sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), container.resultingLayout);
		final int height = LayoutHelperUnits.computeHeightPx(sub.layoutStyles.getHeight(), sub.layoutStyles.getHeightUnit(), container.resultingLayout);

		LayoutHelperWrappingBounds.computeDimensionsFromOuter(
			sub.layoutStyles.getDisplay(),
			container.getRemainingWidth(),  width,  sub.layoutStyles.hasWidth(),
			container.getRemainingHeight(), height, sub.layoutStyles.hasHeight(),
			sub.layoutStyles.getMargins(), sub.layoutStyles.getPadding(), sub.resultingLayout);

		boolean atStartOfLine = container.hasAnyInlineElementsAdded();
		
		// TODO handle case where there is no room for element, the atStartOfLine may be set to true because we had to wrap
		// to the next line, or we have to look at overflow flag
		
		// Add to textline and wrap and render if necessary
		if (width > container.getRemainingWidth()) {
			// No room on current textline so continue on next
		}

		container.addInlineElement(sub.resultingLayout, atStartOfLine);
	}
}
