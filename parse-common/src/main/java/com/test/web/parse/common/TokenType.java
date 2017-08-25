package com.test.web.parse.common;

public enum TokenType {
	
	NONE,
	EOF,
	
	CHARACTER, // A single character
	CS_LITERAL, // A literal string
	CI_LITERAL, // A case insensitive literal string
	CHARTYPE,
	FROM_CHAR_TO_CHAR, // From a character to a characters, eg. quoted string
	FROM_STRING_TO_STRING // From a string to another, eg. HTML comment
	;
}