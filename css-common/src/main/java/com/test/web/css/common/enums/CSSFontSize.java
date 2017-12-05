package com.test.web.css.common.enums;

public enum CSSFontSize {
	MEDIUM("medium"),
	XX_SMALL("xx-small"),
	X_SMALL("x-small"),
	SMALL("small"),
	LARGE("large"),
	X_LARGE("x-large"),
	XX_LARGE("xx-large"),
	SMALLER("smaller"),
	LARGER("larger"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	
	private final String name;

	private CSSFontSize(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
