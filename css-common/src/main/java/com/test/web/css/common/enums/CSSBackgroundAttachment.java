package com.test.web.css.common.enums;

public enum CSSBackgroundAttachment {

	SCROLL("scroll"),
	FIXED("fixed"),
	LOCAL("local"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSBackgroundAttachment(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
