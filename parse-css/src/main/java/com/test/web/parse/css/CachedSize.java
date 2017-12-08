package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSUnit;
import com.test.web.types.DecimalSize;

class CachedSize {
	private int value;
	private CSSUnit unit;
	
	public CachedSize() {
		clear();
	}
	
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
	
	void clear() {
		this.value = DecimalSize.NONE;
		this.unit = null;
	}
}
