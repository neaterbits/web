package com.test.web.parse.common;

import java.io.EOFException;
import java.io.IOException;

import com.test.web.io.common.CharInput;

public final class Lexer<TOKEN extends Enum<TOKEN> & IToken, INPUT extends CharInput> {

	private final INPUT input;
	private final TOKEN tokNone;
	
	private final StringBuilder cur;
	private int buffered;
	
	// Scratch array for maintaining number of matching tokens at any given type
	private final int [] matchingTokens;
	
	private int lineNo;
	
	
	public Lexer(INPUT input, Class<TOKEN> tokenClass, TOKEN tokNone) {
		
		if (input == null) {
			throw new IllegalArgumentException("inputStream == null");
		}
		
		if (tokNone == null) {
			throw new IllegalArgumentException("tokNone == null");
		}
		
		this.input = input;
		
		this.matchingTokens = new int[tokenClass.getEnumConstants().length];
		
		this.tokNone = tokNone;
		this.cur = new StringBuilder();
		this.buffered = -1;
		this.lineNo = 1;
	}
	
	
	public TOKEN lex(TOKEN ... tokens) throws IOException {
		
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
				throw new EOFException("Reached EOF");
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
					
				case LITERAL:
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
					
				case CHARTYPE:
					final boolean matches = token.getCharType().matches(cur.toString());
					if (matches) {
						// Matches but we should read all characters from stream
						++ numPossibleMatch;
						match = false;
					}
					else {
						// Does not match, but if length > 1 that means we have a match from previous chars
						if (cur.length() > 1) {
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
					
				default:
					throw new IllegalArgumentException("Unknown token type " + token.getTokenType() + " for token " + token);
				}
				
				
				if (match) {
					// Return first matching token
					found = token;
					break;
				}
				else if (numPossibleMatch == 0) {
					// No possible matches, return not-found token
					found = tokNone;
				}
			}

		} while (found == null);
		
		return found;
	}
	
	public final int read() throws IOException {
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
	
	public final ParserException unexpectedToken() {
		return new ParserException("Unexpected token at " + lineNo);
	}
}
