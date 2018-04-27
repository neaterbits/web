package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IStyleDimensions;
import com.test.web.layout.common.IWrapping;

final class DimensionsBlock_CSSSizeKnown extends DimensionsBlock_Base {

	@Override
	void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub, ElementLayout resultingLayout) {

		if ( ! layoutStyles.hasWidth() || ! layoutStyles.hasHeight()) {
			  throw new IllegalArgumentException("CSS dims not known");
		}
		
		final IStyleDimensions layoutMargins = layoutStyles.getMargins();
		
		final IWrapping padding = initPadding(layoutStyles.getPadding(), container, resultingLayout);
		final IWrapping margins = initMarginsWhenKnownCSSWidth(layoutMargins, container, layoutStyles, padding, resultingLayout);

		// TODO content-box
		final int widthPx  = LayoutHelperUnits.computeWidthPx (layoutStyles.getWidth(), layoutStyles.getWidthUnit(), container.getAvailableWidth());
		final int heightPx = LayoutHelperUnits.computeHeightPx(layoutStyles.getHeight(), layoutStyles.getHeightUnit(), container.getAvailableHeight());

		initBoundsAndVerifyAutoMargins(layoutMargins, container, 0, container.getCurBlockYPos(), widthPx, heightPx, padding, margins, resultingLayout);
	}
}

