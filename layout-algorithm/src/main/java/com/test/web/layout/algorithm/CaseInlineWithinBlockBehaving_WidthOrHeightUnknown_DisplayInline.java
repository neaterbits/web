package com.test.web.layout.algorithm;

// Width and height is always unknown for eg. span since setting width and height for styling has no effect	
// We rely on the sum of sub-elements instead that are summarized for each line as we go
class CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_DisplayInline
	extends CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_Base {

	@Override
	<ELEMENT> void onInlineElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {

		// Just a wrapper but must still be added to inline-element tree
		state.addInlineWrapperElement(container, sub);
		
	}
}