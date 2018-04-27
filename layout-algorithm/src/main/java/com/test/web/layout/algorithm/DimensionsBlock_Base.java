package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IStyleDimensions;
import com.test.web.layout.common.IWrapping;
import com.test.web.layout.common.enums.Display;
import com.test.web.layout.common.enums.Justify;
import com.test.web.layout.common.enums.Unit;
import com.test.web.types.Pixels;

abstract class DimensionsBlock_Base extends DimensionCase {
	
	  static void computeDimensionsFromOuter(
	    		ILayoutStylesGetters layoutStyles,
	    		ContainerDimensions container,
	    		int remainingWidth, int remainingHeight,
	    		ElementLayout resultingLayout) {
		  
		  final int widthPx;
		  
		  if (layoutStyles.hasWidth()) {
			  widthPx = LayoutHelperUnits.computeWidthPx (layoutStyles.getWidth(), layoutStyles.getWidthUnit(), container.getAvailableWidth());
		  }
		  else {
			  widthPx = Pixels.NONE;
		  }

		  final int heightPx;

		  if (layoutStyles.hasHeight()) {
			  heightPx = LayoutHelperUnits.computeHeightPx(layoutStyles.getHeight(), layoutStyles.getHeightUnit(), container.getAvailableHeight());
		  }
		  else {
			  heightPx = Pixels.NONE;
		  }

		  computeDimensionsFromOuter(
				  layoutStyles.getDisplay(),
				  container,
				  remainingWidth, widthPx, layoutStyles.hasWidth(),
				  remainingHeight, heightPx, layoutStyles.hasHeight(),
				  layoutStyles.getMargins(), layoutStyles.getPadding(),
				  resultingLayout);
	  }

	  static void computeDimensionsFromOuter(
	    		Display display,
	    		ContainerDimensions container,
	    		int remainingWidth, int widthPxFromCSS, boolean hasWidthFromCSS,
	    		int remainingHeight, int heightPxFromCSS, boolean hasHeightFromCSS,
	    		IStyleDimensions margins, IStyleDimensions padding,
	    		ElementLayout resultingLayout) {

		  
		  if (!display.isBlock()) {
			  throw new IllegalArgumentException("Expected block display: " + display);
		  }
		  
		  final IWrapping layoutPadding = initPadding(padding, resultingLayout, container.getAvailableWidth(), container.getAvailableHeight());
	    	
	    	// margins can be auto so in that case must know how much space is required by element so that can supply the rest as margins
	    	
	    	final int innerWidth;
	    	final int innerHeight;
	    	
	    	if (hasWidthFromCSS) {
	    		// width is specified in CSS, we use that
	    		innerWidth = widthPxFromCSS;
	    	}
	    	else {
	    		// no width in CSS so we'll use what space is available. For height this is probably the value -1, which means not known
	    		innerWidth = remainingWidth;
	    	}
		
	    	if (hasHeightFromCSS) {
	    	   	innerHeight = heightPxFromCSS;
	    	}
	    	else {
	    		innerHeight = remainingHeight;
	    	}
	 	    	
	    	final int leftPadding = layoutPadding.getLeft();
	    	final int topPadding = layoutPadding.getTop();
	     	final int rightPadding = layoutPadding.getRight();
	    	final int bottomPadding = layoutPadding.getBottom();
	      
	    	
	    	// result is encoded in long to avoid having a separate class for returning values
	    	final long horizontalMargins = computeHorizontalMargins(
	    			margins,
	    			display,
	    			remainingWidth, widthPxFromCSS, hasWidthFromCSS,
	    			leftPadding, rightPadding);
	    	
	    	final int leftMargin = (int)(horizontalMargins >> 32);
	    	final int rightMargin = (int)(horizontalMargins & 0xFFFFFFFFL);
	 	
	 	
			// margins of auto are always set to 0 for vertical case
			final int topMargin  = getNonAutoSize(margins.getTop(), margins.getTopUnit(), margins.getTopType(), remainingWidth);
	  		final int bottomMargin  = getNonAutoSize(margins.getBottom(), margins.getBottomUnit(), margins.getBottomType(), remainingWidth);
	  		
	    	resultingLayout.getMarginWrapping().init(topMargin, rightMargin, bottomMargin, leftMargin);
	       	resultingLayout.getPaddingWrapping().init(topPadding, rightPadding, bottomPadding, leftPadding);
	   
	    	initBounds(
	    			0, // Always starts at 0 since this is a block layout element
	    			container.getCurBlockYPos(),
	    			innerWidth, innerHeight,
	    			resultingLayout.getPadding(), resultingLayout.getMargins(),
	    			resultingLayout);
	    	
	    	// Verify margin and padding calculations
	    	if (margins.getLeftType() == Justify.AUTO || margins.getRightType() == Justify.AUTO) {
	    		if (resultingLayout.getOuter().getWidth() != remainingWidth) {
	    			throw new IllegalStateException("Not all space utiltized when auto margins");
	    		}
	    	}
	  }

	    // Compute horizontal margins and return result as left << 32 || right
  private static long computeHorizontalMargins(
		    		IStyleDimensions margins,
		    		Display display,
		    		int remainingWidth, int widthFromCSS, boolean hasWidthFromCSS,
		    		int leftPadding, int rightPadding) {
		    	
    	final int leftMargin;
    	final int rightMargin;
    	
    	// If margin is auto, we have to compute these.
    	// - if dusplay:block abd width was specified in CSS and rhere is room for margin, it will be set to remaining
    	// - otherwise set to 0
    	if (margins.getLeftType() == Justify.AUTO || margins.getRightType() == Justify.AUTO) {
    		
    		final int paddingWidth = leftPadding + rightPadding;
    		
    		if (hasWidthFromCSS) {
    			
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
    		}
    		else {
    			// left and right margins are 0 since can only use auto for display:block it seems
    			leftMargin 	 = autoToNoneSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
    			rightMargin = autoToNoneSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
    		}
    	}
    	else {
    		// margins can be computed directly since not auto
    		leftMargin  = getNonAutoSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
      		rightMargin  = getNonAutoSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
     	}
    	
    	return ((long)leftMargin) << 32 | rightMargin;
    }
}
