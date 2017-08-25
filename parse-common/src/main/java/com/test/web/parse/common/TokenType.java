package com.test.web.parse.common;

public enum TokenType {
	
	NONE,
	EOF,
	
	CHARACTER, // A single character
	CS_LITERAL, // A literal string
	CI_LITERAL, // A case insensitive literal string
	CHARTYPE;
}