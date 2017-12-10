package com.test.web.layout;

class CaseBlockWithinBlockBehaving_CSSSizeUnknown extends CaseBlockWithinBlockBehaving_Base {

	
	@Override
	<ELEMENT> void onElementEnd(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {

		if (sub.hasUserSpecifiedWidth()|| sub.hasUserSpecifiedHeight()) {
			throw new IllegalStateException();
		}
		
		// At this point we know dimensions since we have collected from member elements
		final int width  = sub.getCollectedBlockWidth();
		final int height = sub.getCollectedBlockHeight();

		LayoutHelperWrappingBounds.computeDimensionsFromOuter(
			sub.layoutStyles.getDisplay(),
			container.getRemainingWidth(),  width,  false,
			container.getRemainingHeight(), height, false,
			sub.layoutStyles.getMargins(), sub.layoutStyles.getPadding(), sub.resultingLayout);

		// block within block so will increase container size
		container.addToBlockElementHeight(height);
		
		// no need to add to width since this is a block

		renderBlockElement(element, sub, state);
	}
}
