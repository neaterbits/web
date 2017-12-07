package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSUnit;

class CachedSize {
	private int value;
	private CSSUnit unit;
	
	void init(int value, CSSUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	int getValue() {
		return value;
	}

	CSSUnit getUnit() {
		return unit;
	}
}
