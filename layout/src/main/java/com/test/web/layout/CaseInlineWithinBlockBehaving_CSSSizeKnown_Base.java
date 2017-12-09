package com.test.web.layout;

public abstract class CaseInlineWithinBlockBehaving_CSSSizeKnown_Base
	extends CaseInlineWithinBlockBehaving_Base {

	@Override
	void onElementStart(StackElement container, StackElement sub) {
		// add to current textline
		final int width  = LayoutHelperUnits.computeWidthPx (sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), container.resultingLayout);
		final int height = LayoutHelperUnits.computeHeightPx(sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), container.resultingLayout);

		LayoutHelperWrappingBounds.computeDimensionsFromOuter(
			sub.layoutStyles.getDisplay(),
			container.getRemainingWidth(),  width,  sub.layoutStyles.hasWidth(),
			container.getRemainingHeight(), height, sub.layoutStyles.hasHeight(),
			sub.layoutStyles.getMargins(), sub.layoutStyles.getPadding(), sub.resultingLayout);

		// Add to textline and wrap and render if necessary
		if (width > container.getRemainingWidth()) {
			// No room on current textline so just render what we got and then add
			renderCurrentTextLine();
		}

		container.addInlineElement(sub.resultingLayout);
	}
}
