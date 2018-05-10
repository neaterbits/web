package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

public interface LayoutUpdate extends ILayoutState {

	void addInlineElementAndWrapToNextTextLineIfNecessary(StackElement container, StackElement sub, int widthPx, int heightPx);

	// Add an element that is only a wrapper-element for text, eg. <span> element or an <a> link
	// These only has width and height from wrapped text and we will add text separately
	// They must however still be added to the tree of inline elements so that we can properly traverse the whole tree
	// from the closest block element
	void addInlineWrapperElementStart(StackElement container, StackElement sub);

	// End of wrapper-element
	void addInlineWrapperElementEnd(StackElement container, StackElement sub);
	
	// Called at end of block-element to compute layout for any inline-elements on last line
	void applyLineBreakAtEndOfBlockElement(StackElement sub);
}
