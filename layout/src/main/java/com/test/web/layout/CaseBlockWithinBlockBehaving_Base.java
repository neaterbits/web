package com.test.web.layout;

import com.test.web.types.Pixels;

abstract class CaseBlockWithinBlockBehaving_Base extends BaseLayoutCase {

	@Override
	<ELEMENT> void onElementStart(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {

		// Try to figure out available space
		if (sub.layoutStyles.hasWidth()) {
			// width in CSS
			initAvailableAndRemainingWidthFromCSS(container.resultingLayout, sub);
		}
		else {
			// constrained by container, for which avaiable is always known
			initAvailableAndRemainingWidthFromContainerRemaining(container, sub);
		}

		if (sub.layoutStyles.hasHeight()) {
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
