package com.test.web.css.common.enums;

public enum CSSClear {

	NONE("none"),
	LEFT("left"),
	RIGHT("right"),
	BOTH("both"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSClear(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
