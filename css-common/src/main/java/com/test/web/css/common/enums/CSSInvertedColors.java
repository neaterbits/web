package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSInvertedColors implements IEnum {
	
	NONE("none"),
	INVERTED("inverted");

	private final String name;

	private CSSInvertedColors(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
