package com.test.web.layout.algorithm;


/**
 * Base class for all cases where sub element is display: block
 * 
 * We must look for any nested inline elements, like text in orderto start a new block
 *   - at the start of a new block sub-element within a block element, eg <div>some text<div>sub element</div></div>
 *   - at block element end tag, eg. <div>som text that may even wrap over multiple lines</div> 
 *   
 *   For this reason we override onElementStart() and onElementEnd() here as final and call new callbacks
 *   for doing case specific processing so we make sure that this needs to be handled only one place within the code.
 * 
 */

abstract class CaseBlock_Base extends BaseLayoutCase {

	<ELEMENT> void onBlockElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
	}

	<ELEMENT> void onBlockElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
	}

	@Override
	final <ELEMENT> void onElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
		
		// Start of a block element, we should now process any remaining inline elements
		// added so far within the container (not the new sub element)
		// since we are going to the next block in the document
		processAnyPendingInlineElements(container);
		
		// Then call subclass to do computations with regards to this new block element
		onBlockElementStart(container, htmlElement, sub, state);
	}

	@Override
	final <ELEMENT> void onElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
		// Here we might have the second case where there are unprocessed inline elements within the current block
		// that we have to process here. Do that first and then call subclass
		processAnyPendingInlineElements(sub);
		
		onBlockElementEnd(container, htmlElement, sub, state);
	}
	
	private void processAnyPendingInlineElements(StackElement element) {
		
		if (element.hasAnyInlineElementsAdded()) {
			
			// There are unprocessed inline elements in the container, compute height of all and clear
			final int inlineHeight = element.computeInlineHeightAndClearInlineData();
			
			element.addToBlockElementHeight(inlineHeight);
		}
	}
}
