package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.IStyleDimensions;
import com.test.web.layout.common.IWrapping;
import com.test.web.layout.common.enums.Display;
import com.test.web.layout.common.enums.Justify;
import com.test.web.layout.common.enums.Unit;

// related to computing margins, padding and inner and outer bounds of elements
abstract class DimensionCase {

	/**
	 * Compute dimensions (size and position) when dimensions are specified in CSS, ie. "width: 500px; height: 25%".
	 * Both dimensions must be specified.
	 * This is for block containers.
	 * 
	 * This sets:
	 * - outer bounds relative to container
	 * - inner bounds relative to container
	 * - absolute position in viewport
	 * 
	 * @param layoutStyles getters for obtaining CSS sizes
	 * @param container.containerWidth total width of container element
	 * @param container.containerHeight total height of container height
	 * @param container.containerRemainingWidth width of free space in container, so that margin:auto can be handled properly
	 * @param container.containerRemainingHeight height of free space in container, so that eg. vertical-align: middle can be handled correctly
	 * @param resultingLayout computed information will be stored here
	 */

	abstract void computeDimensions(
		  ILayoutStylesGetters layoutStyles,
		  ContainerDimensions container,
		  SubDimensions sub,
		  ElementLayout resultingLayout);
	
	static IWrapping initPadding(IStyleDimensions padding, ElementLayout resultingLayout, ContainerDimensions container) {
		return initPadding(padding, resultingLayout, container.getAvailableWidth(), container.getAvailableHeight());
	}

	static IWrapping initPadding(IStyleDimensions padding, ElementLayout resultingLayout, int containerWidth, int containerHeight) {
    	final int topPadding 		= getPaddingSize(padding.getTop(), 		padding.getTopUnit(), 		padding.getTopType(),		containerHeight);
    	final int rightPadding 	= getPaddingSize(padding.getRight(), 		padding.getRightUnit(), 		padding.getRightType(),		containerWidth);
    	final int bottomPadding 	= getPaddingSize(padding.getBottom(), 	padding.getBottomUnit(), 	padding.getBottomType(),	containerHeight);
    	final int leftPadding 		= getPaddingSize(padding.getLeft(),			padding.getLeftUnit(), 		padding.getLeftType(),		containerWidth);

    	resultingLayout.getPaddingWrapping().init(topPadding, rightPadding, bottomPadding, leftPadding);

    	return resultingLayout.getPadding();
	}
	    
	    
	    static int getPaddingSize(int size, Unit unit, Justify type, int containerSize) {
	    	return getNonAutoSize(size, unit, type, containerSize);
	    }
	    
	    
	    static void initBounds(int containerLeft, int containerTop, int innerWidth, int innerHeight, IWrapping padding, IWrapping margins, ElementLayout resultingLayout) {

	    	resultingLayout.getOuter().init(
	    			containerLeft,
	    			containerTop,
	    			margins.getLeft() + padding.getLeft() + innerWidth + padding.getRight() + margins.getRight(),
	    			margins.getTop() + padding.getTop() + innerHeight + padding.getBottom() + margins.getBottom());
	    	
	    	resultingLayout.getInner().init(
	    			containerLeft + margins.getLeft() + padding.getLeft() ,
	    			containerTop + margins.getTop() + padding.getTop(),
	    			innerWidth,
	    			innerHeight);
	    	
			resultingLayout.setBoundsComputed();
	    }
	    	    
	    
	    static int getNonAutoSize(int size, Unit unit, Justify type, int curSize) {
	        
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

	    static int autoToNoneSize(int size, Unit unit, Justify type, int curSize) {
			return getNonAutoSize(size, unit, type == Justify.AUTO ? Justify.NONE : type, curSize);
		}
}
