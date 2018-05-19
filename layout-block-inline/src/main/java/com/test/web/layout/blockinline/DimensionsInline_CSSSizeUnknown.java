package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.ContainerDimensions;
import com.test.web.layout.algorithm.ElementLayoutSetters;
import com.test.web.layout.algorithm.SubDimensions;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IWrapping;

@Deprecated
final class DimensionsInline_CSSSizeUnknown extends DimensionsInline_Base {
	
	@Override
	public void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub,
			ElementLayoutSetters resultingLayout) {
		
		final IWrapping padding = initPadding(layoutStyles.getPadding(), container, resultingLayout);

		final IWrapping margins = initNoAutoMargins(layoutStyles.getMargins(), resultingLayout, container);
		
		final int contentWidth = sub.getInlineContentMaxWidth();
		final int contentHeight = sub.getInlineContentHeight();

		initBounds(
				container.getLineStartXPos(),
				container.getCurBlockYPos(),
				contentWidth, contentHeight,
				padding, margins,
				resultingLayout);
	}
}
