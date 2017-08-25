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
	
	TRANSLATE("translate", ValueType.ONE),
	SPELLCHECK("spellcheck", ValueType.ONE),
	HIDDEN("hidden", ValueType.ONE),
	DRAGGABLE("draggable", ValueType.ONE),
	CONTENTEDITABLE("contenteditable", ValueType.ONE),
	DROPZONE("dropzone", ValueType.ONE),
	DIRECTION("direction", ValueType.ONE),
	
	ACCESSKEY("accesskey", ValueType.ONE),
	CONTEXTMENU("contextmenu", ValueType.ONE),
	
	TITLE("title", ValueType.ONE),
	LANG("lang", ValueType.ONE),
	TABINDEX("tabindex", ValueType.ONE),
	
	STYLE("style", CSStyle.class),
	
	TYPE("type", ValueType.ONE);
	// 
	private final String name;
	private final ValueType paramType;
	private final Class<? extends IEnum> htmlEnum;
	
	private HTMLAttribute(String name, ValueType paramType) {
		this.name = name;
		this.paramType = paramType;
		this.htmlEnum = null;
	}
	
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
