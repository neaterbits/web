package com.test.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
	
	public static String [] split(String s, char c) {
		final List<String> list = new ArrayList<>();
		
		int lastIdx = 0;

		for (int i = 0; i < s.length(); ++ i) {
			if (s.charAt(i) == c) {
				list.add(s.substring(lastIdx, i));
				lastIdx = i + 1;
			}
		}
		
		if (lastIdx < s.length()) {
			list.add(s.substring(lastIdx));
		}
		else if (lastIdx == s.length()) {
			list.add("");
		}
		
		return list.toArray(new String[list.size()]);
	}

	public static int countTokens(String s) {

	    int numTokens = 0;
	    boolean atToken = false;
	    
	    for (int i = 0; i < s.length(); ++ i) {
	        final char c = s.charAt(i);
	        
	        if (!atToken && !Character.isWhitespace(c)) {
	            if (i == s.length() - 1) {
	                // on-letter char as last entry
	                ++ numTokens;
	            }
	            else {
	                atToken = true;
	            }
	        }
	        else if (atToken && (Character.isWhitespace(c) || i == s.length() - 1)) {
	            ++ numTokens;

	            atToken = false;
	            
	        }
	    }

	    return numTokens;
	}
	
    public static String [] splitToTokens(String s) {
        final List<String> list = new ArrayList<>();
        
        int tokenStartIdx = -1;

        for (int i = 0; i < s.length(); ++ i) {
            final char c = s.charAt(i);
            
            if (tokenStartIdx == -1 && !Character.isWhitespace(c)) {
                if (i == s.length() - 1) {
                    // on-letter char as last entry
                    list.add(s.substring(i, s.length()));
                }
                else {
                    tokenStartIdx = i;
                }
            }
            else if (tokenStartIdx != -1 && Character.isWhitespace(c)) {
                
                list.add(s.substring(tokenStartIdx, i));
                tokenStartIdx = -1;
                
            } else if (tokenStartIdx != -1 && i == s.length() - 1) {
                
                list.add(s.substring(tokenStartIdx, s.length()));
                tokenStartIdx = -1;
            }
        }
        
        return list.toArray(new String[list.size()]);
    }

    public static boolean hasToken(String s, String token) {
        int tokenStartIdx = -1;

        for (int i = 0; i < s.length(); ++ i) {
            final char c = s.charAt(i);
            
            if (tokenStartIdx == -1 && !Character.isWhitespace(c)) {
                if (i == s.length() - 1) {
                    // on-letter char as last entry
                    if (token.equals(s.substring(i, s.length()))) {
                        return true;
                    }
                }
                else {
                    tokenStartIdx = i;
                }
            }
            else if (tokenStartIdx != -1 && Character.isWhitespace(c)) {
                
                if (token.equals(s.substring(tokenStartIdx, i))) {
                    return true;
                }

                tokenStartIdx = -1;
                
            } else if (tokenStartIdx != -1 && i == s.length() - 1) {
                
                if (token.equals(s.substring(tokenStartIdx, s.length()))) {
                    return true;
                }
                tokenStartIdx = -1;
            }
        }
        
        return false;
    }
    
    /**
     * Removes token from input string and also formats output correctly with one space between tokens
     * and not starting or trailing spaces.
     * 
     * If there no tokens are matched, we return null - not a formatted version of this string
     * This is so to make be able to differentiate between change and no change without allocating a
     * separate return object
     * 
     * @return string with remaining tokens, or null if there was no change
     */
    
    public static String removeTokens(String input, String ... tokens) {

        final String [] split = StringUtils.splitToTokens(input);

        int numRemoved = 0;
        
        int sLength = 0;
        
        for (int i = 0; i < split.length; ++ i) {
            boolean thisRemoved = false;
            
            for (String token : tokens) {
                if (split[i].equals(token)) {
                    split[i] = null; // Set to null to mark as remove
                    thisRemoved = true;
                    ++ numRemoved;
                    break;
                }
            }
            
            if (!thisRemoved) {
                sLength += split[i].length() + 1;
            }
        }

        final String result;
        
        if (numRemoved > 0) {
            
            final String newValue;
            
            switch (split.length - numRemoved) {
            case 0:
                newValue = "";
                break;

            case 1:
                String v = null;
                for (int i = 0; i < split.length; ++ i) {
                    if (split[i] != null) {
                        v = split[i];
                        break;
                    }
                }
                
                newValue = v;
                break;
                
            default:
                final StringBuilder sb = new StringBuilder(sLength);

                int appended = 0;
                
                for (int i = 0; i < split.length; ++ i) {
                    
                    final String s = split[i];
                    if (s != null) {
                        if (appended > 0) {
                            sb.append(' ');
                        }
                    
                        sb.append(s);
                        ++ appended;
                    }
                }
                newValue = sb.toString();
                break;
                
            }
            
            result = newValue;
        }
        else {
            result = null;
        }

        return result;
    }

    /**
     * Replaces a token in input with a new token, returning new string if was replaced 
     * 
     * @param input
     * @param oldToken
     * @param newToken
     * 
     * @return replaced string, or null if no replace took place
     */
    
    public static String replaceToken(String input, String oldToken, String newToken) {
        final String [] split = StringUtils.splitToTokens(input);

        int sLength = 0;
        
        boolean replaceOccured = false;
        
        for (int i = 0; i < split.length; ++ i) {
            boolean thisReplaced = false;
            
            if (split[i].equals(oldToken)) {
                split[i] = newToken;
                
                thisReplaced = true;
            }
            
            if (thisReplaced) {
                replaceOccured = true;
                sLength += newToken.length();
            }
            else {
                sLength += oldToken.length();
            }
            
            /// for space
            ++ sLength;
        }

        final String result;
        
        if (replaceOccured) {
            
            final StringBuilder sb = new StringBuilder(sLength);

            addToStringBuilder(sb, split);
            
            result = sb.toString();
        }
        else {
            result = null;
        }

        return result;
    }
    
    private static int addToStringBuilder(StringBuilder sb, String [] split) {
        int appended = 0;
        
        for (int i = 0; i < split.length; ++ i) {
            
            final String s = split[i];
            if (appended > 0) {
                sb.append(' ');
            }
        
            sb.append(s);
            ++ appended;
        }

        return appended;
    }

    private static int findToken(String [] split, String token) {

        for (int i = 0; i < split.length; ++ i) {
            if (split[i].equals(token)) {
                return i;
            }
        }

        return -1;
    }
    
    private static boolean hasToken(String [] split, String token) {
        return findToken(split, token) != -1;
    }
    
    public static boolean toggleToken(String input, StringBuilder output, String token, Boolean force) {

        boolean tokenInResult;
        
        final String [] split = StringUtils.splitToTokens(input);

        if (force != null) {
            if (force) {
                // Always add current, formatted
                final int appended = addToStringBuilder(output, split);
                
                // only add, if token not there
                if (!hasToken(split, token)) {
                    tokenInResult = true;

                    if (appended > 0) {
                        output.append(' ');
                    }

                    output.append(token);
                }
                
                tokenInResult = true;
            }
            else {
                // Always remove
                int appended = 0;
                
                for (int i = 0; i < split.length; ++ i) {
                    final String s = split[i];

                    if (s.equals(token)) {
                        // Token is removed
                        continue;
                    }
                    
                    if (appended > 0) {
                        output.append(' ');
                    }
                
                    output.append(s);
                    ++ appended;
                }
                
                tokenInResult = false;
            }
        }
        else {
            int appended = 0;
            
            boolean removedToken = false;
            
            for (int i = 0; i < split.length; ++ i) {
                final String s = split[i];

                boolean thisTokenRemoved = false;
                
                if (s.equals(token)) {
                    // Token is removed
                    removedToken = true;
                    thisTokenRemoved = true;
                    continue;
                }
                
                if (appended > 0 && !thisTokenRemoved) {
                    output.append(' ');
                }
            
                output.append(s);
                ++ appended;
            }
            
            if (removedToken) {
                tokenInResult = false;
            }
            else {
                // Token was not present so add it
                
                if (appended > 0) {
                    output.append(' ');
                }
                
                output.append(token);
                tokenInResult = true;
            }
        }
        
        return tokenInResult;
    }
    
    public static void parseTokens(String value, Consumer<String> addToken) {
        final String s = value.trim();
        
        if (!s.isEmpty()) {
            int lastIdx = 0;
            
            for (int i = 0; i < s.length(); ++ i) {
                final char c = s.charAt(i);
                
                if (Character.isWhitespace(c)) {
                    addToken.accept(s.substring(lastIdx, i));
                    
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
                addToken.accept(s.substring(lastIdx));
            }
        }
    }
}
