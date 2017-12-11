package com.test.web.layout;

import com.test.web.css.common.CSSDimensions;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;

// related to computing margins, padding and inner and outer bounds of elements
class LayoutHelperWrappingBounds {
	
	  static void computeDimensionsFromOuter(
	    		CSSDisplay display,
	    		int remainingWidth, int widthFromCSS, boolean hasWidthFromCSS,
	    		int remainingHeight, int heightFromCSS, boolean hasHeightFromCSS,
	    		CSSDimensions margins, CSSDimensions padding,
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
	    		innerWidth = widthFromCSS;
	    	}
	    	else {
	    		// no width in CSS so we'll use what space is available. For height this is probably the value -1, which means not known
	    		innerWidth = remainingWidth;
	    	}
		
	    	if (hasHeightFromCSS) {
	    	   	innerHeight = heightFromCSS;
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
	    			remainingWidth, widthFromCSS, hasWidthFromCSS,
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
	    		CSSDimensions margins,
	    		CSSDisplay display,
	    		int remainingWidth, int widthFromCSS, boolean hasWidthFromCSS,
	    		int leftPadding, int rightPadding) {
	    	
	    	final int leftMargin;
	    	final int rightMargin;
	    	
	    	// If margin is auto, we have to compute these.
	    	// - if dusplay:block abd width was specified in CSS and rhere is room for margin, it will be set to remaining
	    	// - otherwise set to 0
	    	if (margins.getLeftType() == CSSJustify.AUTO || margins.getRightType() == CSSJustify.AUTO) {
	    		
	    		final int paddingWidth = leftPadding + rightPadding;
	    		
	    		if (display == CSSDisplay.BLOCK && hasWidthFromCSS) {
	    			
	    			// We should compute margin from leftover width, but only if had CSS width that was < specified
	    			if (margins.getLeftType() == CSSJustify.AUTO && margins.getRightType() == CSSJustify.AUTO) {
	    				// both left and right margins are auto so split margin size

	    				final int remaining = remainingWidth - (widthFromCSS + paddingWidth);
	    				
	    				final int remainingHalf = remaining / 2;
	    				
	    				leftMargin = remainingHalf + (remaining % 2);
	    				rightMargin = remainingHalf;
	    			}
	    			else if (margins.getLeftType() == CSSJustify.AUTO) {
	    				rightMargin = getNonAutoSize(margins.getRight(), margins.getRightUnit(), margins.getRightType(), remainingWidth);
	    				// left is auto
	    				leftMargin = Math.max(0, remainingWidth - rightMargin - paddingWidth - widthFromCSS);
	    			}
	    			else if (margins.getRightType() == CSSJustify.AUTO) {
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
	    
	    
	    private static int getPaddingSize(int size, CSSUnit unit, CSSJustify type, int curSize) {
	    	return getNonAutoSize(size, unit, type, curSize);
	    }
	    
	    private static int getSizeWithAutoAsZero(int size, CSSUnit unit, CSSJustify type, int curSize) {
	    	return type == CSSJustify.AUTO ? 0 : getNonAutoSize(size, unit, type, curSize);
	    }
	    	    
	    
	    private static int getNonAutoSize(int size, CSSUnit unit, CSSJustify type, int curSize) {
	        
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
