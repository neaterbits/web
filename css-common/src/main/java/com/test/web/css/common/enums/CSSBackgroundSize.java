package com.test.web.css.common.enums;

public enum CSSBackgroundSize {
	
	AUTO("auto"),
	COVER("cover"),
	CONTAIN("contain"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSBackgroundSize(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
