package com.test.web.layout.blockinline;

@Deprecated // cannot ussualy have size known for display-inline since setting style size does not have any impact
public class CaseInlineWithinBlockBehaving_SizeKnown_DisplayInline<ELEMENT>
	extends CaseInlineWithinBlockBehaving_SizeKnown_Base<ELEMENT> {

	@Override
	void onInlineElementStart(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub,
			BlockInlineLayoutUpdate<ELEMENT> state) {

		throw new UnsupportedOperationException();
	}

	@Override
	void onInlineElementEnd(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub,
			BlockInlineLayoutUpdate<ELEMENT> state) {

		throw new UnsupportedOperationException();
	}
}
