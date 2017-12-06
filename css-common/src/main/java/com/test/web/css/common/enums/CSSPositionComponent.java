package com.test.web.css.common.enums;

public enum CSSPositionComponent {

	TOP("top"),
	RIGHT("right"),
	BOTTOM("bottom"),
	LEFT("left"),
	CENTER("center");
	
	private final String name;

	private CSSPositionComponent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
