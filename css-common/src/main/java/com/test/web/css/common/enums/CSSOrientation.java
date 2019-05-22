package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSOrientation implements IEnum {

	PORTRAIT("portrait"),
	LANDSCAPE("landscape");

	private final String name;

	private CSSOrientation(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
