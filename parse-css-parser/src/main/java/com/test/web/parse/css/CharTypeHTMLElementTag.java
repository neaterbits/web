package com.test.web.parse.css;

import com.neaterbits.util.parse.CharType;

public class CharTypeHTMLElementTag extends CharType {
	
	public static final CharTypeHTMLElementTag INSTANCE = new CharTypeHTMLElementTag();

	@Override
	public boolean matches(String s) {
		boolean matches;
		
		if (!isASCIILetter(s.charAt(0))) {
			matches = false;
		}
		else {
			matches = true;
			
			for (int i = 1; i < s.length(); ++ i) {
				final char c = s.charAt(i);
				if (!isASCIILetter(c) && !Character.isDigit(c)) {
					matches = false;
					break;
				}
			}
		}
		
		return matches;
	}
}
