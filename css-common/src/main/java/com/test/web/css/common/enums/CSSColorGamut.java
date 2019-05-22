package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSColorGamut implements IEnum {

	SRGB("SRGB"),
	P3("P3"),
	REC2020("REC2020");
	
	private final String name;

	private CSSColorGamut(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
