package com.test.web.types;

public class StringUtils {
	public static boolean equals(String s1, String s2) {
		final boolean equals;
		
		if (s1 == null && s2 == null) {
			equals = true;
		}
		else if (s1 == null || s2 == null) {
			equals = false;
		}
		else {
			equals = s1.equals(s2);
		}

		return equals;
	}
	
	public static String trimToNull(String s) {
		
		final String trimmed;

		if (s == null) {
			trimmed = null;
		}
		else if (s.isEmpty()) {
			trimmed = null;
		}
		else if ( ! Character.isWhitespace(s.charAt(0)) && ! Character.isWhitespace(s.charAt(s.length() - 1)) ) {
			trimmed = s;
		}
		else {
			final String s2 = s.trim();
			
			trimmed = s2.isEmpty() ? null : s2;
		}
		
		return trimmed;
	}

	public static Integer asIntegerOrNull(String value) {
		Integer integer;

		final String trimmed = value.trim();

		if (trimmed.isEmpty()) {
			integer = null;
		}
		else {
			try {
				integer = Integer.parseInt(value);
			}
			catch (NumberFormatException ex) {
				integer = null;
			}
		}

		return integer;
	}
}
