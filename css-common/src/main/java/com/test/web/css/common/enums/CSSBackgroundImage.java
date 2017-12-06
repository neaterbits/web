package com.test.web.css.common.enums;

public enum CSSBackgroundImage {

	NONE("none"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSBackgroundImage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
