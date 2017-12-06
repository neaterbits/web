package com.test.web.css.common.enums;

public enum CSSBackgroundPosition {

	LEFT_TOP(CSSPositionComponent.LEFT, CSSPositionComponent.TOP),
	LEFT_CENTER(CSSPositionComponent.LEFT, CSSPositionComponent.CENTER),
	LEFT_BOTTOM(CSSPositionComponent.LEFT, CSSPositionComponent.BOTTOM),
	
	RIGHT_TOP(CSSPositionComponent.RIGHT, CSSPositionComponent.TOP),
	RIGHT_CENTER(CSSPositionComponent.RIGHT, CSSPositionComponent.CENTER),
	RIGHT_BOTTOM(CSSPositionComponent.RIGHT, CSSPositionComponent.BOTTOM),
	
	CENTER_TOP(CSSPositionComponent.CENTER, CSSPositionComponent.TOP),
	CENTER_CENTER(CSSPositionComponent.CENTER, CSSPositionComponent.CENTER),
	CENTER_BOTTOM(CSSPositionComponent.CENTER, CSSPositionComponent.BOTTOM),
	
	INITIAL(null, null),
	INHERIT(null, null)
	
	;
	
	private final CSSPositionComponent first;
	private final CSSPositionComponent second;
	
	private CSSBackgroundPosition(CSSPositionComponent first, CSSPositionComponent second) {
		this.first = first;
		this.second = second;
	}

	public CSSPositionComponent getFirst() {
		return first;
	}

	public CSSPositionComponent getSecond() {
		return second;
	}
}
