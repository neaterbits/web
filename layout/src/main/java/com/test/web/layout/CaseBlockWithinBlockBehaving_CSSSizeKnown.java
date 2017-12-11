package com.test.web.layout;

class CaseBlockWithinBlockBehaving_CSSSizeKnown extends CaseBlockWithinBlockBehaving_Base {

	@Override
	<ELEMENT>void onElementStart(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {

		// Knows sub elements size already, can make some computations
		final int width  = LayoutHelperUnits.computeWidthPx (sub.layoutStyles.getWidth(), sub.layoutStyles.getWidthUnit(), container.resultingLayout);
		final int height = LayoutHelperUnits.computeHeightPx(sub.layoutStyles.getHeight(), sub.layoutStyles.getHeightUnit(), container.resultingLayout);

		LayoutHelperWrappingBounds.computeDimensionsFromOuter(
			sub.layoutStyles.getDisplay(),
			container.getRemainingWidth(),  width,  sub.layoutStyles.hasWidth(),
			container.getRemainingHeight(), height, sub.layoutStyles.hasHeight(),
			sub.layoutStyles.getMargins(), sub.layoutStyles.getPadding(), sub.resultingLayout);

		// set initially available and remaining
		sub.setAvailableWidth(width);
		sub.setRemainingWidth(width);
		sub.setAvailableHeight(height);
		sub.setRemainingHeight(height);

		// block within block so will increase container size
		container.addToBlockElementHeight(height);
	}
}
