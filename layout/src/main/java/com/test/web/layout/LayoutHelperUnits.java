package com.test.web.layout;

import com.test.web.css.common.enums.CSSUnit;

class LayoutHelperUnits {

	static int computeWidthPx(int width, CSSUnit widthUnit, IElementLayout containerLayout) {
		return computeWidthPx(width, widthUnit, containerLayout.getOuterBounds().getWidth());
	}

	static int computeWidthPx(int width, CSSUnit widthUnit, int curWidth) {
		return computeSizePx(width, widthUnit, curWidth);
	}

	static int computeHeightPx(int width, CSSUnit widthUnit, IElementLayout containerLayout) {
		return computeHeightPx(width, widthUnit, containerLayout.getOuterBounds().getHeight());
	}

	static int computeHeightPx(int height, CSSUnit heightUnit, int curHeight) {
		return computeSizePx(height, heightUnit, curHeight);
	}

	static int computeSizePx(int size, CSSUnit sizeUnit, int curSize) {
		final int ret;
		
		switch (sizeUnit) {
		case PX:
			ret = size;
			break;
			
		case EM:
			ret = pxFromEm(size);
			break;
			
		case PCT:
			ret = curSize == -1 ? -1 : percentOf(curSize, size);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown unit " + sizeUnit);
		}
		
		return ret;
	}
	
	private static int pxFromEm(int em) {
		// TODO
		return em;
	}
	
	private static int percentOf(int px, int pct) {
		return (px * pct) / 100; 
	}

}
