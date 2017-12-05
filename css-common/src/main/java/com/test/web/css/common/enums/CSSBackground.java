package com.test.web.css.common.enums;

public enum CSSBackground {
	
	TRANSPARENT("transparent"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSBackground(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
