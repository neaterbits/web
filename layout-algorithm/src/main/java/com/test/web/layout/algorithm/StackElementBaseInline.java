package com.test.web.layout.algorithm;

// Contains all fields for inline-elements only
abstract class StackElementBaseInline extends StackElementBase implements SubDimensions {

	// Max width for inline element, eg. if line did not wrap this will be length of text on first line, otherwise
	// will be length of longest line within wrapped text

	StackElementBaseInline(int stackIdx) {
		super(stackIdx);
	}
	
	final void initInline() {
	}

	private void checkIsInlineElement() {
		if (isViewPort() || !layoutStyles.getDisplay().isInline()) {
			throw new IllegalStateException("Current element is not an inline element");
		}
	}

	@Override
	public final int getInlineContentMaxWidth() {
		checkIsInlineElement();

		throw new UnsupportedOperationException();
		// return inlineMaxWidth;
	}
}