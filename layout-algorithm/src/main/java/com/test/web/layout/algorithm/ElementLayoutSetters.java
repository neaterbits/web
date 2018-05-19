package com.test.web.layout.algorithm;

import com.test.web.layout.common.IWrapping;
import com.test.web.render.common.IDelayedRenderer;
import com.test.web.render.common.IFont;

public interface ElementLayoutSetters {

	IWrapping initMargins(int top, int right, int bottom, int left);

	IWrapping initPadding(int top, int right, int bottom, int left);
	
	
	void initOuter(int left, int top, int width, int height);
	
	void initInner(int left, int top, int width, int height);

	void initOuterPosition(int left, int top);
	void initInnerPosition(int left, int top);

	void initOuterWidthHeight(int width, int height);
	void initInnerWidthHeight(int width, int height);
	
	void setRenderer(int zIndex, IDelayedRenderer renderer);

	void setFont(IFont font);
	
	// Call when done initializing all dimensions
	// TODO perhaps just check from different parts, ie. use bitflag
	void setBoundsComputed();
	
}
