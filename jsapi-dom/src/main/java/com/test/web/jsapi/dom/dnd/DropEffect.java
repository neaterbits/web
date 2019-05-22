package com.test.web.jsapi.dom.dnd;

import com.neaterbits.util.IEnum;

public enum DropEffect implements IEnum {
	NONE("none"),
	COPY("copy"),
	LINK("link"),
	MOVE("move");
	
	private final String name;

	private DropEffect(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
