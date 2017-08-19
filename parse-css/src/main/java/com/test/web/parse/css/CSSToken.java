package com.test.web.parse.css;

import com.test.web.parse.common.CharType;
import com.test.web.parse.common.CharTypeWS;
import com.test.web.parse.common.IToken;
import com.test.web.parse.common.TokenType;
import com.test.web.css.common.CSStyle;

public enum CSSToken implements IToken {
	NONE(),
	
	BRACKET_START('{'),
	BRACKET_END('}'),

	QUOTE('"'),
	EQUALS('='),

	WS(CharTypeWS.INSTANCE)
	
	// Styles
	;
	
	private final TokenType tokenType;
	private final char character;
	private final String literal;
	private final CharType charType;

	private CSSToken() {
		this.tokenType = TokenType.NONE;
		this.character = 0;
		this.literal = null;
		this.charType = null;
	}
	
	private CSSToken(char character) {
		this.tokenType = TokenType.CHARACTER;
		this.character = character;
		this.literal = null;
		this.charType = null;
	}
	
	private CSSToken(String literal) {
		this.tokenType = TokenType.CHARTYPE;
		this.character = 0;
		this.literal = literal;
		this.charType = null;
	}

	private CSSToken(CSStyle element) {
		this(element.getName());
	}

	private CSSToken(CharType charType) {
		this.tokenType = TokenType.CHARTYPE;
		this.character = 0;
		this.literal = null;
		this.charType = charType;
	}

	@Override
	public TokenType getTokenType() {
		return tokenType;
	}

	@Override
	public char getCharacter() {
		return character;
	}

	@Override
	public String getLiteral() {
		return literal;
	}

	@Override
	public CharType getCharType() {
		return charType;
	}
}
