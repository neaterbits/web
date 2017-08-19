package com.test.web.css.common;

import com.test.web.types.IKeyValueList;
import com.test.web.types.ValueType;

/**
 * Style attributes, eg. for size etc
 * @author nhl
 *
 */

public enum CSStyle implements IKeyValueList {
	
	WIDTH("width", ValueType.ONE),
	HEIGHT("height", ValueType.ONE),
	
	BACKGOUND_COLOR("background-color", ValueType.ONE),
	FONT_SIZE("font-size", ValueType.ONE)
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
