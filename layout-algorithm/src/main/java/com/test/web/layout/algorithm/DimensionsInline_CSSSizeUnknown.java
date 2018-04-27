package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IStyleDimensions;
import com.test.web.layout.common.IWrapping;

final class DimensionsInline_CSSSizeUnknown extends DimensionsInline_Base {
	@Override
	void computeDimensions(ILayoutStylesGetters layoutStyles, ContainerDimensions container, SubDimensions sub,
			ElementLayout resultingLayout) {
		
		final IWrapping padding = initPadding(layoutStyles.getPadding(), resultingLayout, container);

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
	
	private static IWrapping initNoAutoMargins(IStyleDimensions margins, ElementLayout resultingLayout, ContainerDimensions container) {
		return initNoAutoMargins(margins, resultingLayout, container.getAvailableWidth(), container.getAvailableHeight());
	}
	

	private static IWrapping initNoAutoMargins(IStyleDimensions margins, ElementLayout resultingLayout, int containerWidth, int containerHeight) {
		final int topMargin  = autoToNoneSize(margins.getTop(), margins.getTopUnit(), margins.getTopType(), containerHeight);
  		final int rightMargin  = autoToNoneSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), containerWidth);
  		final int bottomMargin  = autoToNoneSize(margins.getBottom(), margins.getBottomUnit(), margins.getBottomType(), containerHeight);
		final int leftMargin  = autoToNoneSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), containerWidth);

		resultingLayout.getMarginWrapping().init(topMargin, rightMargin, bottomMargin, leftMargin);
		
		return resultingLayout.getMargins();
	}
}
