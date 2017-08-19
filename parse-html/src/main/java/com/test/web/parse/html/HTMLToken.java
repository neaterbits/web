package com.test.web.parse.html;

import com.test.web.parse.common.CharType;
import com.test.web.parse.common.CharTypeWS;
import com.test.web.parse.common.IToken;
import com.test.web.parse.common.TokenType;
import com.test.web.parse.html.enums.HTMLAttribute;
import com.test.web.parse.html.enums.HTMLElement;

public enum HTMLToken implements IToken {

	NONE(),
	
	TAG_START('<'),
	TAG_END('>'),
	END_TAG_MARKER('/'),

	QUOTE('"'),
	EQUALS('='),

	WS(CharTypeWS.INSTANCE),
	
	// Tags
	HTML(HTMLElement.HTML),
	HEAD(HTMLElement.HEAD),
	SCRIPT(HTMLElement.SCRIPT),
	BODY(HTMLElement.BODY),
	DIV(HTMLElement.DIV),
	SPAN(HTMLElement.SPAN),
	INPUT(HTMLElement.INPUT),
	FIELDSET(HTMLElement.FIELDSET),
	UL(HTMLElement.UL),
	LI(HTMLElement.LI),
	
	
	// Attributes
	DISPLAY(HTMLAttribute.DISPLAY),
	STYLE(HTMLAttribute.STYLE)
	;
	
	private final TokenType tokenType;
	private final char character;
	private final String literal;
	private final CharType charType;
	private final HTMLElement element;
	private final HTMLAttribute attribute;

	private HTMLToken() {
		this.tokenType = TokenType.NONE;
		this.character = 0;
		this.literal = null;
		this.charType = null;
		this.element = null;
		this.attribute = null;
	}
	
	private HTMLToken(char character) {
		this.tokenType = TokenType.CHARACTER;
		this.character = character;
		this.literal = null;
		this.charType = null;
		this.element = null;
		this.attribute = null;
	}
	
	private HTMLToken(String literal) {
		this(literal, null, null);
	}
	
	private HTMLToken(String literal, HTMLElement element, HTMLAttribute attribute) {
		this.tokenType = TokenType.LITERAL;
		this.character = 0;
		this.literal = literal;
		this.charType = null;
		this.element = element;
		this.attribute = attribute;
	}

	private HTMLToken(HTMLElement element) {
		this(element.getName(), element, null);
	}

	private HTMLToken(HTMLAttribute attribute) {
		this(attribute.getName(), null, attribute);
	}

	private HTMLToken(CharType charType) {
		this.tokenType = TokenType.CHARTYPE;
		this.character = 0;
		this.literal = null;
		this.charType = charType;
		this.element = null;
		this.attribute = null;
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

	public HTMLElement getElement() {
		return element;
	}

	public HTMLAttribute getAttribute() {
		return attribute;
	}
}
