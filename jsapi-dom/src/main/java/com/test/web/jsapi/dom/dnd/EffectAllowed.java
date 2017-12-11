package com.test.web.jsapi.dom.dnd;

import com.test.web.types.IEnum;

public enum EffectAllowed implements IEnum {

	NONE("none"),
	COPY("copy"),
	COPY_LINK("copyLink"),
	COPY_MOVE("copyMove"),
	LINK("link"),
	LINK_MOVE("linkMove"),
	MOVE("move"),
	ALL("all"),
	UNINITIALIZED("uninitialized");
	
	private final String name;

	private EffectAllowed(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
