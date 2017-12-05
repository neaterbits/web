package com.test.web.css.common.enums;

public enum CSSFontWeight {
	
	NORMAL("normal"),
	BOLD("bold"),
	BOLDER("bolder"),
	LIGHTER("lighter"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSFontWeight(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
