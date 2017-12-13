package com.test.web.layout;

import com.test.web.render.common.IFont;

public interface IElementLayout {

	// Bounds outside wrapping and padding within document
	IBounds getAbsoluteBounds();

	// Bounds outside wrapping and padding within container
	IBounds getOuterBounds();
	
	// Bounds of element itself
	IBounds getInnerBounds();
	
	boolean areBoundsComputed();
	
	IWrapping getMargins();
	
	IWrapping getPadding();

	IFont getFont();

	int getZIndex();
	
	int getRenderQueueStartOffset();

	int getRenderQueueEndOffset();

}
