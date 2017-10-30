package com.test.web.layout;

import com.test.web.render.common.IFont;

public interface IElementLayout {

	// Bounds outside wrapping and padding
	IBounds getOuterBounds();
	
	// Bounds of element itself
	IBounds getInnerBounds();
	
	IWrapping getMargins();
	
	IWrapping getPadding();

	IFont getFont();
}
