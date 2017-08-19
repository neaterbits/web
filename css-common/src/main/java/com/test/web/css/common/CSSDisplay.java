package com.test.web.css.common;

import com.test.web.types.IOptionEnum;

public enum CSSDisplay implements IOptionEnum {
	INLINE("inline"),
	BLOCK("block"),
	INLINE_BLOCK("inline-block"),
	TABLE("table")

	;

	private final String name;

	private CSSDisplay(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
