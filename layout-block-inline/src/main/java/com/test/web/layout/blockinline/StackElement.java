package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.ContainerDimensions;
import com.test.web.layout.algorithm.LayoutStackElement;
import com.test.web.layout.algorithm.SubDimensions;
import com.test.web.layout.common.ViewPort;

// Stack element on the layout stack, we add information here at time of start tag
// and then just fetch that information when getting to end tag
// Mutable so can be reused within stack
public final class StackElement extends StackElementBaseBlock implements ContainerDimensions, SubDimensions  {
	
	
	// In order to reuse elements in the stack and avoid memory allocation for each push/pop
	// we keep information specific to inline, text and block etc in the same element
	

	StackElement(int stackIdx, StackElement container, String debugName) {
		this(stackIdx);
		
		init(container, debugName);
	}
	
	StackElement(int stackIdx) {
		super(stackIdx);
	}
	
	@Override
	protected void clear() {
		
		super.clear();
		
		clearBase();
	}

	@Override
    protected void init(LayoutStackElement container, String debugName) {
		super.init(container, debugName);
		
		final StackElement containerElement = (StackElement)container;
		
		final int availableWidth = containerElement.getAvailableWidth();
		final int availableHeight = containerElement.getAvailableHeight();

		init(availableWidth, availableHeight);
	}
	
	
	@Override
	protected void init(ViewPort viewPort, String debugName) {
		super.init(viewPort, debugName);
		
		init(viewPort.getWidth(), viewPort.getHeight());
	}

	private void init(int availableWidth, int availableHeight) {

		initBase();
		
		if (availableWidth == 0)  {
			throw new IllegalArgumentException("availableWidth == 0");
		}

		if (availableHeight == 0) {
			throw new IllegalArgumentException("availableHeight == 0");
		}
		
		initInline();
		initBlock(availableWidth, availableHeight);
	}
}

