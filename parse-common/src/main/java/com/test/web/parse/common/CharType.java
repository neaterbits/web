package com.test.web.parse.common;

// A character type, eg letter or whitespace

public abstract class CharType {

	abstract boolean isOfType(char c); 
	
	public boolean matches(String s) {
		
		final int len = s.length();
		
		boolean matches = true;
		
		for (int i = 0; i < len; ++ i) {
			if (!isOfType(s.charAt(i))) {
				matches = false;
				break;
			}
		}

		return matches;
	}
	
}
