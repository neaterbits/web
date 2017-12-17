package com.test.web.layout.algorithm;

import com.test.web.layout.common.IElementLayout;
import com.test.web.layout.common.enums.Unit;

class LayoutHelperUnits {

	static int computeWidthPx(int width, Unit widthUnit, IElementLayout containerLayout) {
		return computeWidthPx(width, widthUnit, containerLayout.getOuterBounds().getWidth());
	}

	static int computeWidthPx(int width, Unit widthUnit, int curWidth) {
		return computeSizePx(width, widthUnit, curWidth);
	}

	static int computeHeightPx(int width, Unit widthUnit, IElementLayout containerLayout) {
		return computeHeightPx(width, widthUnit, containerLayout.getOuterBounds().getHeight());
	}

	static int computeHeightPx(int height, Unit heightUnit, int curHeight) {
		return computeSizePx(height, heightUnit, curHeight);
	}

	static int computeSizePx(int size, Unit sizeUnit, int curSize) {
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
