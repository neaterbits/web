package com.test.web.parse.css;

import com.test.web.css.common.enums.CSSBackground;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSDisplay;
import com.test.web.css.common.enums.CSSFloat;
import com.test.web.css.common.enums.CSSFontSize;
import com.test.web.css.common.enums.CSSFontWeight;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSOverflow;
import com.test.web.css.common.enums.CSSPosition;
import com.test.web.css.common.enums.CSSTextAlign;
import com.test.web.css.common.enums.CSSTextDecoration;
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
	
	COMMENT("/*", "*/"),
	
	CLASS_MARKER('.'),
	ID_MARKER('#'),
	
	ID(CharTypeHTMLElementId.INSTANCE),
	CLASS(CharTypeHTMLElementClass.INSTANCE),

	TAG(CharTypeHTMLElementTag.INSTANCE),
	
	INTEGER(CharTypeInteger.INSTANCE),
	HEXDIGITS(CharTypeHexDigit.INSTANCE),
	
	BRACKET_START('{'),
	BRACKET_END('}'),
	
	PARENTHESIS_START('('),
	PARENTHESIS_END(')'),

	COLON(':'),
	
	COMMA(','),
	
	DOT('.'),
	SEMICOLON(';'),
	
	QUOTE('"'),
	EQUALS('='),
	
	COLOR_MARKER('#'),
	
	FUNCTION_RGB("rgb"),
	FUNCTION_RGBA("rgba"),
	
	AUTO("auto"),
	
	UNIT_PX("px", CSSUnit.PX),
	UNIT_EM("em", CSSUnit.EM),
	UNIT_PCT("%", CSSUnit.PCT),
	
	CSS_WIDTH(CSStyle.WIDTH),
	CSS_HEIGHT(CSStyle.HEIGHT),
	
	CSS_COLOR(CSStyle.COLOR),
	CSS_BACKGOUND_COLOR(CSStyle.BACKGROUND_COLOR),
	
	CSS_FONT_SIZE(CSStyle.FONT_SIZE),
	CSS_FONT_WEIGHT(CSStyle.FONT_WEIGHT),
	
	CSS_MARGIN_LEFT(CSStyle.MARGIN_LEFT),
	CSS_MARGIN_RIGHT(CSStyle.MARGIN_RIGHT),
	CSS_MARGIN_TOP(CSStyle.MARGIN_TOP),
	CSS_MARGIN_BOTTOM(CSStyle.MARGIN_BOTTOM),
	CSS_MARGIN(CSStyle.MARGIN),
	
	CSS_PADDING_LEFT(CSStyle.PADDING_LEFT),
	CSS_PADDING_RIGHT(CSStyle.PADDING_RIGHT),
	CSS_PADDING_TOP(CSStyle.PADDING_TOP),
	CSS_PADDING_BOTTOM(CSStyle.PADDING_BOTTOM),
	
	CSS_DISPLAY(CSStyle.DISPLAY),
	CSS_POSITION(CSStyle.POSITION),
	CSS_FLOAT(CSStyle.FLOAT),
	CSS_TEXT_ALIGN(CSStyle.TEXT_ALIGN),
	CSS_OVERFLOW(CSStyle.OVERFLOW),
	
	CSS_TEXT_DECORATION(CSStyle.TEXT_DECORATION),
	
	CSS_MAX_WIDTH(CSStyle.MAX_WIDTH),
	CSS_MAX_HEIGHT(CSStyle.MAX_HEIGHT),

	CSS_MIN_WIDTH(CSStyle.MIN_WIDTH),
	CSS_MIN_HEIGHT(CSStyle.MIN_HEIGHT),

	FGCOLOR_INITIAL(CSSForeground.INITIAL),
	FGCOLOR_INHERIT(CSSForeground.INHERIT),
	
	BGCOLOR_TRNSPARENT(CSSBackground.TRANSPARENT),
	BGCOLOR_INITIAL(CSSBackground.INITIAL),
	BGCOLOR_INHERIT(CSSBackground.INHERIT),
	
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
	
	TEXT_DECORATION_NONE(CSSTextDecoration.NONE),
	TEXT_DECORATION_UNDERLINE(CSSTextDecoration.UNDERLINE),
	TEXT_DECORATION_OVERLINE(CSSTextDecoration.OVERLINE),
	TEXT_DECORATION_LINE_THOUGH(CSSTextDecoration.LINE_THOUGH),
	TEXT_DECORATION_INITIAL(CSSTextDecoration.INITIAL),
	TEXT_DECORATION_INHERIT(CSSTextDecoration.INHERIT),
	
	FONTSIZE_MEDIUM(CSSFontSize.MEDIUM),
	FONTSIZE_XX_SMALL(CSSFontSize.XX_SMALL),
	FONTSIZE_X_SMALL(CSSFontSize.X_SMALL),
	FONTSIZE_SMALL(CSSFontSize.SMALL),
	FONTSIZE_LARGE(CSSFontSize.LARGE),
	FONTSIZE_X_LARGE(CSSFontSize.X_LARGE),
	FONTSIZE_XX_LARGE(CSSFontSize.XX_LARGE),
	FONTSIZE_SMALLER(CSSFontSize.SMALLER),
	FONTSIZE_LARGER(CSSFontSize.LARGER),
	FONTSIZE_INITIAL(CSSFontSize.INITIAL),
	FONTSIZE_INHERIT(CSSFontSize.INHERIT),
	
	FONTWEIGHT_NORMAL(CSSFontWeight.NORMAL),
	FONTWEIGHT_BOLD(CSSFontWeight.BOLD),
	FONTWEIGHT_BOLDER(CSSFontWeight.BOLDER),
	FONTWEIGHT_LIGHTER(CSSFontWeight.LIGHTER),
	FONTWEIGHT_INITIAL(CSSFontWeight.INITIAL),
	FONTWEIGHT_INHERIT(CSSFontWeight.INHERIT),

	CSSCOLOR_CSSCOLOR_ALICE_BLUE(CSSColor.ALICE_BLUE),
	CSSCOLOR_ANTIQUE_WHITE(CSSColor.ANTIQUE_WHITE),
	CSSCOLOR_AQUA(CSSColor.AQUA),
	CSSCOLOR_AQUAMARINE(CSSColor.AQUAMARINE),
	CSSCOLOR_AZURE(CSSColor.AZURE),
	CSSCOLOR_BEIGE(CSSColor.BEIGE),
	CSSCOLOR_BISQUE(CSSColor.BISQUE),
	CSSCOLOR_BLACK(CSSColor.BLACK),
	CSSCOLOR_BLANCHED_ALMOND(CSSColor.BLANCHED_ALMOND),
	CSSCOLOR_BLUE(CSSColor.BLUE),
	CSSCOLOR_BLUE_VIOLET(CSSColor.BLUE_VIOLET),
	CSSCOLOR_BROWN(CSSColor.BROWN),
	CSSCOLOR_BURLY_WOOD(CSSColor.BURLY_WOOD),
	CSSCOLOR_CADET_BLUE(CSSColor.CADET_BLUE),
	CSSCOLOR_CHARTREUSE(CSSColor.CHARTREUSE),
	CSSCOLOR_CHOCOLATE(CSSColor.CHOCOLATE),
	CSSCOLOR_CORAL(CSSColor.CORAL),
	CSSCOLOR_CORNFLOWER_BLUE(CSSColor.CORNFLOWER_BLUE),
	CSSCOLOR_CORNSILK(CSSColor.CORNSILK),
	CSSCOLOR_CRIMSON(CSSColor.CRIMSON),
	CSSCOLOR_CYAN(CSSColor.CYAN),
	CSSCOLOR_DARK_BLUE(CSSColor.DARK_BLUE),
	CSSCOLOR_DARK_CYAN(CSSColor.DARK_CYAN),
	CSSCOLOR_DARK_GOLDEN_ROD(CSSColor.DARK_GOLDEN_ROD),
	CSSCOLOR_DARK_GRAY(CSSColor.DARK_GRAY),
	CSSCOLOR_DARK_GREY(CSSColor.DARK_GREY),
	CSSCOLOR_DARK_GREEN(CSSColor.DARK_GREEN),
	CSSCOLOR_DARK_KHAKI(CSSColor.DARK_KHAKI),
	CSSCOLOR_DARK_MAGENTA(CSSColor.DARK_MAGENTA),
	CSSCOLOR_DARK_OLIVE_GREEN(CSSColor.DARK_OLIVE_GREEN),
	CSSCOLOR_DARK_ORANGE(CSSColor.DARK_ORANGE),
	CSSCOLOR_DARK_ORCHID(CSSColor.DARK_ORCHID),
	CSSCOLOR_DARK_RED(CSSColor.DARK_RED),
	CSSCOLOR_DARK_SALMON(CSSColor.DARK_SALMON),
	CSSCOLOR_DARK_SEA_GREEN(CSSColor.DARK_SEA_GREEN),
	CSSCOLOR_DARK_SLATE_BLUE(CSSColor.DARK_SLATE_BLUE),
	CSSCOLOR_DARK_SLATE_GRAY(CSSColor.DARK_SLATE_GRAY),
	CSSCOLOR_DARK_SLATE_GREY(CSSColor.DARK_SLATE_GREY),
	CSSCOLOR_DARK_TURQUOISE(CSSColor.DARK_TURQUOISE),
	CSSCOLOR_DARK_VIOLET(CSSColor.DARK_VIOLET),
	CSSCOLOR_DEEP_PINK(CSSColor.DEEP_PINK),
	CSSCOLOR_DEEP_SKY_BLUE(CSSColor.DEEP_SKY_BLUE),
	CSSCOLOR_DIM_GRAY(CSSColor.DIM_GRAY),
	CSSCOLOR_DIM_GREY(CSSColor.DIM_GREY),
	CSSCOLOR_DODGER_BLUE(CSSColor.DODGER_BLUE),
	CSSCOLOR_FIRE_BRICK(CSSColor.FIRE_BRICK),
	CSSCOLOR_FLORAL_WHITE(CSSColor.FLORAL_WHITE),
	CSSCOLOR_FOREST_GREEN(CSSColor.FOREST_GREEN),
	CSSCOLOR_FUCHSIA(CSSColor.FUCHSIA),
	CSSCOLOR_GAINSBORO(CSSColor.GAINSBORO),
	CSSCOLOR_GHOST_WHITE(CSSColor.GHOST_WHITE),
	CSSCOLOR_GOLD(CSSColor.GOLD),
	CSSCOLOR_GOLDEN_ROD(CSSColor.GOLDEN_ROD),
	CSSCOLOR_GRAY(CSSColor.GRAY),
	CSSCOLOR_GREY(CSSColor.GREY),
	CSSCOLOR_GREEN(CSSColor.GREEN),
	CSSCOLOR_GREEN_YELLOW(CSSColor.GREEN_YELLOW),
	CSSCOLOR_HONEY_DEW(CSSColor.HONEY_DEW),
	CSSCOLOR_HOT_PINK(CSSColor.HOT_PINK),
	CSSCOLOR_INDIAN_RED(CSSColor.INDIAN_RED),
	CSSCOLOR_INDIGO(CSSColor.INDIGO),
	CSSCOLOR_IVORY(CSSColor.IVORY),
	CSSCOLOR_KHAKI(CSSColor.KHAKI),
	CSSCOLOR_LAVENDER(CSSColor.LAVENDER),
	CSSCOLOR_LAVENDER_BLUSH(CSSColor.LAVENDER_BLUSH),
	CSSCOLOR_LAWN_GREEN(CSSColor.LAWN_GREEN),
	CSSCOLOR_LEMON_CHIFFON(CSSColor.LEMON_CHIFFON),
	CSSCOLOR_LIGHT_BLUE(CSSColor.LIGHT_BLUE),
	CSSCOLOR_LIGHT_CORAL(CSSColor.LIGHT_CORAL),
	CSSCOLOR_LIGHT_CYAN(CSSColor.LIGHT_CYAN),
	CSSCOLOR_LIGHT_GOLDEN_ROD_YELLOW(CSSColor.LIGHT_GOLDEN_ROD_YELLOW),
	CSSCOLOR_LIGHT_GRAY(CSSColor.LIGHT_GRAY),
	CSSCOLOR_LIGHT_GREY(CSSColor.LIGHT_GREY),
	CSSCOLOR_LIGHT_GREEN(CSSColor.LIGHT_GREEN),
	CSSCOLOR_LIGHT_PINK(CSSColor.LIGHT_PINK),
	CSSCOLOR_LIGHT_SALMON(CSSColor.LIGHT_SALMON),
	CSSCOLOR_LIGHT_SEA_GREEN(CSSColor.LIGHT_SEA_GREEN),
	CSSCOLOR_LIGHT_SKY_BLUE(CSSColor.LIGHT_SKY_BLUE),
	CSSCOLOR_LIGHT_SLATE_GRAY(CSSColor.LIGHT_SLATE_GRAY),
	CSSCOLOR_LIGHT_SLATE_GREY(CSSColor.LIGHT_SLATE_GREY),
	CSSCOLOR_LIGHT_STEEL_BLUE(CSSColor.LIGHT_STEEL_BLUE),
	CSSCOLOR_LIGHT_YELLOW(CSSColor.LIGHT_YELLOW),
	CSSCOLOR_LIME(CSSColor.LIME),
	CSSCOLOR_LIME_GREEN(CSSColor.LIME_GREEN),
	CSSCOLOR_LINEN(CSSColor.LINEN),
	CSSCOLOR_MAGENTA(CSSColor.MAGENTA),
	CSSCOLOR_MAROON(CSSColor.MAROON),
	CSSCOLOR_MEDIUM_AQUA_MARINE(CSSColor.MEDIUM_AQUA_MARINE),
	CSSCOLOR_MEDIUM_BLUE(CSSColor.MEDIUM_BLUE),
	CSSCOLOR_MEDIUM_ORCHID(CSSColor.MEDIUM_ORCHID),
	CSSCOLOR_MEDIUM_PURPLE(CSSColor.MEDIUM_PURPLE),
	CSSCOLOR_MEDIUM_SEA_GREEN(CSSColor.MEDIUM_SEA_GREEN),
	CSSCOLOR_MEDIUM_SLATE_BLUE(CSSColor.MEDIUM_SLATE_BLUE),
	CSSCOLOR_MEDIUM_SPRING_GREEN(CSSColor.MEDIUM_SPRING_GREEN),
	CSSCOLOR_MEDIUM_TURQUOISE(CSSColor.MEDIUM_TURQUOISE),
	CSSCOLOR_MEDIUM_VIOLET_RED(CSSColor.MEDIUM_VIOLET_RED),
	CSSCOLOR_MIDNIGHT_BLUE(CSSColor.MIDNIGHT_BLUE),
	CSSCOLOR_MINT_CREAM(CSSColor.MINT_CREAM),
	CSSCOLOR_MISTY_ROSE(CSSColor.MISTY_ROSE),
	CSSCOLOR_MOCCASIN(CSSColor.MOCCASIN),
	CSSCOLOR_NAVAJO_WHITE(CSSColor.NAVAJO_WHITE),
	CSSCOLOR_NAVY(CSSColor.NAVY),
	CSSCOLOR_OLD_LACE(CSSColor.OLD_LACE),
	CSSCOLOR_OLIVE(CSSColor.OLIVE),
	CSSCOLOR_OLIVE_DRAB(CSSColor.OLIVE_DRAB),
	CSSCOLOR_ORANGE(CSSColor.ORANGE),
	CSSCOLOR_ORANGE_RED(CSSColor.ORANGE_RED),
	CSSCOLOR_ORCHID(CSSColor.ORCHID),
	CSSCOLOR_PALE_GOLDEN_ROD(CSSColor.PALE_GOLDEN_ROD),
	CSSCOLOR_PALE_GREEN(CSSColor.PALE_GREEN),
	CSSCOLOR_PALE_TURQUIOSE(CSSColor.PALE_TURQUIOSE),
	CSSCOLOR_PALE_VIOLET_RED(CSSColor.PALE_VIOLET_RED),
	CSSCOLOR_PAPAYA_WHIP(CSSColor.PAPAYA_WHIP),
	CSSCOLOR_PEACH_PUFF(CSSColor.PEACH_PUFF),
	CSSCOLOR_PERU(CSSColor.PERU),
	CSSCOLOR_PINK(CSSColor.PINK),
	CSSCOLOR_PLUM(CSSColor.PLUM),
	CSSCOLOR_POWDER_BLUE(CSSColor.POWDER_BLUE),
	CSSCOLOR_PURPLE(CSSColor.PURPLE),
	CSSCOLOR_REBECCA_PURPLE(CSSColor.REBECCA_PURPLE),
	CSSCOLOR_RED(CSSColor.RED),
	CSSCOLOR_ROSY_BROWN(CSSColor.ROSY_BROWN),
	CSSCOLOR_ROYAL_BLUE(CSSColor.ROYAL_BLUE),
	CSSCOLOR_SADDLE_BROWN(CSSColor.SADDLE_BROWN),
	CSSCOLOR_SALMON(CSSColor.SALMON),
	CSSCOLOR_SANDY_BROWN(CSSColor.SANDY_BROWN),
	CSSCOLOR_SEA_GREEN(CSSColor.SEA_GREEN),
	CSSCOLOR_SEA_SHELL(CSSColor.SEA_SHELL),
	CSSCOLOR_SIENNA(CSSColor.SIENNA),
	CSSCOLOR_SILVER(CSSColor.SILVER),
	CSSCOLOR_SKY_BLUE(CSSColor.SKY_BLUE),
	CSSCOLOR_SLATE_BLUE(CSSColor.SLATE_BLUE),
	CSSCOLOR_SLATE_GRAY(CSSColor.SLATE_GRAY),
	CSSCOLOR_SLATE_GREY(CSSColor.SLATE_GREY),
	CSSCOLOR_SNOW(CSSColor.SNOW),
	CSSCOLOR_SPRING_GREEN(CSSColor.SPRING_GREEN),
	CSSCOLOR_STEEL_BLUE(CSSColor.STEEL_BLUE),
	CSSCOLOR_TAN(CSSColor.TAN),
	CSSCOLOR_TEAL(CSSColor.TEAL),
	CSSCOLOR_THISTLE(CSSColor.THISTLE),
	CSSCOLOR_TOMATO(CSSColor.TOMATO),
	CSSCOLOR_TURQUOISE(CSSColor.TURQUOISE),
	CSSCOLOR_VIOLET(CSSColor.VIOLET),
	CSSCOLOR_WHEAT(CSSColor.WHEAT),
	CSSCOLOR_WHITE(CSSColor.WHITE),
	CSSCOLOR_WHITE_SMOKE(CSSColor.WHITE_SMOKE),
	CSSCOLOR_YELLOW(CSSColor.YELLOW),
	CSSCOLOR_YELLOW_GREEN(CSSColor.YELLOW_GREEN),

	WS(CharTypeWS.INSTANCE),
	
	// For those CSS types that also take sizes, eg. max-width
	CSS_NONE("none"),
	INITIAL("initial"),
	INHERIT("inherit")
	
	// Styles
	;
	
	private final TokenType tokenType;
	private final char character;
	private final String literal;
	private final String toLiteral;
	private final CharType charType;
	
	// enum tokens
	private final CSStyle element;
	private final CSSUnit unit;
	private final CSSDisplay display;
	private final CSSPosition position;
	private final CSSFloat _float;
	private final CSSTextAlign textAlign;
	private final CSSOverflow overflow;
	private final CSSTextDecoration textDecoration;
	private final CSSFontSize fontSize;
	private final CSSFontWeight fontWeight;
	private final CSSColor color;
	private final CSSForeground foreground;
	private final CSSBackground background;

	private CSSToken(TokenType tokenType) {
		
		if (tokenType != TokenType.NONE && tokenType != TokenType.EOF) {
			throw new IllegalArgumentException("Neither NONE or EOF token: " + tokenType);
		}
		
		this.tokenType = tokenType;
		this.character = 0;
		this.literal = null;
		this.toLiteral = null;
		this.charType = null;
		this.element = null;
		this.unit = null;
		this.display = null;
		this.position = null;
		this._float = null;
		this.textAlign = null;
		this.overflow = null;
		this.textDecoration = null;
		this.fontSize = null;
		this.fontWeight = null;
		this.color = null;
		this.foreground = null;
		this.background = null;
	}
	
	private CSSToken(char character) {
		this.tokenType = TokenType.CHARACTER;
		this.character = character;
		this.literal = null;
		this.toLiteral = null;
		this.charType = null;
		this.element = null;
		this.unit = null;
		this.display = null;
		this.position = null;
		this._float = null;
		this.textAlign = null;
		this.overflow = null;
		this.textDecoration = null;
		this.fontSize = null;
		this.fontWeight = null;
		this.color = null;
		this.foreground = null;
		this.background = null;
	}
	
	private CSSToken(
			String literal,
			CSStyle element,
			CSSUnit unit,
			CSSDisplay display, CSSPosition position, CSSFloat _float, CSSTextAlign textAlign, CSSOverflow overflow, CSSTextDecoration textDecoration,
			CSSFontSize fontSize, CSSFontWeight fontWeight,
			CSSColor color, CSSForeground foreground, CSSBackground background) {
		
		this.tokenType = TokenType.CS_LITERAL;
		this.character = 0;
		this.literal = literal;
		this.toLiteral = null;
		this.charType = null;
		this.element = element;
		this.unit = unit;
		this.display = display;
		this.position = position;
		this._float = _float;
		this.textAlign = textAlign;
		this.overflow = overflow;
		this.textDecoration = textDecoration;
		this.fontSize = fontSize;
		this.fontWeight = fontWeight;
 		this.color = color;
		this.foreground = foreground;
		this.background = background;
	}

	private CSSToken(CSStyle element) {
		this(element.getName(), element, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	private CSSToken(String literal) {
		this(literal, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	private CSSToken(String fromLiteral, String toLiteral) {
		this.tokenType = TokenType.FROM_STRING_TO_STRING;
		this.character = 0;
		this.literal = fromLiteral;
		this.toLiteral = toLiteral;
		this.charType = null;
		this.element = null;
		this.unit = null;
		this.display = null;
		this.position = null;
		this._float = null;
		this.textAlign = null;
		this.overflow = null;
		this.textDecoration = null;
		this.fontSize = null;
		this.fontWeight = null;
		this.color = null;
		this.foreground = null;
		this.background = null;
	}

	private CSSToken(String literal, CSSUnit unit) {
		this(literal, null, unit, null, null, null, null, null, null, null, null, null, null, null);
	}

	private CSSToken(CSSDisplay display) {
		this(display.getName(), null, null, display, null, null, null, null, null, null, null, null, null, null);
	}

	private CSSToken(CSSPosition position) {
		this(position.getName(), null, null, null, position, null, null, null, null, null, null, null, null, null);
	}

	private CSSToken(CSSFloat _float) {
		this(_float.getName(), null, null, null, null, _float, null, null, null, null, null, null, null, null);
	}

	private CSSToken(CSSTextAlign textAlign) {
		this(textAlign.getName(), null, null, null, null, null, textAlign, null, null, null, null, null, null, null);
	}

	private CSSToken(CSSOverflow overflow) {
		this(overflow.getName(), null, null, null, null, null, null, overflow, null, null, null, null, null, null);
	}

	private CSSToken(CSSTextDecoration textDecoration) {
		this(textDecoration.getName(), null, null, null, null, null, null, null, textDecoration, null, null, null, null, null);
	}

	private CSSToken(CSSFontSize fontSize) {
		this(fontSize.getName(), null, null, null, null, null, null, null, null, fontSize, null, null, null, null);
	}

	private CSSToken(CSSFontWeight fontWeight) {
		this(fontWeight.getName(), null, null, null, null, null, null, null, null, null, fontWeight, null, null, null);
	}

	private CSSToken(CSSColor color) {
		this(color.getLowerCaseName(), null, null, null, null, null, null, null, null, null, null, color, null, null);
	}

	private CSSToken(CSSForeground foreground) {
		this(foreground.getName(), null, null, null, null, null, null, null, null, null, null, null, foreground, null);
	}

	private CSSToken(CSSBackground background) {
		this(background.getName(), null, null, null, null, null, null, null, null, null, null, null, null, background);
	}

	private CSSToken(CharType charType) {
		this.tokenType = TokenType.CHARTYPE;
		this.character = 0;
		this.literal = null;
		this.toLiteral = null;
		this.charType = charType;
		this.element = null;
		this.unit = null;
		this.display = null;
		this.position = null;
		this._float = null;
		this.textAlign = null;
		this.overflow = null;
		this.textDecoration = null;
		this.fontSize = null;
		this.fontWeight = null;
		this.color = null;
		this.foreground = null;
		this.background = null;
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
		return literal;
	}

	@Override
	public String getToLiteral() {
		return toLiteral;
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
	
	public CSSTextDecoration getTextDecoration() {
		return textDecoration;
	}

	public CSSFontSize getFontSize() {
		return fontSize;
	}

	public CSSFontWeight getFontWeight() {
		return fontWeight;
	}

	public CSSColor getColor() {
		return color;
	}

	public CSSForeground getForeground() {
		return foreground;
	}

	public CSSBackground getBackground() {
		return background;
	}
}
