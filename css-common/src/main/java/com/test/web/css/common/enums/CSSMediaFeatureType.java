package com.test.web.css.common.enums;

public enum CSSMediaFeatureType {

	RANGE_LENGTH(true),
	RANGE_RATIO(true),
	RANGE_RESOLUTION(true),
	RANGE_INTEGER(true),
	ENUM(false),
	MQ_BOOLEAN(false);

	private final boolean rangeType;

	private CSSMediaFeatureType(boolean rangeType) {
		this.rangeType = rangeType;
	}

	public boolean isRangeType() {
		return rangeType;
	}
}
