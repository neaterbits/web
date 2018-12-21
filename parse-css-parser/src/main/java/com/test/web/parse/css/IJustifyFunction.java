package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSUnit;

interface IJustifyFunction {

	void onJustify(int size, CSSUnit unit, CSSJustify justify);
	
}
