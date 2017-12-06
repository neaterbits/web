package com.test.web.css.common.enums;

public enum CSSBackgroundColor {
	
	TRANSPARENT("transparent"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSBackgroundColor(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
