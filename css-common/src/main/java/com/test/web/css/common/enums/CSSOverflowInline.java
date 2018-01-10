package com.test.web.css.common.enums;

import com.test.web.types.IEnum;

public enum CSSOverflowInline implements IEnum {

	NONE("none"),
	SCROLL("scroll");

	private final String name;

	private CSSOverflowInline(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
