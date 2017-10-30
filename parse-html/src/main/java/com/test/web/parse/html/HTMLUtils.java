package com.test.web.parse.html;

public class HTMLUtils {

    public static String removeNewlines(String input) {
    	StringBuilder sb = null;
    	
    	final int l = input.length();
    	
    	for (int i = 0; i < l; ++ i) {

    		final char c = input.charAt(i);
    		
    		if (c == '\r'  || c == '\n') {
    			if (sb == null) {
    				sb = new StringBuilder();
    				
    				sb.append(input.substring(0, i));
    			}

    			sb.append(' ');
    		}
    		else if (sb != null) {
    			sb.append(c);
    		}
    	}

    	return sb != null ? sb.toString() : input;
    }
    
}
