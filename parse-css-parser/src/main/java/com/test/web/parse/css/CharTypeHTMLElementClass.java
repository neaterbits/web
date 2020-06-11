package com.test.web.parse.css;

import com.neaterbits.util.parse.CharType;

public class CharTypeHTMLElementClass extends CharType {

	public static final CharTypeHTMLElementClass INSTANCE = new CharTypeHTMLElementClass();

	@Override
	public boolean matches(CharSequence s) {
		return CharTypeHTMLElementId.isIdentifier(s);
	}
}
