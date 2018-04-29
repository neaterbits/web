package com.test.web.layout.algorithm;

@Deprecated // cannot ussualy have size known for display-inline since setting style size does not have any impact
class CaseInlineWithinBlockBehaving_SizeKnown_DisplayInline
	extends CaseInlineWithinBlockBehaving_SizeKnown_Base {

	@Override
	<ELEMENT> void onInlineElementStart(StackElement container, ELEMENT htmlElement, StackElement sub,
			LayoutUpdate state) {

		throw new UnsupportedOperationException();
	}

	@Override
	<ELEMENT> void onInlineElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub,
			LayoutUpdate state) {

		throw new UnsupportedOperationException();
	}
}
