package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;

final class DimensionsBlock_CSSSizeUnknown extends DimensionsBlock_Base {

	@Override
	void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub, ElementLayout resultingLayout) {

		// We compute dimensions from the collected height of all block sub-elements since we have no height for this one
		computeDimensionsFromOuter(
				layoutStyles,
				container,
				container.getRemainingWidth(), container.getRemainingHeight(),
				resultingLayout);

	}
}
