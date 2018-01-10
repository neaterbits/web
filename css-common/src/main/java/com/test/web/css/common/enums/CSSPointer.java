package com.test.web.css.common.enums;

import com.test.web.types.IEnum;

public enum CSSPointer implements IEnum {

	NONE("none"),
	COARSE("coarse"),
	FINE("fine");
	
	private final String name;

	private CSSPointer(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
