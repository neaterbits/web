package com.test.web.document.html.oo;

import java.util.function.Consumer;

class ClassAttributeParser {

	static void parseClassAttribute(String value, Consumer<String> addClass) {

		final String s = value.trim();
		
		if (!s.isEmpty()) {
			int lastIdx = 0;
			
			for (int i = 0; i < s.length(); ++ i) {
				final char c = s.charAt(i);
				
				if (Character.isWhitespace(c)) {
					addClass.accept(s.substring(lastIdx, i));
					
					// Skip more whitespace
					for (int j = i + 1; j < s.length(); ++ j) {
						if ( ! Character.isWhitespace(s.charAt(j))) {
							lastIdx = i = j;
							break;
						}
					}
				}
			}

			if (lastIdx != s.length() - 1) {
				addClass.accept(s.substring(lastIdx));
			}
		}
	}
}
