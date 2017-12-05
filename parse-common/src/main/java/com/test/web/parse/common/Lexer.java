package com.test.web.parse.common;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Array;
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
	private final TOKEN [] possiblyMatchingTokens;
	private final boolean [] exactMatches;
	private final TokenMatch tokenMatch;
	
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
		
		this.possiblyMatchingTokens = createTokenArray(tokenClass);
		this.exactMatches = new boolean[tokenClass.getEnumConstants().length];
		
		this.tokenMatch = new TokenMatch();
		
		this.tokNone = tokNone;
		this.tokEOF = tokEOF;
		this.cur = new StringBuilder();
		this.buffered = -1;
		this.lineNo = 1;
	}
	
	@SuppressWarnings("unchecked")
	private TOKEN [] createTokenArray(Class<TOKEN> tokenClass) {
		return (TOKEN[])Array.newInstance(tokenClass, tokenClass.getEnumConstants().length);
	}
	
	// HElper class to return multiple values
	private static class TokenMatch {
		private boolean matchesExactly;
		private boolean mightMatch;
	}
	
	public TOKEN lex(@SuppressWarnings("unchecked") TOKEN ... inputTokens) throws IOException {
		
		if (DEBUG) {
			System.out.println("----");
			System.out.println(PREFIX + " lex(" + Arrays.toString(inputTokens) + ")");
		}
		
		if (inputTokens.length > possiblyMatchingTokens.length) {
			throw new IllegalArgumentException("tokens.length > matchingTokens.length");
		}
		
		cur.setLength(0);
		
		if (input.markSupported()) {
			// Mark so that tokenizer may know starting point of string
			input.mark();
		}
		
		// Scan all tokens for input from reader and check whether any tokens match 
		
		TOKEN found = null;
		
		TOKEN longestFoundSoFar = null;
		
		// Start out with scanning all input tokens, we will switch to scan only those tokens left matching
		TOKEN [] tokens = inputTokens;
		int numTokens = inputTokens.length;
		
		// Read input until finds matching token
		// TODO: exit if cannot find matching token of any length
		do {
			final int val = read();
			
			
			if (val < 0) {
				// If found a matching token, return that
				if (longestFoundSoFar != null) {
					found = longestFoundSoFar;
					bufferCharacter(val); // put EOF back
				}
				else {
					if (tokEOF != null) {
						for (TOKEN token : tokens) {
							if (token == tokEOF) {
								found = tokEOF;
								break;
							}
						}
					}
				}
				
				if (found == null) {
					throw new EOFException("Reached EOF");
				}
				else {
					// Just break out of loop since we already found a token
					break;
				}
			}
			
			final char c = (char)val;
			
			// for showing lineno in case of error
			if (c == '\n') {
				++ lineNo;
			}
			
			cur.append(c);
			
			int numPossibleMatch = 0;
			
			// Loop through all tokens to see if any match. We will return the longest-matching token so we have to keep track of that
			TOKEN firstMatchThisIteration = null;
			
			for (int i = 0; i < numTokens; ++ i) {
				// Check whether tokens match
				final TOKEN token = tokens[i];
				
				matchToken(token, c, tokenMatch);

				final boolean match = tokenMatch.matchesExactly;
				
				// If a token went from matching to non matching, we should remove it from the array of tokens
				// eg for a C style comment, we can continue to have a possible match against */ after found one, but we already have a match and should remove it from the list
				// since it already stopped matching
				final boolean wentFromMatchingToNonMatching = ! match && this.exactMatches[token.ordinal()];
				
				// If might match later or matches now, add to array for next iteration
				if ((tokenMatch.mightMatch || match) && ! wentFromMatchingToNonMatching) {
					this.possiblyMatchingTokens[numPossibleMatch] = token;
					++ numPossibleMatch;
				}
				
				this.exactMatches[token.ordinal()] = match;

				if (DEBUG) {
					System.out.println(PREFIX + " match to " + token + "\", match=" + match +", numPossibleMatch=" + numPossibleMatch+ ", buf = \"" + cur);
				}
				
				if (match) {
					if (firstMatchThisIteration == null) {
						firstMatchThisIteration = token;
					}
				}
			}
			
			if (firstMatchThisIteration != null) {
				// found a match this iterations, set as longest so far
				longestFoundSoFar = firstMatchThisIteration;
			}

			// No possible matches and none found, return tokNone unless have found an earlier match
			if (numPossibleMatch == 0) {

				// If read a character, buffer it for next iteration and remove from buffer
				bufferCharacter(val);

				if (cur.charAt(cur.length() - 1) != c) {
					throw new IllegalStateException("Mismatch of last char: " + c);
				}
				
				cur.setLength(cur.length() - 1);

				if (longestFoundSoFar == null) {
				
					if (DEBUG) {
						System.out.println(PREFIX + " No possible matches, returning");
					}
					
					// No possible matches, return not-found token
					found = tokNone; // triggers to break out of loop
				}
				else {
					// found a token, return that
					found = longestFoundSoFar;
				}
			}
			/*
			else if (numPossibleMatch == 1 && firstMatchThisIteration != null) {
				// Was only one match, return that
				found = firstMatchThisIteration;
				// TODO what if tokens is the very last characters of a stream, will we be able to fetch that here? Could still be multiple matches
			}
			*/
			else {
				// Continue iterating, switch to iterating over only matching tokens
				tokens = this.possiblyMatchingTokens;
				numTokens = numPossibleMatch;
			}
		} while (found == null);
		
		if (DEBUG) {
			System.out.println(PREFIX + " returned token " + found + " at " + lineNo + ", buf=\"" + cur + "\", buffered char='"+ (char)buffered + "'");
		}
		
		this.lastToken = found;
		
		return found;
	}

	private void matchToken(TOKEN token, char c, TokenMatch tokenMatch) {
		if (DEBUG) {
			System.out.println(PREFIX + " Matching token " + token + " to \"" + cur + "\"");
		}
		
		final boolean match;
		final boolean possibleMatch;
		
		switch (token.getTokenType()) {
		case CHARACTER:
			if (cur.length() == 1) {
				match = c == token.getCharacter();

				possibleMatch = match;
			}
			else {
				match = false;
				possibleMatch = false;
			}
			break;
			
		case FROM_CHAR_TO_CHAR:
			if (cur.charAt(0) != token.getFromCharacter()) {
				match = false;
				possibleMatch = false;
			}
			else if (cur.length() >= 2 && c == token.getToCharacter() ){
				match = true;
				possibleMatch = true;
			}
			else {
				possibleMatch = true;
				match = false;
			}
			break;
			
		case FROM_STRING_TO_STRING:
			if (cur.length() <= token.getFromLiteral().length()) {
				match = false;
				possibleMatch = token.getFromLiteral().startsWith(cur.toString());
			}
			else if (cur.length() >= (token.getFromLiteral().length() + token.getToLiteral().length())) {
				final String s = cur.toString();
				
				if (s.startsWith(token.getFromLiteral())) {
					if (s.endsWith(token.getToLiteral())) {
						match = true;
						possibleMatch = true;
					}
					else {
						possibleMatch = true;
						match = false;
					}
				}
				else {
					possibleMatch = false;
					match = false;
				}
			}
			else {
				// cur length is > from literal length but less than sum
				final String s = cur.toString();
				
				match = false;
				possibleMatch = s.startsWith(token.getFromLiteral());
			}
			break;
			
		case CS_LITERAL:
			if (cur.length() < token.getLiteral().length()) {
				match = false;
				possibleMatch = token.getLiteral().startsWith(cur.toString());
			}
			else if (cur.length() == token.getLiteral().length()) {
				match = cur.toString().equals(token.getLiteral());
				possibleMatch = match;
			}
			else {
				match = false;
				possibleMatch = false;
			}
			break;
			
		case CI_LITERAL:
			if (cur.length() < token.getLiteral().length()) {
				match = false;
				possibleMatch = token.getLiteral().substring(0, cur.length()).equalsIgnoreCase(cur.toString());
			}
			else if (cur.length() == token.getLiteral().length()) {
				match = cur.toString().equalsIgnoreCase(token.getLiteral());
				possibleMatch = match;
			}
			else {
				match = false;
				possibleMatch = false;
			}
			break;

		case CHARTYPE:
			final boolean matches = token.getCharType().matches(cur.toString());
			if (matches) {
				// Matches but we should read all characters from stream
				if (DEBUG) {
					System.out.println(PREFIX + " matched chartype");
				}
				match = true;
				possibleMatch = true;
			}
			else {
				match = false;
				possibleMatch = false;
			}
			break;
			
		case EOF:
			// skip
			match = false;
			possibleMatch = false;
			break;
				
			
		default:
			throw new IllegalArgumentException("Unknown token type " + token.getTokenType() + " for token " + token);
		}

		tokenMatch.matchesExactly = match;
		tokenMatch.mightMatch = possibleMatch;
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
	
	private void bufferCharacter(int val) {
		this.buffered = val;
		
		if ((char) val == '\n') {
			// if buffering newline, subtract from previously increased lineNo
			-- lineNo;
		}
	}
	
	public final String get() {
		return cur.toString();
	}
	
	public final ParserException unexpectedToken() {
		return new ParserException("Unexpected token for \"" + cur.toString() + "\" at " + lineNo + ": " + lastToken);
	}
	
	public final int getEndSkip() {
		return buffered >= 0 ? 1 : 0;
	}
}
