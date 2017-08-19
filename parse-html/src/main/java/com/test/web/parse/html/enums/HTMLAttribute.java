package com.test.web.parse.html.enums;

import com.test.web.css.common.CSStyle;
import com.test.web.css.common.CSSDisplay;
import com.test.web.types.IEnum;
import com.test.web.types.IKeyValue;
import com.test.web.types.IKeyValueList;
import com.test.web.types.IOptionEnum;
import com.test.web.types.ValueType;

/**
 * All known HTML attributes
 * 
 * @author nhl
 *
 */

public enum HTMLAttribute implements IKeyValue {
	
	DISPLAY("display", CSSDisplay.class),
	STYLE("style", CSStyle.class);
	// 
	private final String name;
	private final ValueType paramType;
	private final Class<? extends IEnum> htmlEnum;
	
	private HTMLAttribute(String name, Class<? extends IEnum> enumClass) {
		this.name = name;
		this.htmlEnum = enumClass;
		
		if (IOptionEnum.class.isAssignableFrom(enumClass)) {
			this.paramType = ValueType.ONE;
		}
		else if (IKeyValueList.class.isAssignableFrom(enumClass)) {
			this.paramType = ValueType.MULTIPLE;
		}
		else {
			throw new IllegalStateException("unknown html enum type " + enumClass);
		}
	}
	
	public String getName() {
		return name;
	}

	@Override
	public ValueType getValueType() {
		return paramType;
	}
}
