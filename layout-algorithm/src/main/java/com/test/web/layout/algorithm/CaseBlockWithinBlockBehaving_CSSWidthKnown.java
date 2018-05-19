package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;

final class CaseBlockWithinBlockBehaving_CSSWidthKnown extends CaseBlockWithinBlockBehaving_Base {

	@Override
	<ELEMENT> void onBlockElementStart(StackElement container, ELEMENT element, StackElement sub, LayoutUpdate state) {

		final ILayoutStylesGetters styles = sub.getLayoutStyles();

		final int width = LayoutHelperUnits.computeWidthPx(styles.getWidth(), styles.getWidthUnit(), container.getAvailableWidth());

		// set initially available and remaining width since this is known
		sub.setAvailableWidth(width);
		sub.setRemainingWidth(width);
	}


	// Since we do not know height, we must handle layout upon element end
	@Override
	<ELEMENT> void onBlockElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
		DimensionCases.BLOCK_CSS_WIDTH_KNOWN.computeDimensions(sub.getLayoutStyles(), container, sub, sub.resultingLayout);

		// block within block so will increase container size with what was computed
		container.addToBlockElementHeight(sub.resultingLayout.getOuterBounds().getHeight());
	}
}
