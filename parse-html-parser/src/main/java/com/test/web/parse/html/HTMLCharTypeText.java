package com.test.web.parse.html;

import com.test.web.parse.common.CharType;

final class HTMLCharTypeText extends CharType {

	static final HTMLCharTypeText INSTANCE = new HTMLCharTypeText();
	
	private HTMLCharTypeText() {
		
	}

	@Override
	protected boolean isOfType(char c) {
		return c != '<';
	}
}
