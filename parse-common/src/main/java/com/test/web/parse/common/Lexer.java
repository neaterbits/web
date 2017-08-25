package com.test.web.parse.common;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import com.test.web.io.common.CharInput;

public final class Lexer<TOKEN extends Enum<TOKEN> & IToken, INPUT extends CharInput> {

	private static final boolean DEBUG = false;

	private static final String PREFIX = "Lexer: ";

	private final INPUT input;
	private final TOKEN tokNone;
	private final TOKEN tokEOF;
	
	private final StringBuilder cur;
	private int buffered;
	
	// Scratch array for maintaining number of matching tokens at any given type
	private final int [] matchingTokens;
	
	private int lineNo;
	
	// For debug
	private TOKEN lastToken;
	
	
	public Lexer(INPUT input, Class<TOKEN> tokenClass, TOKEN tokNone, TOKEN tokEOF) {
		
		if (input == null) {
			throw new IllegalArgumentException("inputStream == null");
		}
		
		if (tokNone == null) {
			throw new IllegalArgumentException("tokNone == null");
		}
		
		this.input = input;
		
		this.matchingTokens = new int[tokenClass.getEnumConstants().length];
		
		this.tokNone = tokNone;
		this.tokEOF = tokEOF;
		this.cur = new StringBuilder();
		this.buffered = -1;
		this.lineNo = 1;
	}
	
	
	public TOKEN lex(TOKEN ... tokens) throws IOException {
		
		if (DEBUG) {
			System.out.println("----");
			System.out.println(PREFIX + " lex(" + Arrays.toString(tokens) + ")");
		}
		
		if (tokens.length > matchingTokens.length) {
			throw new IllegalArgumentException("tokens.length > matchingTokens.length");
		}
		
		cur.setLength(0);
		
		// Scan all tokens for input from reader and check whether any tokens match 
		
		TOKEN found = null;

		// Read input until finds matching token
		// TODO: exit if cannot find matching token of any length
		do {
			final int val = read();
			
			if (val < 0) {
				
				if (tokEOF != null) {
					for (TOKEN token : tokens) {
						if (token == tokEOF) {
							found = tokEOF;
							break;
						}
					}
				}
				
				if (found == null) {
					throw new EOFException("Reached EOF");
				}
			}
			
			final char c = (char)val;
			
			if (c == '\n') {
				++ lineNo;
			}
			
			cur.append(c);
			
			int numPossibleMatch = 0;
			
			// Loop through all tokens to see if any match
			for (int i = 0; i < tokens.length; ++ i) {
				// Check whether tokens match
				final TOKEN token = tokens[i];
				
				if (DEBUG) {
					System.out.println(PREFIX + " Matching token " + token + " to \"" + cur + "\"");
				}
				
				final boolean match;
				
				switch (token.getTokenType()) {
				case CHARACTER:
					if (cur.length() == 1) {
						match = c == token.getCharacter();

						// No point in incrementing since if we matched we exit
						// ++ numPossibleMatch;
					}
					else {
						match = false;
					}
					break;
					
				case FROM_CHAR_TO_CHAR:
					if (cur.charAt(0) != token.getFromCharacter()) {
						match = false;
					}
					else if (cur.length() >= 2 && c == token.getToCharacter() ){
						match = true;
					}
					else {
						++ numPossibleMatch;
						match = false;
					}
					break;
					
				case FROM_STRING_TO_STRING:
					if (cur.length() <= token.getFromLiteral().length()) {
						match = false;
						
						if (token.getFromLiteral().startsWith(cur.toString())) {
							++ numPossibleMatch;
						}
					}
					else if (cur.length() >= (token.getFromLiteral().length() + token.getToLiteral().length())) {
						final String s = cur.toString();
						
						if (s.startsWith(token.getFromLiteral())) {
							if (s.endsWith(token.getToLiteral())) {
								match = true;
							}
							else {
								++ numPossibleMatch;
								match = false;
							}
						}
						else {
							match = false;
						}
					}
					else {
						// cur length is > from literal length but less than sum
						final String s = cur.toString();
						
						match = false;
						
						if (s.startsWith(token.getFromLiteral())) {
							++ numPossibleMatch;
						}
					}
					break;
					
				case CS_LITERAL:
					if (cur.length() < token.getLiteral().length()) {
						++ numPossibleMatch;
						match = false;
					}
					else if (cur.length() == token.getLiteral().length()) {
						match = cur.toString().equals(token.getLiteral());
					}
					else {
						match = false;
					}
					break;
					
				case CI_LITERAL:
					if (cur.length() < token.getLiteral().length()) {
						++ numPossibleMatch;
						match = false;
					}
					else if (cur.length() == token.getLiteral().length()) {
						match = cur.toString().equalsIgnoreCase(token.getLiteral());
					}
					else {
						match = false;
					}
					break;

				case CHARTYPE:
					final boolean matches = token.getCharType().matches(cur.toString());
					if (matches) {
						// Matches but we should read all characters from stream
						if (DEBUG) {
							System.out.println(PREFIX + " matched chartype");
						}
						++ numPossibleMatch;
						match = false;
					}
					else {
						// Does not match, but if length > 1 that means we have a match from previous chars
						if (   cur.length() > 1 
							&& token.getCharType().matches(cur.toString().substring(0, cur.length() - 1))) {
							
							if (DEBUG) {
								System.out.println(PREFIX + " no longer matched chartype but matched last");
							}
							match = true;
							
							// Must copy to temporary buf since we read a character that cannot be read
							this.buffered = val;
							cur.setLength(cur.length() - 1);
						}
						else {
							// Cannot match
							match = false;
						}
					}
					break;
					
				case EOF:
					// skip
					match = false;
					break;
						
					
				default:
					throw new IllegalArgumentException("Unknown token type " + token.getTokenType() + " for token " + token);
				}
				
				if (DEBUG) {
					System.out.println(PREFIX + " match to " + token + ", buf = \"" + cur + "\", match=" + match +", numPossibleMatch=" + numPossibleMatch);
				}
				
				if (match) {
					// Return first matching token
					found = token;
					break;
				}
			}

			if (numPossibleMatch == 0 && found == null) {
				
				if (DEBUG) {
					System.out.println(PREFIX + " No possible matches, returning");
				}
				
				// No possible matches, return not-found token
				found = tokNone;
			}

		} while (found == null);
		
		if (DEBUG) {
			System.out.println(PREFIX + " returned token " + found + " at " + lineNo + ", buf=\"" + cur + "\", buffered char='"+ (char)buffered + "'");
		}
		
		this.lastToken = found;
		
		return found;
	}
	
	private final int read() throws IOException {
		final int ret;
		
		if (buffered >= 0) {
			ret = buffered;
			this.buffered = -1;
		}
		else {
			ret = input.readNext();
		}

		return ret;
	}
	
	public final String get() {
		return cur.toString();
	}
	
	public final ParserException unexpectedToken() {
		return new ParserException("Unexpected token for \"" + cur.toString() + "\" at " + lineNo + ": " + lastToken);
	}
}
