package com.test.web.layout.blockinline;

import com.test.web.types.Pixels;

abstract class CaseBlockWithinBlockBehaving_Base<ELEMENT> extends CaseBlock_Base<ELEMENT> {

	@Override
	void onBlockElementStart(StackElement<ELEMENT> container, ELEMENT element, StackElement<ELEMENT> sub, BlockInlineLayoutUpdate<ELEMENT> state) {

		// Try to figure out available space
		if (sub.getLayoutStyles().hasWidth()) {
			// width in CSS
			initAvailableAndRemainingWidthFromCSS(container.resultingLayout, sub);
		}
		else {
			// constrained by container, for which available is always known
			initAvailableAndRemainingWidthFromContainerRemaining(container, sub);
		}

		if (sub.getLayoutStyles().hasHeight()) {
			initAvailableAndRemainingHeightFromCSS(container.resultingLayout, sub);
		}
		else if (container.hasUserSpecifiedHeight()) {
			// container has height, like for single page applications
			initAvailableAndRemainingHeightFromContainerRemaining(container, sub);
		}
		else {
			// container does not have height, like for a scrollable document
			sub.setAvailableHeight(Pixels.NONE);
		}
	}
}
