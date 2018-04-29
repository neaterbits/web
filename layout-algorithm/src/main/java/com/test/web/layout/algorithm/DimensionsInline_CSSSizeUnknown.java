package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IWrapping;

@Deprecated
final class DimensionsInline_CSSSizeUnknown extends DimensionsInline_Base {
	@Override
	void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub,
			ElementLayout resultingLayout) {
		
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
