package com.test.web.css.common;

public enum CSSFloat {
	NONE("none"),
	LEFT("left"),
	RIGHT("right"),
	INITIAL("initial"),
	INHERIT("inherit");
	
	private final String name;

	private CSSFloat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
