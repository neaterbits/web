package com.test.web.parse.html.enums;

import com.test.web.types.IEnum;

public enum HTMLDropzone implements IEnum {

	NONE("none"),
	MOVE("move"),
	LINK("link");

	private final String name;

	private HTMLDropzone(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
