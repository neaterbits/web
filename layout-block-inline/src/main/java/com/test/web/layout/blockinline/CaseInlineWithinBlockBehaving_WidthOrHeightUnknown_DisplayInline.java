package com.test.web.layout.blockinline;

// Width and height is always unknown for eg. span since setting width and height for styling has no effect	
// We rely on the sum of sub-elements instead that are summarized for each line as we go
public class CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_DisplayInline<ELEMENT>
	extends CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_Base<ELEMENT> {

	@Override
	void onInlineElementStart(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		// Just a wrapper but must still be added to inline-element tree
		state.addInlineWrapperElementStart(container, sub);
		
	}

	@Override
	void onInlineElementEnd(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		state.addInlineWrapperElementEnd(container, sub);

	}
}
