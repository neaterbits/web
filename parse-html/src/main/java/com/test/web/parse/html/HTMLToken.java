package com.test.web.parse.html;

import com.test.web.document.common.HTMLAttribute;
import com.test.web.document.common.HTMLElement;
import com.test.web.parse.common.CharType;
import com.test.web.parse.common.CharTypeWS;
import com.test.web.parse.common.IToken;
import com.test.web.parse.common.TokenType;
import com.test.web.parse.css.CharTypeHTMLElementClass;
import com.test.web.parse.css.CharTypeHTMLElementTag;

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
	LINK(HTMLElement.LINK),
	SCRIPT(HTMLElement.SCRIPT),
	BODY(HTMLElement.BODY),
	DIV(HTMLElement.DIV),
	SPAN(HTMLElement.SPAN),
	INPUT(HTMLElement.INPUT),
	FIELDSET(HTMLElement.FIELDSET),
	UL(HTMLElement.UL),
	LI(HTMLElement.LI),
	
	// Attributes
	
	ID(HTMLAttribute.ID),
	CLASS(HTMLAttribute.CLASS),
	TRANSLATE(HTMLAttribute.TRANSLATE),
	ACCESSKEY(HTMLAttribute.ACCESSKEY),
	CONTENTEDITABLE(HTMLAttribute.CONTENTEDITABLE),
	CONTEXTMENU(HTMLAttribute.CONTEXTMENU),
	DIRECTION(HTMLAttribute.DIRECTION),
	DRAGGABLE(HTMLAttribute.DRAGGABLE),
	DROPZONE(HTMLAttribute.DROPZONE),
	
	HIDDEN(HTMLAttribute.HIDDEN),
	
	LANG(HTMLAttribute.LANG),
	
	SPELLCHECK(HTMLAttribute.SPELLCHECK),
	TABINDEX(HTMLAttribute.TABINDEX),
	ATTR_TITLE(HTMLAttribute.TITLE),
	
	STYLE(HTMLAttribute.STYLE),
	REL(HTMLAttribute.REL),
	TYPE(HTMLAttribute.TYPE),
	HREF(HTMLAttribute.HREF),
	
	CLASS_NAME(CharTypeHTMLElementClass.INSTANCE),
	ANY_TAG(CharTypeHTMLElementTag.INSTANCE)
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

	private static final HTMLToken [] NO_TOKENS = new HTMLToken[0];
	
	private static final HTMLToken [] GLOBAL_ATTRIBUTE_TOKENS;
	
	
	static {
		
		int numGlobalAttributes = 0;
		
		for (HTMLAttribute attribute : HTMLAttribute.values()) {
			if (attribute.isGlobal()) {
				++ numGlobalAttributes;
			}
		}
		
		GLOBAL_ATTRIBUTE_TOKENS = new HTMLToken[numGlobalAttributes];
		
		int i = 0;
		for (HTMLAttribute attribute : HTMLAttribute.values()) {
			if (attribute.isGlobal()) {
				GLOBAL_ATTRIBUTE_TOKENS[i ++] = findTokenForAttribute(attribute);
			}
		}
	}
	
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

	
	private static HTMLToken findTokenForAttribute(HTMLAttribute attribute) {
		HTMLToken found = null;

		for (HTMLToken t : HTMLToken.values()) {
			if (t.attribute != null && t.attribute == attribute) {
				found = t;
				break;
			}
		}
		
		if (found == null) {
			throw new IllegalStateException("No token found for attribute " + attribute);
		}
		
		return found;
	}
	
	
	public static HTMLToken [] getAttributeTokens(HTMLToken token) {
		final HTMLAttribute [] attributes = token.getElement().getAttributes();
		final HTMLToken [] ret;
		
		if (attributes != null) {
			ret = new HTMLToken[GLOBAL_ATTRIBUTE_TOKENS.length + attributes.length];
			
			System.arraycopy(GLOBAL_ATTRIBUTE_TOKENS, 0, ret, 0, GLOBAL_ATTRIBUTE_TOKENS.length);

			for (int i = 0; i < attributes.length; ++ i) {
				ret[GLOBAL_ATTRIBUTE_TOKENS.length + i] = findTokenForAttribute(attributes[i]);
			}
		}
		else {
			ret = GLOBAL_ATTRIBUTE_TOKENS;
		}
		
		return ret;
	}
}
