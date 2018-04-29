package com.test.web.layout.algorithm;

import com.test.web.layout.common.IElementLayout;

// Base class for layout cases
abstract class BaseLayoutCase {

	<ELEMENT> void onElementStart(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
	}

	<ELEMENT> void onElementEnd(StackElement container, ELEMENT htmlElement, StackElement sub, LayoutUpdate state) {
		
	}
	
	<ELEMENT >void onText(StackElement container, StackElement text, LayoutUpdate state) {
		
	}

	final String getName() {
		return getClass().getSimpleName();
	}

	final void initAvailableAndRemainingWidthFromCSS(IElementLayout containerLayout, StackElement sub) {
		final int width  = LayoutHelperUnits.computeWidthPx(
				sub.layoutStyles.getWidth(),
				sub.layoutStyles.getWidthUnit(),
				containerLayout);

		initAvailableAndRemainingFromCSSWidth(sub, width);
	}

	final void initAvailableAndRemainingFromCSSWidth(StackElement sub, int width) {
		sub.setAvailableWidth(width);
		sub.setRemainingWidth(width);
	}

	final void initAvailableAndRemainingWidthFromContainerRemaining(StackElement container, StackElement cur) {
		cur.setAvailableWidth(cur.getRemainingWidth());
		cur.setRemainingWidth(cur.getRemainingWidth());
	}

	final void initAvailableAndRemainingHeightFromCSS(IElementLayout containerLayout, StackElement sub) {
		final int height  = LayoutHelperUnits.computeHeightPx(
				sub.layoutStyles.getHeight(),
				sub.layoutStyles.getHeightUnit(),
				containerLayout);

		initAvailableAndRemainingFromCSSHeight(sub, height);
	}

	final void initAvailableAndRemainingFromCSSHeight(StackElement sub, int height) {
		sub.setAvailableHeight(height);
		sub.setRemainingHeight(height);
	}

	final void initAvailableAndRemainingHeightFromContainerRemaining(StackElement container, StackElement cur) {
		cur.setAvailableHeight(cur.getRemainingHeight());
		cur.setRemainingHeight(cur.getRemainingHeight());
	}
}
