package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSUnit;

@FunctionalInterface
public interface IMinFunction {

	void onMin(int size, CSSUnit unit, CSSMin type);

}
