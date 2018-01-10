package com.test.web.css.common.enums;

import com.test.web.types.IEnum;

public enum CSSUpdate implements IEnum {

	NONE("none"),
	SLOW("slow"),
	FAST("fast");

	private final String name;

	private CSSUpdate(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
