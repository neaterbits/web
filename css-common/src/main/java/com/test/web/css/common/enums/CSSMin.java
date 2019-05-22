package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSMin implements IEnum {
	SIZE("size"),
	INITIAL("initial"),
	INHERIT("inherit");

	private final String name;

	private CSSMin(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
