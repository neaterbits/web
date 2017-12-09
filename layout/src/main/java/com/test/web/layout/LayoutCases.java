package com.test.web.layout;

import com.test.web.css.common.CSSLayoutStyles;

class LayoutCases {

	private static final CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInline DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE
				= new CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInline();

	private static final CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInline DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE
				= new CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInline();
	
	private static final CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE_BLOCK
				= new CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock();

	private static final CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInlineBlock DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE_BLOCK
				= new CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInlineBlock();

	static BaseLayoutCase determineLayoutCase(StackElement container, CSSLayoutStyles subLayoutStyles) {
		
		final BaseLayoutCase ret;
		
		switch (container.getDisplay()) {
		case BLOCK:
		case INLINE_BLOCK:
			final boolean hasDimensionsFromCSS = subLayoutStyles.hasWidth() && subLayoutStyles.hasHeight();

			switch (subLayoutStyles.getDisplay()) {
			case INLINE:
				ret = hasDimensionsFromCSS
					? DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE
	    			: DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE;
				break;

			case INLINE_BLOCK:
				ret = hasDimensionsFromCSS
    				? DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE_BLOCK
    				: DISPLAY_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE_BLOCK;
				break;

		default:
				throw new IllegalArgumentException("Unknown sub display type: " + container.getDisplay());
			}
			break;
			
		default:
			throw new IllegalArgumentException("Unknown container display type: " + container.getDisplay());
		}

		return ret;
	}
}
