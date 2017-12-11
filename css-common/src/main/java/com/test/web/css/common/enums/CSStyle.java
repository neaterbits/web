package com.test.web.css.common.enums;

import com.test.web.types.IKeyValueList;
import com.test.web.types.ValueArity;

/**
 * Style attributes, eg. for size etc
 * @author nhl
 *
 */

public enum CSStyle implements IKeyValueList {
	
	LEFT("left", ValueArity.ONE),
	TOP("top", ValueArity.ONE),
	
	WIDTH("width", ValueArity.ONE),
	HEIGHT("height", ValueArity.ONE),
	
	Z_INDEX("z-index", ValueArity.ONE),
	
	COLOR("color", ValueArity.ONE),
	
	BACKGROUND_IMAGE("background-image", ValueArity.ONE),
	BACKGROUND_POSITION("background-position", ValueArity.ONE),
	BACKGROUND_SIZE("background-size", ValueArity.ONE),
	BACKGROUND_REPEAT("background-repeat", ValueArity.ONE),
	BACKGROUND_ATTACHMENT("background-attachment", ValueArity.ONE),
	BACKGROUND_ORIGIN("background-origin", ValueArity.ONE),
	BACKGROUND_CLIP("background-clip", ValueArity.ONE),
	BACKGROUND_COLOR("background-color", ValueArity.ONE),
	BACKGROUND("background", ValueArity.ONE),
	
	MARGIN("margin", ValueArity.ONE),
	MARGIN_LEFT("margin-left", ValueArity.ONE),
	MARGIN_RIGHT("margin-right", ValueArity.ONE),
	MARGIN_TOP("margin-top", ValueArity.ONE),
	MARGIN_BOTTOM("margin-bottom", ValueArity.ONE),
	
	PADDING("padding", ValueArity.ONE),
	PADDING_LEFT("padding-left", ValueArity.ONE),
	PADDING_RIGHT("padding-right", ValueArity.ONE),
	PADDING_TOP("padding-top", ValueArity.ONE),
	PADDING_BOTTOM("padding-bottom", ValueArity.ONE),
	
	DISPLAY("display", ValueArity.ONE),
	POSITION("position", ValueArity.ONE),
	FLOAT("float", ValueArity.ONE),
	CLEAR("clear", ValueArity.ONE),
	TEXT_ALIGN("text-align", ValueArity.ONE),
	OVERFLOW("overflow", ValueArity.ONE),
	
	TEXT_DECORATION("text-decoration", ValueArity.ONE),
	
	FONT_FAMILY("font-family", ValueArity.ONE),
	FONT_NAME("font-name", ValueArity.ONE),
	FONT_SIZE("font-size", ValueArity.ONE),
	
	FONT_WEIGHT("font-weight", ValueArity.ONE),
	
	MAX_WIDTH("max-width", ValueArity.ONE),
	MAX_HEIGHT("max-height", ValueArity.ONE),

	MIN_WIDTH("min-width", ValueArity.ONE),
	MIN_HEIGHT("min-height", ValueArity.ONE),
	
	FILTER("filter", ValueArity.ONE)
	;
	
	private final String name;
	private final ValueArity valueType;

	private CSStyle(String name, ValueArity valueType) {
		this.name = name;
		this.valueType = valueType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ValueArity getValueArity() {
		return valueType;
	}
}
