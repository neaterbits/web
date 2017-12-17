package com.test.web.layout;

import com.test.web.css.common.CSSLayoutStyles;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.document.html.common.HTMLElement;

class LayoutCases {

	private static final CaseBlockRoot_CSSSizeKnown BLOCK_ROOT_CSS_SIZE_KNOWN = new CaseBlockRoot_CSSSizeKnown();

	private static final CaseBlockRoot_CSSSizeUnknown BLOCK_ROOT_CSS_SIZE_UNKNOWN = new CaseBlockRoot_CSSSizeUnknown();

	private static final CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInline INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE
				= new CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInline();

	private static final CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInline INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE
				= new CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInline();
	
	private static final CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE_BLOCK
				= new CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock();

	private static final CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInlineBlock INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE_BLOCK
				= new CaseInlineWithinBlockBehaving_CSSSizeUnknown_DisplayInlineBlock();

	private static final CaseBlockWithinBlockBehaving_CSSSizeKnown BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN
				= new CaseBlockWithinBlockBehaving_CSSSizeKnown();
	
	private static final CaseBlockWithinBlockBehaving_CSSSizeUnknown BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN
				= new CaseBlockWithinBlockBehaving_CSSSizeUnknown();

	static BaseLayoutCase determineLayoutCase(StackElement container, CSSLayoutStyles subLayoutStyles, HTMLElement elementType) {

		final BaseLayoutCase ret;

		if (container.isViewPort()) {
			if (subLayoutStyles.getDisplay() != CSSDisplay.BLOCK) {
				throw new UnsupportedOperationException("TODO - support other than block root element");
			}
			
			if (subLayoutStyles.hasWidth() && subLayoutStyles.hasHeight()) {
				ret = BLOCK_ROOT_CSS_SIZE_KNOWN;
			}
			else {
				ret = BLOCK_ROOT_CSS_SIZE_UNKNOWN;
			}
		}
		else {
			
			if (container.getDisplay() == null) {
				throw new IllegalStateException("container display not set for " + elementType);
			}
		
			switch (container.getDisplay()) {
			case BLOCK:
			case INLINE_BLOCK:
				final boolean hasDimensionsFromCSS = subLayoutStyles.hasWidth() && subLayoutStyles.hasHeight();
	
				switch (subLayoutStyles.getDisplay()) {
				case INLINE:
					ret = hasDimensionsFromCSS
						? INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE
		    			: INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE;
					break;
	
				case INLINE_BLOCK:
					ret = hasDimensionsFromCSS
	    				? INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE_BLOCK
	    				: INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE_BLOCK;
					break;
				
				case BLOCK:
					ret = hasDimensionsFromCSS
						? BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN
						: BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN;
					break;
	
				default:
					throw new IllegalArgumentException("Unknown sub display type: " + container.getDisplay());
				}
				break;
				
			default:
				throw new IllegalArgumentException("Unknown container display type: " + container.getDisplay());
			}
		}

		return ret;
	}
}
