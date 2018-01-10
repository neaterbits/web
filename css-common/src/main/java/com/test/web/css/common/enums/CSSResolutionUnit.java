package com.test.web.css.common.enums;

import com.test.web.types.IEnum;

public enum CSSResolutionUnit implements IEnum {

	DPI("dpi"),
	DPCM("dpcm"),
	DPPX("dppx");
	
	private CSSResolutionUnit(String name) {
		this.name = name;
	}

	private final String name;

	@Override
	public String getName() {
		return name;
	}
}
