package com.test.web.layout.html;

import com.test.web.layout.algorithm.BaseLayoutCase;
import com.test.web.layout.blockinline.CaseBlockRoot_CSSSizeKnown;
import com.test.web.layout.blockinline.CaseBlockRoot_CSSSizeUnknown;
import com.test.web.layout.blockinline.CaseBlockWithinBlockBehaving_CSSHeightKnown;
import com.test.web.layout.blockinline.CaseBlockWithinBlockBehaving_CSSSizeKnown;
import com.test.web.layout.blockinline.CaseBlockWithinBlockBehaving_CSSSizeUnknown;
import com.test.web.layout.blockinline.CaseBlockWithinBlockBehaving_CSSWidthKnown;
import com.test.web.layout.blockinline.CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock;
import com.test.web.layout.blockinline.CaseInlineWithinBlockBehaving_CSSWidthOrHeightUnknown_DisplayInlineBlock;
import com.test.web.layout.blockinline.CaseInlineWithinBlockBehaving_SizeKnown_DisplayInline;
import com.test.web.layout.blockinline.CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_DisplayInline;
import com.test.web.layout.blockinline.StackElement;
import com.test.web.layout.common.ILayoutStylesGetters;
import com.test.web.layout.common.enums.Display;

class LayoutCases {

	private static final CaseBlockRoot_CSSSizeKnown BLOCK_ROOT_CSS_SIZE_KNOWN = new CaseBlockRoot_CSSSizeKnown();

	private static final CaseBlockRoot_CSSSizeUnknown BLOCK_ROOT_CSS_SIZE_UNKNOWN = new CaseBlockRoot_CSSSizeUnknown();

	private static final CaseInlineWithinBlockBehaving_SizeKnown_DisplayInline INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE
				= new CaseInlineWithinBlockBehaving_SizeKnown_DisplayInline();

	private static final CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_DisplayInline INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE
				= new CaseInlineWithinBlockBehaving_WidthOrHeightUnknown_DisplayInline();
	
	private static final CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN_DISPLAY_INLINE_BLOCK
				= new CaseInlineWithinBlockBehaving_CSSSizeKnown_DisplayInlineBlock();

	private static final CaseInlineWithinBlockBehaving_CSSWidthOrHeightUnknown_DisplayInlineBlock INLINE_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN_DISPLAY_INLINE_BLOCK
				= new CaseInlineWithinBlockBehaving_CSSWidthOrHeightUnknown_DisplayInlineBlock();

	private static final CaseBlockWithinBlockBehaving_CSSSizeKnown BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN
				= new CaseBlockWithinBlockBehaving_CSSSizeKnown();
	
	private static final CaseBlockWithinBlockBehaving_CSSWidthKnown BLOCK_WITHIN_BLOCK_BEHAVING_CSS_WIDTH_KNOWN
				= new CaseBlockWithinBlockBehaving_CSSWidthKnown();

	private static final CaseBlockWithinBlockBehaving_CSSHeightKnown BLOCK_WITHIN_BLOCK_BEHAVING_CSS_HEIGHT_KNOWN
				= new CaseBlockWithinBlockBehaving_CSSHeightKnown();

	private static final CaseBlockWithinBlockBehaving_CSSSizeUnknown BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN
				= new CaseBlockWithinBlockBehaving_CSSSizeUnknown();

	static <ELEMENT_TYPE> BaseLayoutCase<?, ?> determineLayoutCase(StackElement container, ILayoutStylesGetters subLayoutStyles, ELEMENT_TYPE elementType) {

		final BaseLayoutCase<?, ?> ret;

		if (container.isViewPort()) {
			if (subLayoutStyles.getDisplay() != Display.BLOCK) {
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
					if (subLayoutStyles.hasWidth() && subLayoutStyles.hasHeight()) {
						ret = BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_KNOWN;
					}
					else if (subLayoutStyles.hasWidth()) {
						ret = BLOCK_WITHIN_BLOCK_BEHAVING_CSS_WIDTH_KNOWN;
					}
					else if (subLayoutStyles.hasHeight()) {
						ret = BLOCK_WITHIN_BLOCK_BEHAVING_CSS_HEIGHT_KNOWN;
					}
					else {
						ret = BLOCK_WITHIN_BLOCK_BEHAVING_CSS_SIZE_UNKNOWN;
					}
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
