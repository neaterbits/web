package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSMediaType implements IEnum {

	ALL("all"),
	PRINT("print"),
	SCREEN("screen"),
	SPEECH("speech");

	private final String name;

	private CSSMediaType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
