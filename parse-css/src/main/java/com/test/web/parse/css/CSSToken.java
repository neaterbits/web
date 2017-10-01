package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.parse.common.CharType;
import com.test.web.parse.common.CharTypeHexDigit;
import com.test.web.parse.common.CharTypeInteger;
import com.test.web.parse.common.CharTypeWS;
import com.test.web.parse.common.IToken;
import com.test.web.parse.common.TokenType;

public enum CSSToken implements IToken {
	NONE(TokenType.NONE),
	EOF(TokenType.EOF),
	
	ID_MARKER('#'),
	CLASS_MARKER('.'),
	
	ID(CharTypeHTMLElementId.INSTANCE),
	CLASS(CharTypeHTMLElementClass.INSTANCE),

	TAG(CharTypeHTMLElementTag.INSTANCE),
	
	INTEGER(CharTypeInteger.INSTANCE),
	HEXDIGITS(CharTypeHexDigit.INSTANCE),
	
	BRACKET_START('{'),
	BRACKET_END('}'),

	COLON(':'),
	
	DOT('.'),
	SEMICOLON(';'),
	
	QUOTE('"'),
	EQUALS('='),
	
	COLOR_MARKER('#'),
	
	AUTO("auto"),
	
	UNIT_PX("px", CSSUnit.PX),
	UNIT_EM("em", CSSUnit.EM),
	UNIT_PCT("%", CSSUnit.PCT),
	
	CSS_WIDTH(CSStyle.WIDTH),
	CSS_HEIGHT(CSStyle.HEIGHT),
	
	CSS_BACKGOUND_COLOR(CSStyle.BACKGROUND_COLOR),
	CSS_FONT_SIZE(CSStyle.FONT_SIZE),
	
	CSS_MARGIN_LEFT(CSStyle.MARGIN_LEFT),
	CSS_MARGIN_RIGHT(CSStyle.MARGIN_RIGHT),
	CSS_MARGIN_TOP(CSStyle.MARGIN_TOP),
	CSS_MARGIN_BOTTOM(CSStyle.MARGIN_BOTTOM),
	
	CSS_PADDING_LEFT(CSStyle.PADDING_LEFT),
	CSS_PADDING_RIGHT(CSStyle.PADDING_RIGHT),
	CSS_PADDING_TOP(CSStyle.PADDING_TOP),
	CSS_PADDING_BOTTOM(CSStyle.PADDING_BOTTOM),
	
	CSS_DISPLAY(CSStyle.DISPLAY),
	CSS_POSITION(CSStyle.POSITION),
	CSS_FLOAT(CSStyle.FLOAT),
	CSS_TEXT_ALIGN(CSStyle.TEXT_ALIGN),
	CSS_OVERFLOW(CSStyle.OVERFLOW),
	
	DISPLAY_INLINE(CSSDisplay.INLINE),
	DISPLAY_BLOCK(CSSDisplay.BLOCK),
	DISPLAY_FLEX(CSSDisplay.FLEX),
	DISPLAY_INLINE_BLOCK(CSSDisplay.INLINE_BLOCK),
	DISPLAY_INLINE_FLEX(CSSDisplay.INLINE_FLEX),
	DISPLAY_INLINE_TABLE(CSSDisplay.INLINE_TABLE),
	DISPLAY_LIST_ITEM(CSSDisplay.LIST_ITEM),
	DISPLAY_RUN_IN(CSSDisplay.RUN_IN),
	DISPLAY_TABLE(CSSDisplay.TABLE),
	DISPLAY_TABLE_CAPTION(CSSDisplay.TABLE_CAPTION),
	DISPLAY_TABLE_COLUMN_GROUP(CSSDisplay.TABLE_COLUMN_GROUP),
	DISPLAY_TABLE_HEADER_GROUP(CSSDisplay.TABLE_HEADER_GROUP),
	DISPLAY_TABLE_FOOTER_GROUP(CSSDisplay.TABLE_FOOTER_GROUP),
	DISPLAY_TABLE_ROW_GROUP(CSSDisplay.TABLE_ROW_GROUP),
	DISPLAY_TABLE_CELL(CSSDisplay.TABLE_CELL),
	DISPLAY_TABLE_COLUMN(CSSDisplay.TABLE_COLUMN),
	DISPLAY_TABLE_ROW(CSSDisplay.TABLE_ROW),
	DISPLAY_NONE(CSSDisplay.NONE),
	DISPLAY_INITIAL(CSSDisplay.INITIAL),
	DISPLAY_INHERIT(CSSDisplay.INHERIT),

	POS_STATIC(CSSPosition.STATIC),
	POS_ABSOLUTE(CSSPosition.ABSOLUTE),
	POS_FIXED(CSSPosition.FIXED),
	POS_RELATIVE(CSSPosition.RELATIVE),
	POS_INITIAL(CSSPosition.INITIAL),
	POS_INHERIT(CSSPosition.INHERIT),
	
	FLOAT_NONE(CSSFloat.NONE),
	FLOAT_LEFT(CSSFloat.LEFT),
	FLOAT_RIGHT(CSSFloat.RIGHT),
	FLOAT_INITIAL(CSSFloat.INITIAL),
	FLOAT_INHERIT(CSSFloat.INHERIT),

	TA_LEFT(CSSTextAlign.LEFT),
	TA_RIGHT(CSSTextAlign.RIGHT),
	TA_CENTER(CSSTextAlign.CENTER),
	TA_JUSTIFY(CSSTextAlign.JUSTIFY),
	TA_INITIAL(CSSTextAlign.INITIAL),
	TA_INHERIT(CSSTextAlign.INHERIT),
	

	OVERFLOW_VISIBLE(CSSOverflow.VISIBLE),
	OVERFLOW_HIDDEN(CSSOverflow.HIDDEN),
	OVERFLOW_SCROLL(CSSOverflow.SCROLL),
	OVERFLOW_AUTO(CSSOverflow.AUTO),
	OVERFLOW_INITIAL(CSSOverflow.INITIAL),
	OVERFLOW_INHERIT(CSSOverflow.INHERIT),

	WS(CharTypeWS.INSTANCE)
	
	// Styles
	;
	
	private final TokenType tokenType;
	private final char character;
	private final String literal;
	private final CharType charType;
	private final CSStyle element;
	private final CSSUnit unit;
	private final CSSDisplay display;
	private final CSSPosition position;
	private final CSSFloat _float;
	private final CSSTextAlign textAlign;
	private final CSSOverflow overflow;

	private CSSToken(TokenType tokenType) {
		
		if (tokenType != TokenType.NONE && tokenType != TokenType.EOF) {
			throw new IllegalArgumentException("Neither NONE or EOF token: " + tokenType);
		}
		
		this.tokenType = tokenType;
		this.character = 0;
		this.literal = null;
		this.charType = null;
		this.element = null;
		this.unit = null;
		this.display = null;
		this.position = null;
		this._float = null;
		this.textAlign = null;
		this.overflow = null;
	}
	
	private CSSToken(char character) {
		this.tokenType = TokenType.CHARACTER;
		this.character = character;
		this.literal = null;
		this.charType = null;
		this.element = null;
		this.unit = null;
		this.display = null;
		this.position = null;
		this._float = null;
		this.textAlign = null;
		this.overflow = null;
	}
	
	private CSSToken(String literal, CSStyle element, CSSUnit unit, CSSDisplay display, CSSPosition position, CSSFloat _float, CSSTextAlign textAlign, CSSOverflow overflow) {
		this.tokenType = TokenType.CS_LITERAL;
		this.character = 0;
		this.literal = literal;
		this.charType = null;
		this.element = element;
		this.unit = unit;
		this.display = display;
		this.position = position;
		this._float = _float;
		this.textAlign = textAlign;
		this.overflow = overflow;
	}

	private CSSToken(CSStyle element) {
		this(element.getName(), element, null, null, null, null, null, null);
	}

	private CSSToken(String literal) {
		this(literal, null, null, null, null, null, null, null);
	}

	private CSSToken(String literal, CSSUnit unit) {
		this(literal, null, unit, null, null, null, null, null);
	}

	private CSSToken(CSSDisplay display) {
		this(display.getName(), null, null, display, null, null, null, null);
	}

	private CSSToken(CSSPosition position) {
		this(position.getName(), null, null, null, position, null, null, null);
	}

	private CSSToken(CSSFloat _float) {
		this(_float.getName(), null, null, null, null, _float, null, null);
	}

	private CSSToken(CSSTextAlign textAlign) {
		this(textAlign.getName(), null, null, null, null, null, textAlign, null);
	}

	private CSSToken(CSSOverflow overflow) {
		this(overflow.getName(), null, null, null, null, null, null, overflow);
	}

	private CSSToken(CharType charType) {
		this.tokenType = TokenType.CHARTYPE;
		this.character = 0;
		this.literal = null;
		this.charType = charType;
		this.element = null;
		this.unit = null;
		this.display = null;
		this.position = null;
		this._float = null;
		this.textAlign = null;
		this.overflow = null;
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

	@Override
	public char getFromCharacter() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public char getToCharacter() {
		throw new UnsupportedOperationException("TODO");
	}
	

	@Override
	public String getFromLiteral() {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public String getToLiteral() {
		throw new UnsupportedOperationException("TODO");
	}

	public CSStyle getElement() {
		return element;
	}

	public CSSUnit getUnit() {
		return unit;
	}

	public CSSTextAlign getTextAlign() {
		return textAlign;
	}

	public CSSDisplay getDisplay() {
		return display;
	}

	public CSSPosition getPosition() {
		return position;
	}

	public CSSFloat getFloat() {
		return _float;
	}

	public CSSOverflow getOverflow() {
		return overflow;
	}
}
