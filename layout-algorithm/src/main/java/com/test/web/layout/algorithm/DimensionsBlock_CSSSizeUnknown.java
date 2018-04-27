package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IWrapping;

final class DimensionsBlock_CSSSizeUnknown extends DimensionsBlock_Base {

	@Override
	void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub, ElementLayout resultingLayout) {

		// We compute dimensions from the collected height of all block sub-elements since we have no height for this one
		final IWrapping padding = initPadding(layoutStyles.getPadding(), container, resultingLayout);
		final IWrapping margins = initMarginsWhenNoCSSWidth(layoutStyles.getMargins(), container, resultingLayout);
		
		initBounds(
				0, container.getCurBlockYPos(),
				container.getRemainingWidth(), container.getRemainingHeight(),
				padding, margins,
				resultingLayout);
	}
}
