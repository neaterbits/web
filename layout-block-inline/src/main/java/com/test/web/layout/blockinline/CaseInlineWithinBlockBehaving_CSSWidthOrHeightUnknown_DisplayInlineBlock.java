package com.test.web.layout.blockinline;

public class CaseInlineWithinBlockBehaving_CSSWidthOrHeightUnknown_DisplayInlineBlock<ELEMENT>
	extends CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_Base<ELEMENT> {

	@Override
	void onInlineElementEnd(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		// TODO we cannot compute absolute or relative position here, we only know width and height since position is dependent
		// on different elements in block-layout. But we do need width and height 
		// We must probably fix that in a separate pass once we have layout of a complete line
		
		state.addInlineElementAndWrapToNextTextLineIfNecessary(
				container,
				sub,
				sub.resultingLayout.getOuterBounds().getWidth(),
				sub.resultingLayout.getOuterBounds().getHeight());
		
		throw new UnsupportedOperationException("TODO");
	}
}
