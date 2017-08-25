package com.test.web.parse.html;

import com.test.web.parse.common.CharType;
import com.test.web.parse.common.CharTypeWS;
import com.test.web.parse.common.IToken;
import com.test.web.parse.common.TokenType;
import com.test.web.parse.html.enums.HTMLAttribute;
import com.test.web.parse.html.enums.HTMLElement;

public enum HTMLToken implements IToken {

	NONE(),
	
	TAG_LESS_THAN('<'),
	TAG_GREATER_THAN('>'),
	TAG_SLASH('/'),
	
	TAG_EXCLAMATION_POINT('!'),
	
	// Pseudo-token to return that we reached end og tag
	TAG_END(' '),

	QUOTE('"'),
	EQUALS('='),
	
	QUOTED_STRING('"', '"'),
	
	COMMENT_CONTENT("!--", "-->"),

	WS(CharTypeWS.INSTANCE),
	
	TEXT(HTMLCharTypeText.INSTANCE),
	
	DOCTYPE("DOCTYPE", false),
	DOCTYPE_HTML("HTML", false),
	DOCTYPE_PUBLIC("PUBLIC", false),
	
	// Tags
	HTML(HTMLElement.HTML),
	HEAD(HTMLElement.HEAD),
	TITLE(HTMLElement.TITLE),
	SCRIPT(HTMLElement.SCRIPT),
	BODY(HTMLElement.BODY),
	DIV(HTMLElement.DIV),
	SPAN(HTMLElement.SPAN),
	INPUT(HTMLElement.INPUT),
	FIELDSET(HTMLElement.FIELDSET),
	UL(HTMLElement.UL),
	LI(HTMLElement.LI),
	
	// Attributes
	STYLE(HTMLAttribute.STYLE),
	TYPE(HTMLAttribute.TYPE)
	;
	
	private final TokenType tokenType;
	private final char character;
	private final char toCharacter;
	private final String literal;
	private final String toLiteral;
	private final CharType charType;
	private final HTMLElement element;
	private final HTMLAttribute attribute;
	
	private HTMLToken [] attributeTokens;

	static {
		// Initialize attributeTokens for all elements
		for (HTMLToken token : HTMLToken.values()) {
			if (token.getElement() != null) {
				token.attributeTokens = getAttributeTokens(token);
			}
		}
	}
	
	private HTMLToken() {
		this.tokenType = TokenType.NONE;
		this.character = 0;
		this.toCharacter = 0;
		this.literal = null;
		this.toLiteral = null;
		this.charType = null;
		this.element = null;
		this.attribute = null;
	}
	
	private HTMLToken(char character) {
		this.tokenType = TokenType.CHARACTER;
		this.character = character;
		this.toCharacter = 0;
		this.literal = null;
		this.toLiteral = null;
		this.charType = null;
		this.element = null;
		this.attribute = null;
	}
	
	private HTMLToken(char fromCharacter, char toCharacter) {
		this.tokenType = TokenType.FROM_CHAR_TO_CHAR;
		this.character = fromCharacter;
		this.toCharacter = toCharacter;
		this.literal = null;
		this.toLiteral = null;
		this.charType = null;
		this.element = null;
		this.attribute = null;
	}

	private HTMLToken(String literal) {
		this(literal, true, null, null);
	}
	
	private HTMLToken(String literal, boolean caseSensitive) {
		this(literal, caseSensitive, null, null);
	}

	private HTMLToken(String fromLiteral, String toLiteral) {
		this.tokenType = TokenType.FROM_STRING_TO_STRING;
		this.character = 0;
		this.toCharacter = 0;
		this.literal = fromLiteral;
		this.toLiteral = toLiteral;
		this.charType = null;
		this.element = null;
		this.attribute = null;
	}

	private HTMLToken(String literal, boolean caseSensitive, HTMLElement element, HTMLAttribute attribute) {
		this.tokenType = caseSensitive ? TokenType.CS_LITERAL : TokenType.CI_LITERAL;
		this.character = 0;
		this.toCharacter = 0;
		this.literal = literal;
		this.toLiteral = null;
		this.charType = null;
		this.element = element;
		this.attribute = attribute;
	}

	private HTMLToken(HTMLElement element) {
		this(element.getName(), false, element, null);
	}

	private HTMLToken(HTMLAttribute attribute) {
		this(attribute.getName(), false, null, attribute);
	}

	private HTMLToken(CharType charType) {
		this.tokenType = TokenType.CHARTYPE;
		this.character = 0;
		this.toCharacter = 0;
		this.literal = null;
		this.toLiteral = null;
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
	public char getFromCharacter() {
		return character;
	}

	@Override
	public char getToCharacter() {
		return toCharacter;
	}

	@Override
	public String getLiteral() {
		return literal;
	}
	
	@Override
	public String getFromLiteral() {
		return literal;
	}

	@Override
	public String getToLiteral() {
		return toLiteral;
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
	
	
	public HTMLToken[] getAttributeTokens() {
		return attributeTokens;
	}

	private static final HTMLToken [] NO_TOKENS = new HTMLToken[0];
	
	public static HTMLToken [] getAttributeTokens(HTMLToken token) {
		final HTMLAttribute [] attributes = token.getElement().getAttributes();
		
		final HTMLToken [] ret;
		
		
		if (attributes != null) {
			ret = new HTMLToken[attributes.length];
			
			for (int i = 0; i < attributes.length; ++ i) {
				HTMLToken found = null;

				for (HTMLToken t : HTMLToken.values()) {
					if (t.attribute != null && t.attribute == attributes[i]) {
						found = t;
						break;
					}
				}
				
				if (found == null) {
					throw new IllegalStateException("No token found for attribute " + attributes[i]);
				}
				
				ret[i] = found;
			}
		}
		else {
			ret = NO_TOKENS;
		}
		
		return ret;
	}
}
