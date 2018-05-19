package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.enums.Display;

// Stack element on the layout stack, we add information here at time of start tag
// and then just fetch that information when getting to end tag
// Mutable so can be reused within stack
final class StackElement extends StackElementBaseBlock implements ContainerDimensions, SubDimensions  {
	
	// For debug printouts
	private String debugName;
	
	// The resulting layout after computation of width and height, this is what rendering sees
	final ElementLayout resultingLayout;

	// The layout case for this element, eg an inline-block element within a block element
	private BaseLayoutCase layoutCase;
	
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	

	StackElement(int stackIdx, int availableWidth, int availableHeight, String debugName) {
		this(stackIdx);
		
		init(availableWidth, availableHeight, debugName);
	}
	
	StackElement(int stackIdx) {
		super(stackIdx);

		this.resultingLayout = new ElementLayout();
	}
	
	void clear() {
		
		clearBase();
		
		resultingLayout.clear();
	}

	void init(int availableWidth, int availableHeight, String debugName) {
		
		initBase();
		
		if (availableWidth == 0)  {
			throw new IllegalArgumentException("availableWidth == 0");
		}

		if (availableHeight == 0) {
			throw new IllegalArgumentException("availableHeight == 0");
		}
		
		this.debugName = debugName;

		initInline();
		initBlock(availableWidth, availableHeight);
	}
	

	ElementLayout getResultingLayout() {
		return resultingLayout;
	}
	
	BaseLayoutCase getLayoutCase() {
		return layoutCase;
	}

	String getLayoutCaseName() {
		return layoutCase.getName();
	}

	void setLayoutCase(BaseLayoutCase layoutCase) {
		this.layoutCase = layoutCase;
	}
	
	String getDebugName() {
		return debugName;
	}
}

