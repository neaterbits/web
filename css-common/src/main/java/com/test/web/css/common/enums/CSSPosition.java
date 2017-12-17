package com.test.web.css.common.enums;

import com.test.web.layout.common.enums.Position;

public enum CSSPosition {

	STATIC("static", Position.STATIC),
	ABSOLUTE("absolute", Position.ABSOLUTE),
	FIXED("fixed", Position.FIXED),
	RELATIVE("relative", Position.RELATIVE),
	INITIAL("initial", null),
	INHERIT("inherit", null);
	
	private final String name;
	private final Position layoutPosition;

	private CSSPosition(String name, Position layoutPosition) {
		this.name = name;
		this.layoutPosition = layoutPosition;
	}

	public String getName() {
		return name;
	}

	public Position getLayoutPosition() {
		return layoutPosition;
	}
}
