package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

/**
 * Methods called to update layout data at various times during layout.
 * 
 * Eg. add <img> ands simiila inline-element to a container element and wrap text-line if reached width of container
 * 
 */

public interface LayoutUpdate extends ILayoutState {
	
	/**
	 * Add <img> or similar inline element
	 * 
	 * @param container container element
	 * @param sub sub element, must be an inline non-container elemenr
	 * @param widthPx width in pixels og inline element (eg. img.width)
	 * @param heightPx height in pixels og inline element (eg. img.height)
	 */

	void addInlineElementAndWrapToNextTextLineIfNecessary(StackElement container, StackElement sub, int widthPx, int heightPx);

	/**
	 *  Add an element that is only a wrapper-element for text, eg. <span> element or an <a> link
	   These only has width and height from wrapped text and we will add text separately
	   They must however still be added to the tree of inline elements so that we can properly traverse the whole tree
	   from the closest block element
	 */
	void addInlineWrapperElementStart(StackElement container, StackElement sub);

	/**
	 * 
	 * @param container container element for wrapper
	 * @param sub wrapper element
	 */
	void addInlineWrapperElementEnd(StackElement container, StackElement sub);
	
	/**
	 *  Called at end of block-element to compute layout for any inline-elements on last line.
	 *  Eg. if is <div>some long text that breaks here. And then more text</div> ,
	 *  we need to call this to make sure we add height of "And then more text".
	 * 
	 * @param sub inline element
	 */
	void applyLineBreakAtEndOfBlockElement(StackElement sub);
}
