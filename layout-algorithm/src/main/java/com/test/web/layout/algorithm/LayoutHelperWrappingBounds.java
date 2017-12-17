package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IStyleDimensions;
import com.test.web.layout.common.enums.Display;
import com.test.web.layout.common.enums.Justify;
import com.test.web.layout.common.enums.Unit;
import com.test.web.types.Pixels;

// related to computing margins, padding and inner and outer bounds of elements
class LayoutHelperWrappingBounds {

	  static void computeDimensionsFromKnownCSSDims(ILayoutStylesGetters layoutStyles,
			  int containerWidth, int containerHeight,
			  int containerRemainingWidth, int containerRemainingHeight,
			  ElementLayout resultingLayout) {
		  
		 if ( ! layoutStyles.hasWidth() || ! layoutStyles.hasHeight()) {
			  throw new IllegalArgumentException("CSS dims not known");
		 }
		  
		// Knows sub elements size already, can make some computations
		final int widthPx  = LayoutHelperUnits.computeWidthPx (layoutStyles.getWidth(), layoutStyles.getWidthUnit(), containerWidth);
		final int heightPx = LayoutHelperUnits.computeHeightPx(layoutStyles.getHeight(), layoutStyles.getHeightUnit(), containerHeight);
		
		LayoutHelperWrappingBounds.computeDimensionsFromOuter(
			layoutStyles.getDisplay(),
			containerRemainingWidth,  widthPx, layoutStyles.hasWidth(),
			containerRemainingHeight, heightPx, layoutStyles.hasHeight(),
			layoutStyles.getMargins(), layoutStyles.getPadding(), resultingLayout);
		  
	  }

	  static void computeDimensionsFromOuter(
	    		ILayoutStylesGetters layoutStyles,
				int containerWidth, int containerHeight,
	    		int remainingWidth, int remainingHeight,
	    		ElementLayout resultingLayout) {
		  
		  final int widthPx;
		  final boolean hasCSSWidth;
		  
		  if (layoutStyles.hasWidth()) {
			  widthPx = LayoutHelperUnits.computeWidthPx (layoutStyles.getWidth(), layoutStyles.getWidthUnit(), containerWidth);
			  hasCSSWidth = true;
		  }
		  else {
			  widthPx = Pixels.NONE;
			  hasCSSWidth = false;
		  }

		  final int heightPx;
		  final boolean hasCSSHeight;

		  if (layoutStyles.hasHeight()) {
			  heightPx = LayoutHelperUnits.computeHeightPx(layoutStyles.getHeight(), layoutStyles.getHeightUnit(), containerHeight);
			  hasCSSHeight = true;
		  }
		  else {
			  heightPx = Pixels.NONE;
			  hasCSSHeight = false;
		  }
		  

		  computeDimensionsFromOuter(
				  layoutStyles.getDisplay(),
				  remainingWidth, layoutStyles.getWidth(), layoutStyles.hasWidth(),
				  remainingHeight, layoutStyles.getHeight(), layoutStyles.hasHeight(),
				  layoutStyles.getMargins(), layoutStyles.getPadding(),
				  resultingLayout);
	  }

	  static void computeDimensionsFromOuter(
	    		Display display,
	    		int remainingWidth, int widthPxFromCSS, boolean hasWidthFromCSS,
	    		int remainingHeight, int heightPxFromCSS, boolean hasHeightFromCSS,
	    		IStyleDimensions margins, IStyleDimensions padding,
	    		ElementLayout resultingLayout) {
	    	
	    	final int topPadding 		= getPaddingSize(padding.getTop(), 		padding.getTopUnit(), 		padding.getTopType(),		remainingHeight);
	    	final int rightPadding 	= getPaddingSize(padding.getRight(), 		padding.getRightUnit(), 		padding.getRightType(),		remainingWidth);
	    	final int bottomPadding 	= getPaddingSize(padding.getBottom(), 	padding.getBottomUnit(), 	padding.getBottomType(),	remainingHeight);
	    	final int leftPadding 		= getPaddingSize(padding.getLeft(),			padding.getLeftUnit(), 		padding.getLeftType(),		remainingWidth);

	    	resultingLayout.getPaddingWrapping().init(topPadding, rightPadding, bottomPadding, leftPadding);
	    	
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
	 	    	
	    	int innerLeft = leftPadding;
	    	int innerTop = topPadding;
	    	
	    	// result is encoded in long to avoid having a separate class for returning values
	    	final long horizontalMargins = computeHorizontalMargins(
	    			margins,
	    			display,
	    			remainingWidth, widthPxFromCSS, hasWidthFromCSS,
	    			leftPadding, rightPadding);
	    	
	    	final int leftMargin = (int)(horizontalMargins >> 32);
	    	final int rightMargin = (int)(horizontalMargins & 0xFFFFFFFFL);
	 	
	  		innerLeft += leftMargin;
	 	
			// margins of auto are always set to 0 for horizontal case
			final int topMargin  = getNonAutoSize(margins.getTop(), margins.getTopUnit(), margins.getTopType(), remainingWidth);
	  		final int bottomMargin  = getNonAutoSize(margins.getBottom(), margins.getBottomUnit(), margins.getBottomType(), remainingWidth);
	  		
	  		innerTop += topMargin;
	    	
	    	resultingLayout.getInner().init(innerLeft, innerTop, innerWidth, innerHeight);
	    
	    	resultingLayout.getMarginWrapping().init(topMargin, rightMargin, bottomMargin, leftMargin);
	       	resultingLayout.getPaddingWrapping().init(topPadding, rightPadding, bottomPadding, leftPadding);
	   
	     	// Outer width and outer height
	    	resultingLayout.getOuter().setWidth(innerWidth + leftPadding + rightPadding + leftMargin + rightMargin);
	    	
	    	if (innerHeight != -1) {
	    		resultingLayout.getOuter().setHeight(innerHeight + topPadding + bottomPadding + topMargin + bottomMargin);
	    		resultingLayout.setBoundsComputed();
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
	    		
	    		if (display == Display.BLOCK && hasWidthFromCSS) {
	    			
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
	    			leftMargin 	 = getSizeWithAutoAsZero(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
	    			rightMargin = getSizeWithAutoAsZero(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
	    		}
	    	}
	    	else {
	    		// margins can be computed directly since not auto
	    		leftMargin  = getNonAutoSize(margins.getLeft(), margins.getLeftUnit(), margins.getLeftType(), remainingWidth);
	      		rightMargin  = getNonAutoSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
	     	}
	    	
	    	return ((long)leftMargin) << 32 | rightMargin;
	    }
	    
	    
	    private static int getPaddingSize(int size, Unit unit, Justify type, int curSize) {
	    	return getNonAutoSize(size, unit, type, curSize);
	    }
	    
	    private static int getSizeWithAutoAsZero(int size, Unit unit, Justify type, int curSize) {
	    	return type == Justify.AUTO ? 0 : getNonAutoSize(size, unit, type, curSize);
	    }
	    	    
	    
	    private static int getNonAutoSize(int size, Unit unit, Justify type, int curSize) {
	        
	    	final int ret;
	    	
	    	switch (type) {
	    	case NONE:
	    		ret = 0;
	    		break;
	    		
	    	case SIZE:
	    		ret = LayoutHelperUnits.computeSizePx(size, unit, curSize);
	    		break;
	    		
	    	default:
	    		throw new UnsupportedOperationException("Unknown type " + type);
	    	}
	    	
	    	return ret;
	    }
}
