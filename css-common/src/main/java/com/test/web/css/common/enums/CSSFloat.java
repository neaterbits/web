package com.test.web.css.common.enums;

import com.test.web.layout.common.enums.LayoutFloat;

public enum CSSFloat {
	NONE("none", LayoutFloat.NONE),
	LEFT("left", LayoutFloat.LEFT),
	RIGHT("right", LayoutFloat.RIGHT),
	INITIAL("initial", null),
	INHERIT("inherit", null);
	
	private final String name;
	private final LayoutFloat layoutFloat;

	private CSSFloat(String name, LayoutFloat layoutFloat) {
		this.name = name;
		this.layoutFloat = layoutFloat;
	}

	public String getName() {
		return name;
	}

	public LayoutFloat getLayoutFloat() {
		return layoutFloat;
	}
}
