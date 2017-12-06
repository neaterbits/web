package com.test.web.css.common.enums;

public enum CSSBackgroundOrigin {
	
	PADDING_BOX("padding-box"),
	BORDER_BOX("border-box"),
	CONTENT_BOX("content-box"),
	
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSBackgroundOrigin(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
