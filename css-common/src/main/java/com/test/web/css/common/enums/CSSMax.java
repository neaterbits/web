package com.test.web.css.common.enums;

import com.test.web.types.IEnum;

public enum CSSMax implements IEnum {
	NONE("none"),
	SIZE("size"),
	INITIAL("initial"),
	INHERIT("inherit");

	private final String name;

	private CSSMax(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
