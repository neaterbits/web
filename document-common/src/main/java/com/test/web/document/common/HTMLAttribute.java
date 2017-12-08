package com.test.web.document.common;

import com.test.web.css.common.enums.CSStyle;
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
	
	ID("id", true, ValueType.ONE),
	CLASS("class", true, ValueType.ONE),
	
	TRANSLATE("translate", true, ValueType.ONE),
	SPELLCHECK("spellcheck", true, ValueType.ONE),
	HIDDEN("hidden", true, ValueType.ONE),
	DRAGGABLE("draggable", true, ValueType.ONE),
	CONTENTEDITABLE("contenteditable", true, ValueType.ONE),
	DROPZONE("dropzone", true, ValueType.ONE),
	DIRECTION("direction", true, ValueType.ONE),
	
	ACCESSKEY("accesskey", true, ValueType.ONE),
	CONTEXTMENU("contextmenu", true, ValueType.ONE),
	
	TITLE("title", true, ValueType.ONE),
	LANG("lang", true, ValueType.ONE),
	TABINDEX("tabindex", true, ValueType.ONE),
	
	STYLE("style", true, CSStyle.class),
	
	REL("rel", false, ValueType.ONE),
	TYPE("type", false, ValueType.ONE),
	HREF("href", false, ValueType.ONE),
	HREFLANG("hreflang", false, ValueType.ONE),
	MEDIA("media", false, ValueType.ONE),
	
	REV("rev", false, ValueType.ONE), // not supported in HTML 5
	
	// style
	SCOPED("scoped", false, ValueType.ONE),
	
	// meta
	CHARSET("charset", false, ValueType.ONE),
	CONTENT("content", false, ValueType.ONE),
	HTTP_EQUIV("http-equiv", false, ValueType.ONE),
	NAME("name", false, ValueType.ONE),
	SCHEME("scheme", false, ValueType.ONE),
	
	XMLNS("xmlns", false, ValueType.ONE),
	XML_LANG("xml:lang", false, ValueType.ONE),
	
	
	// progress element
	MAX("max", false, ValueType.ONE),
	VALUE("value", false, ValueType.ONE)
	
	;
	// 
	private final String name;
	private final boolean global;
	private final ValueType paramType;
	private final Class<? extends IEnum> htmlEnum;
	
	private HTMLAttribute(String name, boolean global, ValueType paramType) {
		this.name = name;
		this.global = global;
		this.paramType = paramType;
		this.htmlEnum = null;
	}
	
	private HTMLAttribute(String name, boolean global, Class<? extends IEnum> enumClass) {
		this.name = name;
		this.global = global;
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
	
	public boolean isGlobal() {
		return global;
	}

	@Override
	public ValueType getValueType() {
		return paramType;
	}
}
