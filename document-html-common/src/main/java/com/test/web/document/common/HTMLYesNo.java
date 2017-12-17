package com.test.web.document.common;

import com.test.web.types.IEnum;

public enum HTMLYesNo implements IEnum {

	YES("yes"),
	NO("no");
	
	private final String name;

	private HTMLYesNo(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}