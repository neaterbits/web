package com.test.web.document.html.common.enums;

import com.neaterbits.util.IEnum;

public enum HTMLTarget implements IEnum {

	BLANK("_blank"),
	SELF("_self"),
	PARENT("_parent"),
	TOP("_top");
	
	private final String name;

	private HTMLTarget(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
