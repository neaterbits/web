package com.test.web.css.common.enums;

public enum CSSPosition {

	STATIC("static"),
	ABSOLUTE("absolute"),
	FIXED("fixed"),
	RELATIVE("relative"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSPosition(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
