package com.test.web.document.common;

public enum HTMLAttributeValueType {
	STRING,
	STRING_ARRAY,
	BOOLEAN_TRUE_FALSE,
	BOOLEAN_MINIMIZABLE,
	YES_NO,
	INTEGER,
	DECIMAL, // encoded as 32 bit
	BIGDECIMAL, // larger decimals
	ENUM,
	ENUM_OR_STRING,
	CSS;
}
