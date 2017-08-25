package com.test.web.parse.html.enums;

public enum HTMLDirection {
	LTR("ltr"),
	RTL("rtl"),
	AUTO("auto");
	
	private final String name;

	private HTMLDirection(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
