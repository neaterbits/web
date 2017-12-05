package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSColor;

@FunctionalInterface
public interface ICSSColorFunction {
	void onColor(CSSColor color);
}
