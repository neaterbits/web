package com.test.web.css.common.enums;

public enum CSSBackgroundRepeat {

	REPEAT("repeat"),
	REPEAT_X("repeat-x"),
	REPEAT_Y("repeat-y"),
	NO_REPEAT("no-repeat"),
	SPACE("space"),
	ROUND("round"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSBackgroundRepeat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
