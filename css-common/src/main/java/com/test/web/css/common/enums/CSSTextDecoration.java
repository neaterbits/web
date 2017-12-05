package com.test.web.css.common.enums;

public enum CSSTextDecoration {

	NONE("none"),
	UNDERLINE("underline"),
	OVERLINE("overline"),
	LINE_THOUGH("line-through"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSTextDecoration(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
