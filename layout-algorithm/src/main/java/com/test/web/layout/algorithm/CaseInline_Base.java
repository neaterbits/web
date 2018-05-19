package com.test.web.layout.algorithm;

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
abstract class CaseInline_Base extends BaseLayoutCase {

	<ELEMENT> void onInlineElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
	}

	<ELEMENT> void onInlineElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
	}

	@Override
	final <ELEMENT> void onElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {

		onInlineElementStart(container, htmlElement, sub, state);

	}

	@Override
	final <ELEMENT> void onElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {

		onInlineElementEnd(container, htmlElement, sub, state);

	}
}
