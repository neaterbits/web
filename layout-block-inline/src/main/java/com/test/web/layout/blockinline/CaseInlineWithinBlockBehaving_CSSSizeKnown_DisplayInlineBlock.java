package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.LayoutHelperUnits;

// inline-block element with known size
public class CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock
	extends CaseInlineWithinBlockBehaving_SizeKnown_Base {

	@Override
	<ELEMENT> void onInlineElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, BlockInlineLayoutUpdate state) {

		final int widthFromCSS = LayoutHelperUnits.computeWidthPx(
				sub.getLayoutStyles().getWidth(),
				sub.getLayoutStyles().getWidthUnit(),
				container);

		final int heightFromCSS = LayoutHelperUnits.computeHeightPx(
				sub.getLayoutStyles().getHeight(),
				sub.getLayoutStyles().getHeightUnit(),
				container);

		// Can add to layout-state in start-tag, will also update remaining width in current block element
		state.addInlineElementAndWrapToNextTextLineIfNecessary(container, sub, widthFromCSS, heightFromCSS);
	}
}