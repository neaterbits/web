package com.test.web.layout;

abstract class CaseInlineWithinBlockBehaving_CSSSizeUnknown_Base
	extends CaseInlineWithinBlockBehaving_Base {

	@Override
	<ELEMENT>void onElementEnd(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {
		// Reached element end, at this point we ought to know the size of this element
		
	}
}
