package com.test.web.layout.blockinline;

/**
 * For eg. inline <img> elements where size is always known beforehand
 *
 * @param <ELEMENT>
 */


public class CaseInlineWithinBlockBehaving_SizeKnown_DisplayInline<ELEMENT>
	extends CaseInlineWithinBlockBehaving_SizeKnown_Base<ELEMENT> {

	@Override
	void onInlineElementStart(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub,
			BlockInlineLayoutUpdate<ELEMENT> state) {
		
		state.addInlineElementAndWrapToNextTextLineIfNecessary(container, sub, sub.getKnownSizeWidthPx(), sub.getKnownSizeHeightPx());
	}

	@Override
	void onInlineElementEnd(StackElement<ELEMENT> container, ELEMENT htmlElement, StackElement<ELEMENT> sub,
			BlockInlineLayoutUpdate<ELEMENT> state) {

	}
}
