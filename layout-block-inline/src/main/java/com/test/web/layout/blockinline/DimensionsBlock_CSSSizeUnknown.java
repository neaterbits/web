package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.ContainerDimensions;
import com.test.web.layout.algorithm.ElementLayoutSetters;
import com.test.web.layout.algorithm.SubDimensions;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IWrapping;

final class DimensionsBlock_CSSSizeUnknown extends DimensionsBlock_Base {

	@Override
	public void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub, ElementLayoutSetters resultingLayout) {

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
