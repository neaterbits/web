package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSLightLevel implements IEnum {

	DIM("dim"),
	NORMAL("normal"),
	WASHED("washed");

	private final String name;

	private CSSLightLevel(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
