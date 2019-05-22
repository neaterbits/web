package com.test.web.document.html.common.enums;

import com.neaterbits.util.IEnum;

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
