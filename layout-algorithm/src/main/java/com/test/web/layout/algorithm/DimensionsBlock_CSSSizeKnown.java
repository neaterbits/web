package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;

final class DimensionsBlock_CSSSizeKnown extends DimensionsBlock_Base {

	@Override
	void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub, ElementLayout resultingLayout) {

		if ( ! layoutStyles.hasWidth() || ! layoutStyles.hasHeight()) {
			  throw new IllegalArgumentException("CSS dims not known");
		 }
		  
		// Knows sub elements size already, can make some computations
		final int widthPx  = LayoutHelperUnits.computeWidthPx (layoutStyles.getWidth(), layoutStyles.getWidthUnit(), container.getAvailableWidth());
		final int heightPx = LayoutHelperUnits.computeHeightPx(layoutStyles.getHeight(), layoutStyles.getHeightUnit(), container.getAvailableHeight());
		
		computeDimensionsFromOuter(
			layoutStyles.getDisplay(),
			container,
			container.getRemainingWidth(),  widthPx, layoutStyles.hasWidth(),
			container.getRemainingHeight(), heightPx, layoutStyles.hasHeight(),
			layoutStyles.getMargins(), layoutStyles.getPadding(), resultingLayout);
	}
}

