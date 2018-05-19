package com.test.web.layout.blockinline;

import com.test.web.layout.algorithm.DimensionCase;

class DimensionCases {
	static final DimensionCase BLOCK_CSS_SIZES_KNOWN = new DimensionsBlock_CSSSizeKnown();
	static final DimensionCase BLOCK_CSS_WIDTH_KNOWN = new DimensionsBlock_CSSWidthKnown();
	static final DimensionCase BLOCK_CSS_HEIGHT_KNOWN = new DimensionsBlock_CSSHeightKnown();
	static final DimensionCase BLOCK_CSS_SIZES_UNKNOWN = new DimensionsBlock_CSSSizeUnknown();

	static final DimensionCase INLINE_CSS_SIZES_KNOWN = new DimensionsInline_CSSSizeKnown();
	static final DimensionCase INLINE_CSS_SIZES_UNKNOWN = new DimensionsInline_CSSSizeUnknown();
}
