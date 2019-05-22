package com.test.web.document.html.common;

import com.neaterbits.util.IEnum;

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
