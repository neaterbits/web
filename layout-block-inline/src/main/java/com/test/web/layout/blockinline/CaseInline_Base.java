package com.test.web.layout.blockinline;

/**
 * Base for all inline elements.
 * 
 * For inline elements we cannot really compute on element start or end since
 *  - css width/height set explicitely is not taken into account, one has to go by size of content
 *  - if multiple elements on one line, height is the tallest element and position of elements
 *    get adjusted according to baseline, so we must at least get a whole line before computing.
 *    
 *    Thus we just handle onElementStart() and onElementEnd() in baseclass so that is handled consistently
 *    for all inline elements.
 */
abstract class CaseInline_Base<ELEMENT> extends BaseBlockInlineLayoutCase<ELEMENT> {

	void onInlineElementStart(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {
		
	}

	void onInlineElementEnd(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {
		
	}

	@Override
	protected final void onElementStart(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		onInlineElementStart(container, htmlElement, sub, state);

	}

	@Override
	protected final void onElementEnd(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		onInlineElementEnd(container, htmlElement, sub, state);

	}
}
