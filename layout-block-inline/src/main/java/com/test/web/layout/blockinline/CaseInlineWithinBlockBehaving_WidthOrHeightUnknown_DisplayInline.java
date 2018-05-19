package com.test.web.layout.blockinline;

// Width and height is always unknown for eg. span since setting width and height for styling has no effect	
// We rely on the sum of sub-elements instead that are summarized for each line as we go
public class CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_DisplayInline
	extends CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_Base {

	@Override
	<ELEMENT> void onInlineElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, BlockInlineLayoutUpdate state) {

		// Just a wrapper but must still be added to inline-element tree
		state.addInlineWrapperElementStart(container, sub);
		
	}

	@Override
	<ELEMENT> void onInlineElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, BlockInlineLayoutUpdate state) {

		state.addInlineWrapperElementEnd(container, sub);

	}
}
