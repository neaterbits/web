package com.test.web.css.common.enums;

import com.test.web.types.IKeyValueList;
import com.test.web.types.ValueType;

/**
 * Style attributes, eg. for size etc
 * @author nhl
 *
 */

public enum CSStyle implements IKeyValueList {
	
	LEFT("left", ValueType.ONE),
	TOP("top", ValueType.ONE),
	
	WIDTH("width", ValueType.ONE),
	HEIGHT("height", ValueType.ONE),
	
	Z_INDEX("z-index", ValueType.ONE),
	
	COLOR("color", ValueType.ONE),
	
	BACKGROUND_IMAGE("background-image", ValueType.ONE),
	BACKGROUND_POSITION("background-position", ValueType.ONE),
	BACKGROUND_SIZE("background-size", ValueType.ONE),
	BACKGROUND_REPEAT("background-repeat", ValueType.ONE),
	BACKGROUND_ATTACHMENT("background-attachment", ValueType.ONE),
	BACKGROUND_ORIGIN("background-origin", ValueType.ONE),
	BACKGROUND_CLIP("background-clip", ValueType.ONE),
	BACKGROUND_COLOR("background-color", ValueType.ONE),
	BACKGROUND("background", ValueType.ONE),
	
	MARGIN("margin", ValueType.ONE),
	MARGIN_LEFT("margin-left", ValueType.ONE),
	MARGIN_RIGHT("margin-right", ValueType.ONE),
	MARGIN_TOP("margin-top", ValueType.ONE),
	MARGIN_BOTTOM("margin-bottom", ValueType.ONE),
	
	PADDING("padding", ValueType.ONE),
	PADDING_LEFT("padding-left", ValueType.ONE),
	PADDING_RIGHT("padding-right", ValueType.ONE),
	PADDING_TOP("padding-top", ValueType.ONE),
	PADDING_BOTTOM("padding-bottom", ValueType.ONE),
	
	DISPLAY("display", ValueType.ONE),
	POSITION("position", ValueType.ONE),
	FLOAT("float", ValueType.ONE),
	CLEAR("clear", ValueType.ONE),
	TEXT_ALIGN("text-align", ValueType.ONE),
	OVERFLOW("overflow", ValueType.ONE),
	
	TEXT_DECORATION("text-decoration", ValueType.ONE),
	
	FONT_FAMILY("font-family", ValueType.ONE),
	FONT_NAME("font-name", ValueType.ONE),
	FONT_SIZE("font-size", ValueType.ONE),
	
	FONT_WEIGHT("font-weight", ValueType.ONE),
	
	MAX_WIDTH("max-width", ValueType.ONE),
	MAX_HEIGHT("max-height", ValueType.ONE),

	MIN_WIDTH("min-width", ValueType.ONE),
	MIN_HEIGHT("min-height", ValueType.ONE)
	;
	
	private final String name;
	private final ValueType valueType;

	private CSStyle(String name, ValueType valueType) {
		this.name = name;
		this.valueType = valueType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ValueType getValueType() {
		return valueType;
	}
}
