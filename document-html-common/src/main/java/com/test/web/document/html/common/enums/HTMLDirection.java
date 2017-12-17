package com.test.web.document.html.common.enums;

import com.test.web.types.IEnum;

public enum HTMLDirection implements IEnum {
	LTR("ltr"),
	RTL("rtl"),
	AUTO("auto");
	
	private final String name;

	private HTMLDirection(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
