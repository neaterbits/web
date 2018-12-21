package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSUnit;

@FunctionalInterface
public interface IMaxFunction {
	
	void onMax(int size, CSSUnit unit, CSSMax type);

}
