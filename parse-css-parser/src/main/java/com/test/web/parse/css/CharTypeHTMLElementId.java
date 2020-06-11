package com.test.web.parse.css;

import com.neaterbits.util.parse.CharType;

public class CharTypeHTMLElementId extends CharType {

	public static final CharTypeHTMLElementId INSTANCE = new CharTypeHTMLElementId();

	@Override
	public boolean matches(CharSequence s) {
		return isIdentifier(s);
	}
	
	private static boolean isIdLetter(char c) {
		return Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '-';
	
	}
	
	static boolean isIdentifier(CharSequence s) {
		boolean matches;
		
		if (!Character.isLetter(s.charAt(0))) {
			matches = false;
		}
		else {
			matches = true;
			
			for (int i = 1; i < s.length(); ++ i) {
				final char c = s.charAt(i);
				if (!isIdLetter(c)) {
					matches = false;
					break;
				}
			}
		}
		
		return matches;
	}
	
}
