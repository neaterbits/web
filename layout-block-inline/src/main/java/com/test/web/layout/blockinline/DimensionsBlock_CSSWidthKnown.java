package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.ContainerDimensions;
import com.test.web.layout.algorithm.ElementLayoutSetters;
import com.test.web.layout.algorithm.LayoutHelperUnits;
import com.test.web.layout.algorithm.SubDimensions;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IStyleDimensions;
import com.test.web.layout.common.IWrapping;

public class DimensionsBlock_CSSWidthKnown extends DimensionsBlock_Base {

	@Override
	public void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub,
			ElementLayoutSetters resultingLayout) {

		if ( ! layoutStyles.hasWidth() ||  layoutStyles.hasHeight()) {
			  throw new IllegalArgumentException("CSS dims not matching");
		}
		
		final IStyleDimensions layoutMargins = layoutStyles.getMargins();
		
		final IWrapping padding = initPadding(layoutStyles.getPadding(), container, resultingLayout);
		final IWrapping margins = initMarginsWhenKnownCSSWidth(layoutMargins, container, layoutStyles, padding, resultingLayout);

		// TODO content-box
		final int widthPx  = LayoutHelperUnits.computeWidthPx (layoutStyles.getWidth(), layoutStyles.getWidthUnit(), container.getAvailableWidth());
		
		// This is a block element so get block-height
		final int heightPx = sub.getCollectedBlockHeight();

		initBoundsAndVerifyAutoMargins(layoutMargins, container, 0, container.getCurBlockYPos(), widthPx, heightPx, padding, margins, resultingLayout);
	}
}
