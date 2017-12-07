package com.test.web.css.common.enums;

public enum CSSFilter {

	NONE("none"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSFilter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
