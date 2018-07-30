package com.test.web.document.html.oo;

import java.util.function.Consumer;

import com.test.web.util.StringUtils;

class ClassAttributeParser {

	static void parseClassAttribute(String value, Consumer<String> addClass) {
	    StringUtils.parseTokens(value, addClass);
	}
}
