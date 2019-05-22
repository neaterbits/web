package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSOverflowBlock implements IEnum {

	NONE("none"),
	SCROLL("scroll"),
	OPTIONAL_PAGED("optional-paged"),
	PAGED("paged");
	
	private final String name;

	private CSSOverflowBlock(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
