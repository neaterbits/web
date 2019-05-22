package com.test.web.css.common.enums;

import com.neaterbits.util.IEnum;

public enum CSSMediaFeature implements IEnum {

	WIDTH("width", CSSMediaFeatureType.RANGE_LENGTH),
	HEIGHT("height", CSSMediaFeatureType.RANGE_LENGTH),
	ASPECT_RATIO("aspect-ratio", CSSMediaFeatureType.RANGE_RATIO),
	ORIENTATION("orientation", CSSOrientation.class),
	RESOLUTION("resolution", CSSMediaFeatureType.RANGE_RESOLUTION),
	SCAN("scan", CSSScan.class),
	GRID("grid", CSSMediaFeatureType.MQ_BOOLEAN),
	UPDATE("update", CSSUpdate.class),
	OVERFLOW_BLOCK("overflow-block", CSSOverflowBlock.class),
	OVERFLOW_INLINE("oveflow-inline", CSSOverflowInline.class),
	COLOR("color", CSSMediaFeatureType.RANGE_INTEGER),
	COLOR_GAMUT("color-gamut", CSSColorGamut.class),
	COLOR_INDEX("color-index", CSSMediaFeatureType.RANGE_INTEGER),
	DISPLAY_MODE("display-mode", CSSDisplayMode.class),
	MONOCHROME("monochrome", CSSMediaFeatureType.RANGE_INTEGER),
	INVERTED_COLORS("inverted-colors", CSSInvertedColors.class),
	POINTER("pointer", CSSPointer.class),
	HOVER("hover", CSSHover.class),
	ANY_POINTER("any-pointer", CSSPointer.class),
	ANY_HOVER("any-hover", CSSHover.class),
	LIGHT_LEVEL("light-level", CSSLightLevel.class),
	SCRIPTING("scripting", CSSScripting.class),
	DEVICE_WIDTH("device-width", CSSMediaFeatureType.RANGE_LENGTH),
	DEVICE_HEIGHT("device-height", CSSMediaFeatureType.RANGE_LENGTH),
	DEVICE_ASPECT_RATIO("device-aspect-ratio", CSSMediaFeatureType.RANGE_RATIO);

	private final String name;
	private final CSSMediaFeatureType type;

	private CSSMediaFeature(String name, CSSMediaFeatureType type) {
		this.name = name;
		this.type = type;
	}

	private CSSMediaFeature(String name, Class<? extends IEnum> enumClas) {
		this.name = name;
		this.type = CSSMediaFeatureType.ENUM;
	}

	@Override
	public String getName() {
		return name;
	}

	public CSSMediaFeatureType getType() {
		return type;
	}
}
