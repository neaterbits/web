package com.test.web.css.common.enums;

public enum CSSOverflow {
	VISIBLE("visible"),
	HIDDEN("hidden"),
	SCROLL("scroll"),
	AUTO("auto"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSOverflow(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
