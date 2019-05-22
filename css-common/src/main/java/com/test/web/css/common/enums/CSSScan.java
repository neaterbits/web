package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSScan implements IEnum {

	INTERLACE("interlace"),
	PROGRESSIVE("progressive");
	
	private final String name;

	private CSSScan(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
