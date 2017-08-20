package com.test.web.parse.common;

public enum TokenType {
	
	NONE,
	EOF,
	
	CHARACTER, // A single character
	LITERAL, // A literal string
	CHARTYPE;
}