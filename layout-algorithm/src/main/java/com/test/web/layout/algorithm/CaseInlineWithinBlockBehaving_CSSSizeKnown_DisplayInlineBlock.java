package com.test.web.layout.algorithm;

// inline-block element with known size
class CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock
	extends CaseInlineWithinBlockBehaving_SizeKnown_Base {

	@Override
	<ELEMENT> void onInlineElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {

		final int widthFromCSS = LayoutHelperUnits.computeWidthPx(
				sub.layoutStyles.getWidth(),
				sub.layoutStyles.getWidthUnit(),
				container);

		final int heightFromCSS = LayoutHelperUnits.computeHeightPx(
				sub.layoutStyles.getHeight(),
				sub.layoutStyles.getHeightUnit(),
				container);

		// Can add to layout-state in start-tag, will also update remaining width in current block element
		state.addInlineElementAndWrapIfNecessary(container, sub, widthFromCSS, heightFromCSS);
	}
}
