package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSScripting implements IEnum {

	NONE("none"),
	INITIAL_ONLY("initial-only"),
	ENABLED("enabled");
	
	private final String name;

	private CSSScripting(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
