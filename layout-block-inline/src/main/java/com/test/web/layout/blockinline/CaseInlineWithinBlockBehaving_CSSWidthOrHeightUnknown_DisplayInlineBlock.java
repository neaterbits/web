package com.test.web.layout.blockinline;

public class CaseInlineWithinBlockBehaving_CSSWidthOrHeightUnknown_DisplayInlineBlock
	extends CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_Base {

	@Override
	<ELEMENT> void onInlineElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, BlockInlineLayoutUpdate state) {

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
