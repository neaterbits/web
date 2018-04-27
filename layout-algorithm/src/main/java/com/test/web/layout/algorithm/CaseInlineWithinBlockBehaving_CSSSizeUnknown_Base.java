package com.test.web.layout.algorithm;

import com.test.web.layout.common.ILayoutState;

abstract class CaseInlineWithinBlockBehaving_CSSSizeUnknown_Base
	extends CaseInlineWithinBlockBehaving_Base {

	@Override
	<ELEMENT>void onElementEnd(StackElement container, ELEMENT element, StackElement sub, ILayoutState state) {
		// Reached element end, at this point we ought to know the size of this element
		// as sub of sub-elements.
		
		// Set width and height to whatever is computed from sub element and add to current
		
		final int inlineHeight = sub.computeInlineHeightAtEndTag();
		
		sub.resultingLayout.getOuter().init(
				container.getLineStartXPos(), // Current line start x pos
				container.getCurBlockYPos(), // Current line start y pos, which is the current inline-pos in this block element
				sub.getInlineMaxWidth(),
				inlineHeight);

		sub.resultingLayout.getInner().init(
				container.getLineStartXPos(), // Current line start x pos
				container.getCurBlockYPos(), // Current line start y pos, which is the current inline-pos in this block element
				sub.getInlineMaxWidth(),
				inlineHeight);

		sub.resultingLayout.setBoundsComputed();
	}
}
