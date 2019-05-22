package com.test.web.jsapi.dom.dnd;

import com.neaterbits.util.IEnum;

public enum DataTransferItemKind implements IEnum {

	STRING("string"),
	FILE("file");
	
	private final String name;

	private DataTransferItemKind(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
