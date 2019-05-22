package com.test.web.document.html.common;

import com.neaterbits.util.IEnum;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.document.html.common.enums.HTMLDirection;
import com.test.web.document.html.common.enums.HTMLDropzone;
import com.test.web.document.html.common.enums.LinkRelType;
import com.test.web.document.html.common.enums.LinkRevType;
import com.test.web.types.IKeyValue;
import com.test.web.types.IKeyValueList;
import com.test.web.types.IOptionEnum;
import com.test.web.types.ValueArity;

/**
 * All known HTML attributes
 * 
 * @author nhl
 *
 */

public enum HTMLAttribute implements IKeyValue {
	
	ID("id", true, ValueArity.ONE, HTMLAttributeValueType.STRING),
	CLASS("class", true, ValueArity.ONE, HTMLAttributeValueType.STRING_ARRAY),
	
	TRANSLATE("translate", true, ValueArity.ONE, HTMLAttributeValueType.YES_NO, HTMLYesNo.NO.getName()),
	SPELLCHECK("spellcheck", true, ValueArity.ONE, false),
	HIDDEN("hidden", true, ValueArity.ONE, HTMLAttributeValueType.BOOLEAN_MINIMIZABLE, null),
	DRAGGABLE("draggable", true, ValueArity.ONE, false),
	CONTENTEDITABLE("contenteditable", true, ValueArity.ONE, false),
	DROPZONE("dropzone", true, HTMLDropzone.class, HTMLAttributeValueType.ENUM, null),
	DIRECTION("direction", true, HTMLDirection.class, HTMLAttributeValueType.ENUM, null),
	
	ACCESSKEY("accesskey", true, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	CONTEXTMENU("contextmenu", true, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	
	TITLE("title", true, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	LANG("lang", true, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	TABINDEX("tabindex", true, ValueArity.ONE, HTMLAttributeValueType.INTEGER, null),
	
	STYLE("style", true, CSStyle.class, HTMLAttributeValueType.ENUM, null),
	
	REL("rel", false, LinkRelType.class, HTMLAttributeValueType.ENUM, null),
	TYPE("type", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	HREF("href", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	HREFLANG("hreflang", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	MEDIA("media", false, ValueArity.ONE, HTMLAttributeValueType.STRING),
	
	REV("rev", false, LinkRevType.class, HTMLAttributeValueType.ENUM, null), // not supported in HTML 5
	

	// img
	WIDTH("width", false, ValueArity.ONE, HTMLAttributeValueType.INTEGER, null),
	HEIGHT("height", false, ValueArity.ONE, HTMLAttributeValueType.INTEGER, null),
	SRC("src", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	
	DOWNLOAD("download", false, ValueArity.ONE, HTMLAttributeValueType.STRING),
	TARGET("target", false, ValueArity.ONE, HTMLAttributeValueType.ENUM_OR_STRING, null),
	
	// style
	SCOPED("scoped", false, ValueArity.ONE, HTMLAttributeValueType.BOOLEAN_MINIMIZABLE),
	
	// meta
	CHARSET("charset", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	CONTENT("content", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	HTTP_EQUIV("http-equiv", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	NAME("name", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	SCHEME("scheme", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	
	XMLNS("xmlns", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	XML_LANG("xml:lang", false, ValueArity.ONE, HTMLAttributeValueType.STRING, null),
	
	
	// progress element
	MAX("max", false, ValueArity.ONE, HTMLAttributeValueType.BIGDECIMAL, "0.0"),
	VALUE("value", false, ValueArity.ONE, HTMLAttributeValueType.BIGDECIMAL, "0.0")
	
	;
	
	private static final HTMLAttribute [] globalAttributes;
	
	static {
		int numGlobal = 0;
		
		for (HTMLAttribute attribute : HTMLAttribute.values()) {
			if (attribute.isGlobal()) {
				++ numGlobal;
			}
		}
		
		globalAttributes = new HTMLAttribute[numGlobal];

		int idx = 0;

		for (HTMLAttribute attribute : HTMLAttribute.values()) {
			if (attribute.isGlobal()) {
				globalAttributes[idx ++] = attribute;
			}
		}
	}

	public static HTMLAttribute [] getGlobalAttributes() {
		return globalAttributes;
	}

	// 
	private final String name;
	private final boolean global;
	private final ValueArity paramArity;
	private final Class<? extends IEnum> htmlEnum;
	private final HTMLAttributeValueType valueType;
	private final String defaultValue;

	private HTMLAttribute(String name, boolean global, ValueArity paramType, HTMLAttributeValueType valueType) {
		this(name, global,paramType, valueType, null);
	}

	private HTMLAttribute(String name, boolean global, ValueArity paramType, boolean defaultValue) {
		this(name, global,paramType, HTMLAttributeValueType.BOOLEAN_TRUE_FALSE, HTMLStringConversion.booleanString(defaultValue));
	}

	private HTMLAttribute(String name, boolean global, ValueArity paramType, IEnum defaultValue) {
		this(name, global,paramType, HTMLAttributeValueType.ENUM, defaultValue.getName());
	}

	private HTMLAttribute(String name, boolean global, ValueArity paramArity, HTMLAttributeValueType valueType, String defaultValue) {
		this.name = name;
		this.global = global;
		this.paramArity = paramArity;
		this.htmlEnum = null;
		this.valueType = valueType;
		this.defaultValue = defaultValue;
	}

	private HTMLAttribute(String name, boolean global, Class<? extends IEnum> enumClass, HTMLAttributeValueType valueType, String defaultValue) {
		
		if (valueType != HTMLAttributeValueType.ENUM && valueType != HTMLAttributeValueType.ENUM_OR_STRING) {
			throw new IllegalArgumentException("not an enum type: " + valueType);
		}

		this.name = name;
		this.global = global;
		this.htmlEnum = enumClass;
		this.valueType = valueType;
		this.defaultValue = defaultValue;

		if (IKeyValueList.class.isAssignableFrom(enumClass)) {
			this.paramArity = ValueArity.MULTIPLE;
		}
		else if (IEnum.class.isAssignableFrom(enumClass) || IOptionEnum.class.isAssignableFrom(enumClass)) {
			this.paramArity = ValueArity.ONE;
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
	
	public String getAttributeNamespaceURI() {
		return IDocument.DEFAULT_NAMESPACE;
	}
	
	public String getAttributePrefix() {
		return null;
	}

	public String getAttributeName() {
		// same as localname since no prefix
		return getAttributeLocalName();
	}
	
	public String getAttributeLocalName() {
		return name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public ValueArity getValueArity() {
		return paramArity;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class<? extends Enum<?>> getEnumType() {
		return (Class)htmlEnum;
	}
	
	public HTMLAttributeValueType getValueType() {
		return valueType;
	}

	public boolean matches(String name) {
		return getAttributeName().equals(name);
	}

	public boolean matches(String namespaceURI, String localName) {
		return (namespaceURI == null || namespaceURI.equals(getAttributeNamespaceURI()))
				&& localName.equals(getAttributeLocalName());
	}
	
	public static HTMLAttribute find(String name) {
		for (HTMLAttribute attribute : values()) {
			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}
		
		return null;
	}
}
