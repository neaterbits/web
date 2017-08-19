package com.test.web.parse.common;

public interface IToken {

	TokenType getTokenType();
	
	char getCharacter();
	
	String getLiteral();
	
	CharType getCharType();
	
}
