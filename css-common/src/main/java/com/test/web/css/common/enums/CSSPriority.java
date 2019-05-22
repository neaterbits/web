package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSPriority implements IEnum {

	IMPORTANT("important");
	
	private final String name;

	private CSSPriority(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
