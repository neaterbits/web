package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSHover implements IEnum {
	NONE("none"),
	HOVER("hover");

	private final String name;

	private CSSHover(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
