package com.test.web.parse.css;

import com.test.web.parse.common.CharType;

public class CharTypeHTMLElementClass extends CharType {

	public static final CharTypeHTMLElementClass INSTANCE = new CharTypeHTMLElementClass();

	@Override
	public boolean matches(String s) {
		return CharTypeHTMLElementId.isIdentifier(s);
	}
}
