package com.test.web.layout;

public interface IElementLayout {

	// Bounds outside wrapping and padding
	IBounds getOuterBounds();
	
	// Bounds of element itself
	IBounds getInnerBounds();
	
	IWrapping getMargins();
	
	IWrapping getPadding();

}
