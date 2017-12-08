package com.test.web.document.common.enums;

public enum Target {

	BLANK("_blank"),
	SELF("_self"),
	PARENT("_parent"),
	TOP("_top");
	
	private final String name;

	private Target(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
