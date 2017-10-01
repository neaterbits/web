package com.test.web.css.common.enums;

public enum CSSTextAlign {
	
	LEFT("left"),
	RIGHT("right"),
	CENTER("center"),
	JUSTIFY("justify"),
	INITIAL("unitial"),
	INHERIT("inherit");
	
	private final String name;
	
	private CSSTextAlign(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
