package com.test.web.css.common.enums;

public enum CSSForeground {
	INITIAL("initial"),
	INHERIT("inherit");

	private final String name;

	private CSSForeground(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
