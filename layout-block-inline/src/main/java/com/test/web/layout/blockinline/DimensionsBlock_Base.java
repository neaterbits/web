package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.ContainerDimensions;
import com.test.web.layout.algorithm.DimensionCase;
import com.test.web.layout.algorithm.ElementLayoutSetters;
import com.test.web.layout.algorithm.LayoutHelperUnits;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IStyleDimensions;
import com.test.web.layout.common.IWrapping;
import com.test.web.layout.common.enums.Justify;

abstract class DimensionsBlock_Base extends DimensionCase {
  	  
  	  private static long encodeHorizontalMargin(int leftMargin, int rightMargin) {
  		  return ((long)leftMargin) << 32 | rightMargin;
  	  }

	private static long computeHorizontalMarginsWhenHasWidthFromCSS(
    		IStyleDimensions margins,
    		int remainingWidth, int widthFromCSS,
    		int leftPadding, int rightPadding) {

		final long encoded;

		if (margins.getLeftType() == Justify.AUTO || margins.getRightType() == Justify.AUTO) {
			encoded = computeHorizontalMarginsWhenHasWidthFromCSSAndAutoMargins(margins, remainingWidth, widthFromCSS, leftPadding, rightPadding);
    	}
    	else {
    		final int leftMargin;
    		final int rightMargin;

    		// margins can be computed directly since not auto
    		leftMargin  = getNonAutoSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
      		rightMargin  = getNonAutoSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
      		
      		encoded = encodeHorizontalMargin(leftMargin, rightMargin);
     	}

		return encoded;
	}

  	private static long computeHorizontalMarginsWhenHasWidthFromCSSAndAutoMargins(
    		IStyleDimensions margins,
    		int remainingWidth, int widthFromCSS,
    		int leftPadding, int rightPadding) {

		final int paddingWidth = leftPadding + rightPadding;
		
		final int leftMargin;
		final int rightMargin;

		// We should compute margin from leftover width, but only if had CSS width that was < specified
		if (margins.getLeftType() == Justify.AUTO && margins.getRightType() == Justify.AUTO) {
			// both left and right margins are auto so split margin size

			final int remaining = remainingWidth - (widthFromCSS + paddingWidth);
			
			final int remainingHalf = remaining / 2;
			
			leftMargin = remainingHalf + (remaining % 2);
			rightMargin = remainingHalf;
		}
		else if (margins.getLeftType() == Justify.AUTO) {
			rightMargin = getNonAutoSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
			// left is auto
			leftMargin = Math.max(0, remainingWidth - rightMargin - paddingWidth - widthFromCSS);
		}
		else if (margins.getRightType() == Justify.AUTO) {
			leftMargin = getNonAutoSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
			// right is auto
			rightMargin = Math.max(0, remainingWidth - leftMargin - paddingWidth - widthFromCSS);
		}
		else {
			throw new IllegalStateException("Either left or right should be auto");
		}
		
		
		return encodeHorizontalMargin(leftMargin, rightMargin);
  	}

  	static IWrapping initMarginsWhenKnownCSSWidth(IStyleDimensions margins, ContainerDimensions container, ILayoutStylesGetters styles, IWrapping padding, ElementLayoutSetters resultingLayout) {
  		
  		final int widthFromCSS = LayoutHelperUnits.computeWidthPx(styles.getWidth(), styles.getWidthUnit(), container.getAvailableWidth());
  		
  		final long encoded = computeHorizontalMarginsWhenHasWidthFromCSS(margins, container.getRemainingWidth(), widthFromCSS, padding.getLeft(), padding.getRight());
  		
    	final int leftMargin = (int)(encoded >> 32);
    	final int rightMargin = (int)(encoded & 0xFFFFFFFFL);
  		
		final int topMargin = getNonAutoSize(margins.getTop(), margins.getTopUnit(), margins.getTopType(), container.getAvailableHeight());
  		final int bottomMargin = getNonAutoSize(margins.getBottom(), margins.getBottomUnit(), margins.getBottomType(), container.getAvailableHeight());
    	
  		return resultingLayout.initMargins(topMargin, rightMargin, bottomMargin, leftMargin);
  	}

  	static IWrapping initMarginsWhenNoCSSWidth(IStyleDimensions margins, ContainerDimensions container, ElementLayoutSetters resultingLayout) {
  		// Does not have CSS width, becomes simpler since cannot have margins: auto centering
  		return initNoAutoMargins(margins, resultingLayout, container);
  	}

    public static void initBoundsAndVerifyAutoMargins(IStyleDimensions layoutMargins, ContainerDimensions container,
    		int containerLeft, int containerTop, int innerWidth, int innerHeight, IWrapping padding, IWrapping margins,
    		ElementLayoutSetters resultingLayout) {
    	
    	final int outerWidth = initBounds(containerLeft, containerTop, innerWidth, innerHeight, padding, margins, resultingLayout);

    	// Verify margin and padding calculations
    	if (layoutMargins.getLeftType() == Justify.AUTO || layoutMargins.getRightType() == Justify.AUTO) {
    		if (outerWidth != container.getRemainingWidth()) {
    			throw new IllegalStateException("Not all space utiltized when auto margins");
    		}
    	}

  	}
}
  	
