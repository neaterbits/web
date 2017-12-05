package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;

@FunctionalInterface
interface IWrapping<CONTEXT> {
	
	void onWrapping(CONTEXT context,
			int top, CSSUnit topUnit, CSSJustify topType, 
			int right, CSSUnit rightUnit, CSSJustify rightType,
			int bottom, CSSUnit bottomUnit, CSSJustify bottomType,
			int left, CSSUnit leftUnit, CSSJustify leftType);
}
