package com.test.web.parse.css;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.test.web.css.common.CSSGradientColorStop;
import com.test.web.css.common.enums.CSSBackgroundAttachment;
import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSBackgroundImage;
import com.test.web.css.common.enums.CSSBackgroundOrigin;
import com.test.web.css.common.enums.CSSBackgroundPosition;
import com.test.web.css.common.enums.CSSBackgroundRepeat;
import com.test.web.css.common.enums.CSSBackgroundSize;
import com.test.web.css.common.enums.CSSColor;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSPositionComponent;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io.common.CharInput;
import com.test.web.parse.common.BaseParser;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.LexerMatch;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.common.TokenMergeHelper;
import com.test.web.parse.css.CSSParserHelperFunction.IParseParam;
import com.test.web.types.DecimalSize;
import com.test.web.types.Value;

/**
 * For parsing CSS from file or from a style attribute
 * @author nhl
 *
 */
public class CSSParser<LISTENER_CONTEXT> extends BaseParser<CSSToken, CharInput> {
	
	private final Lexer<CSSToken, CharInput> lexer; 
	private final CSSParserListener<LISTENER_CONTEXT> listener;

	public CSSParser(Lexer<CSSToken, CharInput> lexer, CSSParserListener<LISTENER_CONTEXT> listener) {
		super(lexer, CSSToken.WS);
		
		this.lexer = getLexer();
		this.listener = listener;
	}
	
	public static Lexer<CSSToken, CharInput> createLexer(CharInput input) {
		return new Lexer<CSSToken, CharInput>(input, CSSToken.class, CSSToken.NONE, CSSToken.EOF);
	}
		
	public CSSParser(CharInput input, CSSParserListener<LISTENER_CONTEXT> listener) {
		this(createLexer(input), listener);
	}
	
	@SafeVarargs
	private final CSSToken lexSkipWSAndComment(CSSToken ... tokens) throws IOException {
		return CSSParserHelperWS.lexSkipWSAndComment(lexer, tokens);
	}

	public void parseCSS() throws IOException, ParserException {
		
		boolean done = false;
		do {
			CSSToken token = lexer.lex(CSSToken.WS, CSSToken.ID_MARKER, CSSToken.CLASS_MARKER, CSSToken.TAG, CSSToken.COMMENT, CSSToken.EOF);
			
			switch (token) {
			case ID_MARKER:
			case CLASS_MARKER:
			case TAG:
				final LISTENER_CONTEXT context = listener.onBlockStart();
				parseMarkedBlock(token, context);
				listener.onBlockEnd(context);
				break;
				
			case WS:
			case COMMENT:
				// skip
				break;
				
			case EOF:
				done = true;
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
			
		} while (!done);
	}
	
	private void parseMarkedBlock(CSSToken initialMarker, LISTENER_CONTEXT context) throws IOException, ParserException {
		// First read the initial marker
		parseSelector(initialMarker, context);
		
		// Parse additional markers in a loop
		boolean done = false;
		
		do {
			CSSToken token = lexer.lex(CSSToken.WS, CSSToken.ID_MARKER, CSSToken.CLASS_MARKER, CSSToken.TAG, CSSToken.COMMA, CSSToken.COMMENT, CSSToken.BRACKET_START);
			
			if (token == CSSToken.WS) {
				// Just continue in case of WS
				continue;
			}
			
			switch (token) {
			case ID_MARKER:
			case CLASS_MARKER:
			case TAG:
				parseSelector(token, context);
				break;
				
			case COMMENT:
				break;
				
			case COMMA:
				// multiple selectors
				break;
				
			case BRACKET_START:
				done = true;
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
			
		} while (!done);

		// Done, parse block
		parseBlockContents(context);
	}
	
	private void parseSelector(CSSToken marker, LISTENER_CONTEXT context) throws IOException, ParserException {
		switch (marker) {
		case ID_MARKER: {
			final CSSToken token = lexer.lex(CSSToken.ID);
			
			switch (token) {
			case ID:
				listener.onEntityMap(context, CSSTarget.ID, lexer.get());
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
			break;
		}
			
		case CLASS_MARKER: {
			final CSSToken token = lexer.lex(CSSToken.CLASS);
			
			switch (token) {
			case CLASS:
				listener.onEntityMap(context, CSSTarget.CLASS, lexer.get());
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
			break;
		}
			
		case TAG:
			listener.onEntityMap(context, CSSTarget.TAG, lexer.get());
			break;
			
		default:
			throw new IllegalStateException("Unknown marker " + marker);
		}
	}
	
	private static final CSSToken [] STYLE_TOKENS = copyTokens(token -> token.getElement() != null, CSSToken.BROWSER_SPECIFIC_ATTRIBUTE);
	
	private static final CSSToken [] BLOCK_TOKENS = copyTokens(
			token -> token.getElement() != null,
			CSSToken.BROWSER_SPECIFIC_ATTRIBUTE,
			CSSToken.WS,
			CSSToken.COMMENT,
			CSSToken.BRACKET_END);


	private void parseBlock(LISTENER_CONTEXT context) throws IOException, ParserException {
		// Parse each item within CSS
		
		CSSToken token = lexSkipWSAndComment(CSSToken.BRACKET_START);
		

		if (token != CSSToken.BRACKET_START) {
			throw lexer.unexpectedToken();
		}
		
		parseBlockContents(context);
	}

	private void parseBlockContents(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		// Now parse all fields in CSS until end-bracket
		CSSToken token;
		
		boolean done = false;
		do {
			token = lexer.lex(BLOCK_TOKENS);
			
			switch (token) {
			case WS:
				// Just skip any whitespace
				break;
				
			case BROWSER_SPECIFIC_ATTRIBUTE:
				// skip
				break;
				
			case BRACKET_END:
				// End of CSS block
				done = true;
				break;
			
			case COMMENT:
				break;
				
			case NONE:
				// TODO perhaps just skip block
				throw lexer.unexpectedToken();
				
			default:
				// A CSS element
				parseElement(context, token.getElement());
				break;
			}
			
		} while (!done);
	}

	public void parseElement(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		final CSSToken token = lexSkipWS(STYLE_TOKENS);
		
		if (token == CSSToken.NONE) {
			throw new ParserException("No CSS style token found");
		}
		
		if (token == CSSToken.BROWSER_SPECIFIC_ATTRIBUTE) {
			// starts with '-'
		}
		else {
			parseElementWithoutCheckingForSemiColon(context, token.getElement());
		}
	}
	
	private static final CSSUnit defaultWidthHeightUnit = CSSUnit.PX;
	
	private void parseElementWithoutCheckingForSemiColon(LISTENER_CONTEXT context, CSStyle element) throws IOException, ParserException {
		CSSToken token;
		
		for (;;) {
			token = lexSkipWS(CSSToken.COLON, CSSToken.COMMENT);
		
			if (token == CSSToken.COLON) {
				break;
			}
			
			if (token != CSSToken.COMMENT) {
				throw lexer.unexpectedToken();
			}
		}
		
		CSSParserHelperWS.skipAnyWS(lexer);

		// Now read value, this depends on input style

		switch (element) {
		case WIDTH:
			CSSParserHelperSizeToSemicolon.parsePossiblyDecimalSizeValue(lexer, defaultWidthHeightUnit, (size, unit) -> listener.onWidth(context, size, unit));
			break;
			
		case HEIGHT:
			CSSParserHelperSizeToSemicolon.parsePossiblyDecimalSizeValue(lexer, defaultWidthHeightUnit, (size, unit) -> listener.onHeight(context, size, unit));
			break;

		case COLOR:
			parseFgColor(
					(r, g, b, a) -> listener.onColor(context, r, g, b, a),
					cssColor -> listener.onColor(context, cssColor),
					type -> listener.onColor(context, type));
			break;
			
		case BACKGROUND_COLOR:
			parseBgColor(
					(r, g, b, a) -> listener.onBgColor(context, r, g, b, a),
					cssColor -> listener.onBgColor(context, cssColor),
					type -> listener.onBgColor(context, type));
			break;
			
		case BACKGROUND_IMAGE:
			parseBgImage(context);
			break;

		case BACKGROUND_POSITION:
			parseBgPosition(context);
			break;
			
		case BACKGROUND_SIZE:
			parseBgSize(context);
			break;

		case BACKGROUND_REPEAT:
			parseBgRepeat(context);
			break;

		case BACKGROUND_ATTACHMENT:
			parseBgAttachment(context);
			break;

		case BACKGROUND_ORIGIN:
			parseBgOrigin(context);
			break;

		case BACKGROUND_CLIP:
			parseBgClip(context);
			break;
			
		case BACKGROUND:
			parseBackground(context);
			break;

		case MARGIN_LEFT:
			CSSParserHelperMargin.parseSizeOrAutoOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onMarginLeft(context, size, unit, justify));
			break;
			
		case MARGIN_RIGHT:
			CSSParserHelperMargin.parseSizeOrAutoOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onMarginRight(context, size, unit, justify));
			break;

		case MARGIN_TOP:
			CSSParserHelperMargin.parseSizeOrAutoOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onMarginTop(context, size, unit, justify));
			break;

		case MARGIN_BOTTOM:
			CSSParserHelperMargin.parseSizeOrAutoOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onMarginBottom(context, size, unit, justify));
			break;
			
		case MARGIN:
			parseMargin(context);
			break;
			
		case PADDING_LEFT:
			CSSParserHelperMargin.parseSizeOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onPaddingLeft(context, size, unit, justify));
			break;
			
		case PADDING_RIGHT:
			CSSParserHelperMargin.parseSizeOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onPaddingRight(context, size, unit, justify));
			break;

		case PADDING_TOP:
			CSSParserHelperMargin.parseSizeOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onPaddingTop(context, size, unit, justify));
			break;

		case PADDING_BOTTOM:
			CSSParserHelperMargin.parseSizeOrInitialOrInherit(lexer, (size, unit, justify) -> listener.onPaddingBottom(context, size, unit, justify));
			break;
			
		case PADDING:
			parsePadding(context);
			break;

		case FLOAT:
			parseFloat(context);
			break;
	
		case CLEAR:
			parseClear(context);
			break;
			
		case POSITION:
			parsePosition(context);
			break;
			
		case TEXT_ALIGN:
			parseTextAlign(context);
			break;
			
		case MAX_WIDTH:
			parseMax((size, unit, type) -> listener.onMaxWidth(context, size, unit, type));
			break;
			
		case MAX_HEIGHT:
			parseMax((size, unit, type) -> listener.onMaxHeight(context, size, unit, type));
			break;
			
		case MIN_WIDTH:
			parseMin((size, unit, type) -> listener.onMinWidth(context, size, unit, type));
			break;
			
		case MIN_HEIGHT:
			parseMin((size, unit, type) -> listener.onMinHeight(context, size, unit, type));
			break;
			
		case FONT_SIZE:
			parseFontSize(context);
			break;
			
		case FONT_WEIGHT:
			parseFontWeight(context);
			break;
			
		case TEXT_DECORATION:
			parseTextDecoration(context);
			break;

		case FILTER:
			parseFilter(context);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown element " + element);
		}
	}

	private void parseElement(LISTENER_CONTEXT context, CSStyle element) throws IOException, ParserException {

		parseElementWithoutCheckingForSemiColon(context, element);
		
		assureTokenSkipWSAndComment(CSSToken.SEMICOLON);
	}
	
	@FunctionalInterface
	interface InitialMarginOrPaddingParser {
		void parse(Lexer<CSSToken, CharInput> lexer, IJustifyFunction justifyFunction) throws IOException, ParserException;
	}
	
	private void parseMargin(LISTENER_CONTEXT context) throws IOException, ParserException {
		CSSParserHelperMargin.parseMarginOrPadding(lexer, context, listener::onMargin, CSSParserHelperMargin::parseSizeOrAutoOrInitialOrInherit);
	}

	private void parsePadding(LISTENER_CONTEXT context) throws IOException, ParserException {
		CSSParserHelperMargin.parseMarginOrPadding(lexer,context, listener::onPadding, CSSParserHelperMargin::parseSizeOrInitialOrInherit);
	}


	private static final CSSUnit minMaxDefaultUnit = CSSUnit.PX;

	private void parseMax(IMaxFunction toCall) throws IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER, CSSToken.CSS_NONE, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT);
		
		final BiConsumer<Integer, CSSUnit> sizeCallback = (size, unit) -> toCall.onMax(size, unit, CSSMax.SIZE);
		
		switch (token) {
		case INTEGER:
			final int intValue = Integer.parseInt(lexer.get());
			CSSParserHelperSizeToSemicolon.parseSizeValueAfterInt(lexer, minMaxDefaultUnit, intValue, sizeCallback);
			break;
			
		case NONE:
			toCall.onMax(0, null, CSSMax.NONE);
			break;
	
		case INITIAL:
			toCall.onMax(0, null, CSSMax.INITIAL);
			break;
			
		case INHERIT:
			toCall.onMax(0, null, CSSMax.INHERIT);
			break;
			
		case DOT:
			CSSParserHelperSizeToSemicolon.parseDecimalAfterDot(lexer, minMaxDefaultUnit, 0, sizeCallback);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
	}

	private void parseMin(IMinFunction toCall) throws IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT);
		
		final BiConsumer<Integer, CSSUnit> sizeCallback = (size, unit) -> toCall.onMin(size, unit, CSSMin.SIZE);
		
		switch (token) {
		case INTEGER:
			final int intValue = Integer.parseInt(lexer.get());
			CSSParserHelperSizeToSemicolon.parseSizeValueAfterInt(lexer, minMaxDefaultUnit, intValue, sizeCallback);
			break;
			
		case INITIAL:
			toCall.onMin(0, null, CSSMin.INITIAL);
			break;
			
		case INHERIT:
			toCall.onMin(0, null, CSSMin.INHERIT);
			break;
			
		case DOT:
			CSSParserHelperSizeToSemicolon.parseDecimalAfterDot(lexer, minMaxDefaultUnit, 0, sizeCallback);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
	}
	
	private static CSSToken [] copyTokens(Predicate<CSSToken> test, CSSToken ... extra) {
		return TokenMergeHelper.copyTokens(CSSToken.class, test, extra);
	}
	
	private static final CSSToken [] COLOR_TOKENS = copyTokens(token -> token.getColor() != null, CSSToken.COLOR_MARKER, CSSToken.FUNCTION_RGB, CSSToken.FUNCTION_RGBA);

	private static final CSSToken [] FG_COLOR_TOKENS = copyTokens(token -> token.getForeground() != null, COLOR_TOKENS);

	private static final CSSToken [] BG_COLOR_TOKENS = copyTokens(token -> token.getBackground() != null, COLOR_TOKENS);

	private void parseFgColor(IColorRGBFunction rgbColor, ICSSColorFunction cssColor, Consumer<CSSForeground> colorType) throws IOException, ParserException {
		final CSSToken token = CSSParserHelperColor.parseColor(lexer, rgbColor, cssColor, FG_COLOR_TOKENS);
		
		if (token.getForeground() != null) {
			colorType.accept(token.getForeground());
		}
	}

	private void parseBgColor(IColorRGBFunction rgbColor, ICSSColorFunction cssColor, Consumer<CSSBackgroundColor> colorType) throws IOException, ParserException {
		final CSSToken token = CSSParserHelperColor.parseColor(lexer, rgbColor, cssColor, BG_COLOR_TOKENS);
		
		if (token.getBackground() != null) {
			// special type, call callback
			colorType.accept(token.getBackground());
		}
	}

	private static final CSSToken [] BG_IMAGE_TOKENS = copyTokens(token -> token.getBgImage() != null,
				CSSToken.FUNCTION_URL,
				CSSToken.FUNCTION_LINEAR_GRADIENT,
				CSSToken.BROWSER_SPECIFIC_FUNCTION); // eg -moz-linear-gradient
	
	private void parseBgImage(LISTENER_CONTEXT context) throws IOException, ParserException {
	
		int bgLayer = 0;
		boolean commaRead = false;
		
		do {
			CSSToken token = lexSkipWSAndComment(BG_IMAGE_TOKENS);
			
			switch (token) {
			case FUNCTION_URL:
				// parse function params
				final String url = parseImageURL();
				
				listener.onBgImageURL(context, bgLayer, url);
				
				commaRead = readComma();
				break;

			case FUNCTION_LINEAR_GRADIENT:
				parseLinearGradient(context, bgLayer);

				commaRead = readComma();
				break;
				
			case NONE:
				throw lexer.unexpectedToken();
				
			default:
				if (token.getBgImage() == null) {
					throw new IllegalStateException("Expected bg image");
				}
				listener.onBgImage(context, bgLayer,  token.getBgImage());

				commaRead = readComma();
				break;
			}
			
			++ bgLayer;
			
		} while (commaRead);
	}
	
	private String parseImageURL() throws IOException, ParserException {
		
		final Object [] values = CSSParserHelperFunction.parseFunctionParams(lexer, 1, paramIdx -> parseQuotedString());
		
		final String url = (String)values[0];

		return url;
	}
	
	private static final CSSToken [] POSITION_COMPONENT_TOKENS = TokenMergeHelper.copyTokens(CSSToken.class, token -> token.getPositionComponent() != null);
	
	private static class ParsedPositionComponents {
		private final CSSPositionComponent pos1;
		private final CSSPositionComponent pos2;

		ParsedPositionComponents(CSSPositionComponent pos1, CSSPositionComponent pos2) {
			this.pos1 = pos1;
			this.pos2 = pos2;
		}
	}
	
	private void parseLinearGradient(LISTENER_CONTEXT context, int bgLayer) throws IOException, ParserException {
		// starts with an angle or diagonal description, then an 1 to unlimited number of color stops

		final CachedSize cachedSize = new CachedSize();
		final CachedRGBA cachedRGBA = new CachedRGBA();
		final Value<CSSColor>cachedColor = new Value<>();
		
		final IParseParam parseParam = paramIdx -> {
			final Object ret;
			
			final Object obj;
			
			if (paramIdx == 0) {
				obj = parseGradientDirection();
			}
			else {
				obj = null;
			}
			
			if (obj != null) {
				ret = obj;
			}
			else {
				// color stop which is color followed by optional length or percent
				// since percent is a unit, just use standard parsing
				
				CSSParserHelperColor.parseColor(
						lexer,
						(r, g, b, a) -> cachedRGBA.init(r, g, b, a),
						cssColor -> cachedColor.set(cssColor),
						COLOR_TOKENS);
				
				final CSSToken intToken = lexSkipWSAndComment(CSSToken.INTEGER);
				
				if (intToken == CSSToken.INTEGER) {
				
					CSSParserHelperSize.parseSizeValueAfterInt(lexer, null, Integer.parseInt(lexer.get()), (value, unit) -> cachedSize.init(value, unit));
					
					if (cachedSize.getUnit() == null) {
						throw new ParserException("No unit was specified for color stop");
					}
				}
				
				if (cachedRGBA.isInitialized()) {
					ret = new CSSGradientColorStop(cachedRGBA.getR(), cachedRGBA.getG(), cachedRGBA.getB(), cachedRGBA.getA(), cachedSize.getValue(), cachedSize.getUnit());

					cachedRGBA.clear();
				}
				else {
					ret =new CSSGradientColorStop(cachedColor.get(), cachedSize.getValue(), cachedSize.getUnit()); 
				}
				
				cachedSize.clear();
				cachedColor.clear();
			}

			return ret;
		};
		
		final Object [] result = CSSParserHelperFunction.parseUnknownNumberOfFunctionParams(lexer, parseParam);
		

		final Object direction = result[0];
		
		int firstColorStop;

		if (direction instanceof CSSGradientColorStop) {
			if (result.length < 2) {
				throw new ParserException("linear-gradient without angle or direction must have at least two colorstops");
			}
			firstColorStop = 0;
		}
		else {
			if (result.length < 3) {
				throw new ParserException("linear-gradient must have at least an angle or direction and two colorstops");
			}
			firstColorStop = 1;
		}
		
		final CSSGradientColorStop [] colorStops = new CSSGradientColorStop[result.length - firstColorStop];
		
		for (int i = firstColorStop; i < result.length; ++ i) {
			colorStops[i - firstColorStop] = (CSSGradientColorStop)result[i];
		}
		
		if (colorStops[0].hasDistance() || colorStops[colorStops.length - 1].hasDistance()) {
			throw new ParserException("First and last colorstop cannot have length");
		}
		
		if (direction instanceof Integer) {
			listener.onBgGradient(context, bgLayer, (Integer)direction, colorStops);
		}
		else if (direction instanceof CSSPositionComponent) {
				listener.onBgGradient(context, bgLayer, (CSSPositionComponent)direction, null, colorStops);
		}
		else if (direction instanceof ParsedPositionComponents) {
			final ParsedPositionComponents c = (ParsedPositionComponents)direction;
			
			listener.onBgGradient(context, bgLayer, c.pos1, c.pos2, colorStops);
		}
		else if (direction instanceof CSSGradientColorStop){
			// colorstop
			listener.onBgGradient(context, bgLayer, colorStops);
		}
		else {
			throw new IllegalStateException("unknown direction type");
		}
	}
	
	private Object parseGradientDirection() throws IOException, ParserException {

		final Object ret;
		
		// positions or angle
		final CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER, CSSToken.TO);
		
		switch (token) {
		case INTEGER:
			ret = Integer.parseInt(lexer.get());
			assureTokenSkipWSAndComment(CSSToken.RADIX_DEG);
			break;
			
		case TO:
			// must parse one or two position components
			final CSSToken pos1Token = lexSkipWSAndComment(POSITION_COMPONENT_TOKENS);
			if (pos1Token == CSSToken.NONE) {
				throw lexer.unexpectedToken();
			}
			
			// Check for pos2 as well
			final CSSToken pos2Token = lexSkipWSAndComment(POSITION_COMPONENT_TOKENS);
			if (pos2Token == CSSToken.NONE) {
				ret = pos1Token.getPositionComponent();
			}
			else {
				final CSSPositionComponent pos1 = pos1Token.getPositionComponent();
				final CSSPositionComponent pos2 = pos2Token.getPositionComponent();
				
				// verify so that fails in parsing and can skip to next CSS attribute
				if (pos1 == CSSPositionComponent.CENTER) {
					throw new ParserException("pos1 is center");
				}

				if (pos2 != null) {
					if (pos2 == CSSPositionComponent.CENTER) {
						throw new ParserException("pos2 is center");
					}
					
					if (pos1 == pos2) {
						throw new ParserException("pos1 == pos2");
					}
				}

				final ParsedPositionComponents bgPosition = new ParsedPositionComponents(pos1, pos2);

				// return as temporary instance
				ret = bgPosition;
			}
			break;
			
		default:
			ret = null; // tokens did not match so probably color
			break;
		}
		
		return ret;
	}
	
	private String parseQuotedString() throws IOException, ParserException {
		
		// parse until quote
		final CSSToken token = lexSkipWSAndComment(CSSToken.QUOTED_STRING);
		
		if (token != CSSToken.QUOTED_STRING) {
			throw lexer.unexpectedToken();
		}
		
		final String withQuotes = lexer.get();
		
		// remove quotes
		return withQuotes.substring(1, withQuotes.length() - 1);
	}

	private static final CSSToken [] BG_POSITION_TOKENS
		= copyTokens(token -> token.getPositionComponent() != null,
					CSSToken.INTEGER,
					CSSToken.INITIAL,
					CSSToken.INHERIT);

	private static final CSSToken [] BG_SECOND_POSITION_COMPONENT_TOKENS
		= copyTokens(token -> token.getPositionComponent() != null,
					CSSToken.COMMA);

	private void parseBgPositionAfterInt(LISTENER_CONTEXT context, int bgLayer, CachedSize cachedSize) throws IOException, ParserException {
		
		// we got integer so a regular pos specification, parse any decimal fraction and unit as well
		CSSParserHelperSizeToSemicolon.parseSizeValueAfterInt(lexer, posAndSizeDefaultUnit, Integer.parseInt(lexer.get()), (value, unit) -> cachedSize.init(value, unit));
		
		final int bgl = bgLayer;
		// Now should be another position value
		CSSParserHelperSizeToSemicolon.parsePossiblyDecimalSizeValue(lexer, posAndSizeDefaultUnit, (value, unit) -> listener.onBgPosition(context, bgl, cachedSize.getValue(), cachedSize.getUnit(), value, unit));
	}

	private static final CSSUnit posAndSizeDefaultUnit = CSSUnit.PX;
	
	private void parseBgPosition(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		int bgLayer = 0;
		boolean commaRead = false;
		
		final CachedSize cachedSize = new CachedSize();
		
		do {
			CSSToken token = lexSkipWSAndComment(BG_POSITION_TOKENS);
			
			switch (token) {
			case INTEGER:
				parseBgPositionAfterInt(context, bgLayer, cachedSize);
				
				// next should be comma for new layer or a semicolon
				commaRead = readComma();
				break;
				
			case INITIAL:
			case INHERIT:
				listener.onBgPosition(context, bgLayer, tokenToPosition(token));
				
				// next should be comma for new layer or a semicolon
				commaRead = readComma();
				break;
				
			case NONE:
				throw lexer.unexpectedToken();
				
			default:
				// Position component
				if (token.getPositionComponent() == null) {
					throw new IllegalStateException("Expected position component");
				}
				
				// another position component, comma for next layer or semicolon
				CSSToken secondPosToken = lexSkipWSAndComment(BG_SECOND_POSITION_COMPONENT_TOKENS);
				
				switch (secondPosToken) {
				
				case COMMA:
					// Just continues to next
					notifyPosition(context, bgLayer, token.getPositionComponent());
					commaRead = true;
					break;
					
				case NONE:
					notifyPosition(context, bgLayer, token.getPositionComponent());
					break;
					
					
				default:
					// Position component
					if (secondPosToken.getPositionComponent() == null) {
						throw new IllegalStateException("Expected position component");
					}
					notifyPosition(context, bgLayer, token.getPositionComponent(), secondPosToken.getPositionComponent());
					
					commaRead = readComma();
					break;
				}
				break;
			}
			
			++ bgLayer;
			
		} while (commaRead);
	}

	private static final CSSToken [] BG_SIZE_TOKENS = copyTokens(token -> token.getBgSize() != null, CSSToken.INTEGER);

	private void parseBgSize(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		int bgLayer = 0;
		boolean commaRead = false;
		
		final CachedSize cachedSize = new CachedSize();
		
		do {
			
			commaRead = parseBgSize(context, bgLayer, cachedSize, true);
		
			++ bgLayer;
			
		} while (commaRead);
	}
	
	private boolean parseBgSize(LISTENER_CONTEXT context, int bgLayer, CachedSize cachedSize, boolean readComma) throws IOException, ParserException  {
		CSSToken token = lexSkipWSAndComment(BG_SIZE_TOKENS);
		
		boolean commaRead = false;
		
		switch (token) {
		case INTEGER:
			// we got integer so a regular size specification, parse any decimal fraction and unit as well
			CSSParserHelperSizeToSemicolon.parseSizeValueAfterInt(lexer, posAndSizeDefaultUnit, Integer.parseInt(lexer.get()), (value, unit) -> cachedSize.init(value, unit));
			
			final int bgl = bgLayer;
			// Now should be another size value
			CSSParserHelperSizeToSemicolon.parsePossiblyDecimalSizeValue(lexer, posAndSizeDefaultUnit, (value, unit) -> listener.onBgSize(context, bgl, cachedSize.getValue(), cachedSize.getUnit(), value, unit));
			
			if (readComma) {
				// next should be comma for new layer or a semicolon
				commaRead = readComma();
			}
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			// Size enum
			if (token.getBgSize() == null) {
				throw new IllegalStateException("Expected size: " + token);
			}
			listener.onBgSize(context, bgLayer, token.getBgSize());
			
			if (readComma) {
				commaRead = readComma();
			}
			break;
		}
		
		return commaRead;
	}
	
	private static final CSSToken [] BG_REPEAT_TOKENS = copyTokens(token -> token.getBgRepeat() != null);

	private void parseBgRepeat(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseBgEnum(context, BG_REPEAT_TOKENS, CSSToken::getBgRepeat, listener::onBgRepeat);
	}

	private static final CSSToken [] BG_ATTACHMENT_TOKENS = copyTokens(token -> token.getBgAttachment() != null);

	private void parseBgAttachment(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseBgEnum(context, BG_ATTACHMENT_TOKENS, CSSToken::getBgAttachment, listener::onBgAttachment);
	}

	private static final CSSToken [] BG_ORIGIN_TOKENS = copyTokens(token -> token.getBgOrigin() != null);

	private void parseBgOrigin(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseBgEnum(context, BG_ORIGIN_TOKENS, CSSToken::getBgOrigin, listener::onBgOrigin);
	}

	private void parseBgClip(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseBgEnum(context, BG_ORIGIN_TOKENS, CSSToken::getBgOrigin, listener::onBgClip);
	}

	@FunctionalInterface
	interface ParseBgEnumResult<CONTEXT, E extends Enum<E>> {
		void onResult(CONTEXT context, int bgLayer, E value);
	}
	
	private <E extends Enum<E>> void parseBgEnum(
			LISTENER_CONTEXT context,
			CSSToken [] tokens,
			Function<CSSToken, E> getValue,
			ParseBgEnumResult<LISTENER_CONTEXT, E> processResult) throws IOException, ParserException {
	
		int bgLayer = 0;
		boolean commaRead = false;
		
		do {
			CSSToken token = lexSkipWSAndComment(tokens);
			
			final E value = getValue.apply(token);
			
			if (value == null) {
				throw lexer.unexpectedToken();
			}
			
			processResult.onResult(context, bgLayer, value);
			commaRead = readComma();
			
			++ bgLayer;
			
		} while (commaRead);
	}
	
	private static final Map<CSStyle, CSSToken[]> BG_INITIAL_TOKENS;
	
	static {
		BG_INITIAL_TOKENS = new HashMap<>();
		
		BG_INITIAL_TOKENS.put(CSStyle.BACKGROUND_IMAGE, BG_IMAGE_TOKENS);
		BG_INITIAL_TOKENS.put(CSStyle.BACKGROUND_POSITION, BG_POSITION_TOKENS);
		BG_INITIAL_TOKENS.put(CSStyle.BACKGROUND_REPEAT, BG_REPEAT_TOKENS);
		BG_INITIAL_TOKENS.put(CSStyle.BACKGROUND_ATTACHMENT, BG_ATTACHMENT_TOKENS);
		BG_INITIAL_TOKENS.put(CSStyle.BACKGROUND_ORIGIN, BG_ORIGIN_TOKENS); // clip only if origin is specified
		BG_INITIAL_TOKENS.put(CSStyle.BACKGROUND_COLOR, BG_COLOR_TOKENS);
	}
	
	// Must pass a combination of many tokens since order may vary
	private void parseBackground(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		
		int bgLayer = 0;
		boolean commaRead = false;
		
		do {
			commaRead = parseOneLayer(context, bgLayer);
		
			++ bgLayer;
		} while (commaRead);
	}
	
	private boolean parseOneLayer(LISTENER_CONTEXT context, int bgLayer) throws IOException, ParserException {

		// Maintain a map of which tokens to still try
		final Map<CSStyle, CSSToken[]> tokenMap = new HashMap<>(BG_INITIAL_TOKENS);

		boolean layerDone = false;
		boolean commaRead = false;
		
		final Value<Integer> numOrigin = new Value<>(0);
		
		do {
			// parse for next until reaches comma or semicolon
			final CSSToken lastToken = parseOneLayerIteration(context, bgLayer, tokenMap, numOrigin);
			
			if (lastToken == CSSToken.COMMA) {
				commaRead = true;
				layerDone = true;
			}
			else if (lastToken == CSSToken.NONE) {
				layerDone = true;
			}
			
		} while (!layerDone);
		
		return commaRead;
	}
	
	private CSSToken parseOneLayerIteration(LISTENER_CONTEXT context, int bgLayer, Map<CSStyle, CSSToken[]> tokenMap, Value<Integer> numOrigin) throws IOException, ParserException {
		
		final CSSToken [] tokens = TokenMergeHelper.merge(tokenMap.values(), CSSToken.COMMA);
		
		CSSToken token = lexSkipWSAndComment(tokens);
		
		
		final String text = lexer.get();
		
		// Just check textual value for "initial" and "inherit" since these are part of multiple of the enum tokens
		// but we should just set inherit for all in that case
		
		if (text.equalsIgnoreCase("initial")) {
			setValue(
					context, bgLayer,
					CSSBackgroundImage.INITIAL,
					CSSBackgroundPosition.INITIAL,
					CSSBackgroundSize.INITIAL,
					CSSBackgroundRepeat.INITIAL,
					CSSBackgroundAttachment.INITIAL,
					CSSBackgroundOrigin.INITIAL,
					CSSBackgroundOrigin.INITIAL,
					CSSBackgroundColor.INITIAL);
		}
		else if (text.equalsIgnoreCase("inherit")) {
			setValue(
					context, bgLayer,
					CSSBackgroundImage.INHERIT,
					CSSBackgroundPosition.INHERIT,
					CSSBackgroundSize.INHERIT,
					CSSBackgroundRepeat.INHERIT,
					CSSBackgroundAttachment.INHERIT,
					CSSBackgroundOrigin.INHERIT,
					CSSBackgroundOrigin.INHERIT,
					CSSBackgroundColor.INHERIT);
		}
		else if (token == CSSToken.COMMA) {
			// return token so that we can start new layer, or exit in case of semicolon
		}
		// None of "initial" or "inherit", figure out the token
		else if (token == CSSToken.FUNCTION_URL) {
			// URL for image
			final String url = parseImageURL();
			
			listener.onBgImageURL(context, bgLayer, url);
			
			tokenMap.remove(CSStyle.BACKGROUND_IMAGE);
		}
		else if (token == CSSToken.BROWSER_SPECIFIC_FUNCTION) {
			// skip parameters
			skipFunctionParamsAfterParenthesis();
			tokenMap.remove(CSStyle.BACKGROUND_IMAGE);
		}
		else if (token.getBgImage() != null) {
			listener.onBgImage(context, bgLayer, token.getBgImage());
			
			tokenMap.remove(CSStyle.BACKGROUND_IMAGE);
		}
		else if (token == CSSToken.FUNCTION_LINEAR_GRADIENT) {
			parseLinearGradient(context, bgLayer);

			tokenMap.remove(CSStyle.BACKGROUND_IMAGE);
		}
		else if (token == CSSToken.INTEGER) {
			// position with possible size as well after '/'
			
			final CachedSize cachedSize = new CachedSize();
			
			parseBgPositionAfterInt(context, bgLayer, cachedSize);

			tokenMap.remove(CSStyle.BACKGROUND_POSITION);
			
			// Now we might have '/' for size
			token = lexSkipWSAndComment(CSSToken.SLASH);
			if (token == CSSToken.SLASH) {
				// we should now have size
				parseBgSize(context, bgLayer, cachedSize, false);
			}
		}
		else if (token.getBgRepeat() != null) {
			listener.onBgRepeat(context, bgLayer, token.getBgRepeat());

			tokenMap.remove(CSStyle.BACKGROUND_REPEAT);
		}
		else if (token.getBgAttachment() != null) {
			listener.onBgAttachment(context, bgLayer, token.getBgAttachment());

			tokenMap.remove(CSStyle.BACKGROUND_ATTACHMENT);
		}
		else if (token.getBgOrigin() != null) {
			
			if (numOrigin.get() == 0) {
				listener.onBgOrigin(context, bgLayer, token.getBgOrigin());
				
				numOrigin.set(numOrigin.get() + 1);
			}
			else if (numOrigin.get() == 1) {
				listener.onBgClip(context, bgLayer, token.getBgOrigin());

				tokenMap.remove(CSStyle.BACKGROUND_ORIGIN);
			}
			else {
				throw new IllegalStateException("should never reach here since removed from tokenMap");
			}
		}
		else if (token == CSSToken.COLOR_MARKER) {

			token = CSSParserHelperColor.parseHexColor(lexer, (r, g, b, a) -> listener.onBgColor(context, r, g, b, a));
			
			tokenMap.remove(CSStyle.BACKGROUND_COLOR);
		}
		else if (token.getBackground() != null) {
			
			listener.onBgColor(context, token.getBackground());
			
			tokenMap.remove(CSStyle.BACKGROUND_COLOR);
		}
		else if (token.getColor() != null) {
			
			listener.onBgColor(context, token.getColor());
			
			tokenMap.remove(CSStyle.BACKGROUND_COLOR);
		}
		else if (token == CSSToken.FUNCTION_RGB) {
			
			CSSParserHelperColor.parseRGBFunction(lexer, (r, g, b, a) -> listener.onBgColor(context, r, g, b, a));
			
			tokenMap.remove(CSStyle.BACKGROUND_COLOR);
		}
		else if (token == CSSToken.FUNCTION_RGBA) {

			CSSParserHelperColor.parseRGBAFunction(lexer, (r, g, b, a) -> listener.onBgColor(context, r, g, b, a));

			tokenMap.remove(CSStyle.BACKGROUND_COLOR);
		}
		else if (token == CSSToken.NONE) {
			// probably semicolon
		}
		else {
			throw lexer.unexpectedToken();
		}
		
		return token;
	}
	
	private void setValue(LISTENER_CONTEXT context, int bgLayer,
			CSSBackgroundImage bgImage,
			CSSBackgroundPosition bgPosition,
			CSSBackgroundSize bgSize,
			CSSBackgroundRepeat bgRepeat,
			CSSBackgroundAttachment bgAttachment,
			CSSBackgroundOrigin bgOrigin,
			CSSBackgroundOrigin bgClip,
			CSSBackgroundColor bgColor) {
		
		listener.onBgImage(context, bgLayer, bgImage);
		listener.onBgPosition(context, bgLayer, bgPosition);
		listener.onBgSize(context, bgLayer, bgSize);
		listener.onBgRepeat(context, bgLayer, bgRepeat);
		listener.onBgAttachment(context, bgLayer, bgAttachment);
		listener.onBgOrigin(context, bgLayer, bgOrigin);
		listener.onBgClip(context, bgLayer, bgClip);
		listener.onBgColor(context, bgColor);
	}

	private void skipFunctionParamsAfterParenthesis() throws IOException, ParserException {
		
		boolean done = false;

		do {
			
			// must handle nested function calls as well, so must search for '('
			// Find the FIRST that matches instead of the longest
			CSSToken token = lexer.lex(LexerMatch.FIRST_MATCH,
					CSSToken.INCLUDING_PARENTHESIS_START,
					CSSToken.INCLUDING_COMMA,
					CSSToken.INCLUDING_PARENTHESIS_END,
					CSSToken.COMMENT,
					CSSToken.WS);
			
			switch (token) {
			case COMMENT:
			case WS:
				// skip to next iteration
				break;
				
			case INCLUDING_PARENTHESIS_START:
				// Parenthesis start before comma, this is a nested function, call recursively
				skipFunctionParamsAfterParenthesis();
				break;
			
			case INCLUDING_COMMA:
				// non-functioncall parameter, just loop
				break;
				
			case INCLUDING_PARENTHESIS_END:
				// End of current function call, at som recursion level, exit
				done = true;
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
			
			/*
			CSSToken token = lexSkipWSAndComment(CSSToken.INCLUDING_PARENTHESIS_START);
			if (token == CSSToken.INCLUDING_PARENTHESIS_START) {
				// Nested function, recursively skip
				skipFunctionParamsAfterParenthesis();
			}
			else {
				// We can check for comma or parenthesis end
				token = lexSkipWSAndComment(CSSToken.INCLUDING_COMMA, CSSToken.INCLUDING_PARENTHESIS_END);
				
				// done when found end parenthesis at this nesting level
				if (token == CSSToken.INCLUDING_PARENTHESIS_END) {
					done = true;
				}
				else if (token == CSSToken.NONE) {
					throw lexer.unexpectedToken();
				}
			}
			*/
		} while (!done);
	}


	private boolean readComma() throws IOException, ParserException {
		boolean commaRead = false;
		
		switch(lexSkipWSAndComment(CSSToken.COMMA)) {
		case COMMA: commaRead = true; break; // continue next iteration
		case NONE: break;
		default: throw lexer.unexpectedToken();
		}

		return commaRead;
	}
	
	private static CSSBackgroundPosition tokenToPosition(CSSToken token) {
		final CSSBackgroundPosition ret;
		
		switch (token) {
		case INITIAL:
			ret = CSSBackgroundPosition.INITIAL;
			break;
			
		case INHERIT:
			ret = CSSBackgroundPosition.INHERIT;
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected token " + token);
		}
		
		return ret;
	}
	
	private void notifyPosition(LISTENER_CONTEXT context, int bgLayer, CSSPositionComponent pos1) throws ParserException {
		notifyPosition(context, bgLayer, pos1, CSSPositionComponent.CENTER);
	}
	
	private void notifyPosition(LISTENER_CONTEXT context, int bgLayer, CSSPositionComponent pos1, CSSPositionComponent pos2) throws ParserException {
		
		CSSBackgroundPosition found = CSSBackgroundPosition.fromPositionComponents(pos1, pos2);
		
		if (found == null) {
			throw new ParserException("Not a valid position combination of " + pos1 + "/" + pos2);
		}
		
		listener.onBgPosition(context, bgLayer, found);
	}

	private void assureToken(CSSToken expected) throws IOException, ParserException {
		CSSToken token = lexer.lex(expected);
		
		if (token != expected) {
			throw lexer.unexpectedToken();
		}
	}
	
	private void assureTokenSkipWSAndComment(CSSToken expected) throws IOException, ParserException {
		CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, expected);
	}
	
	private static final CSSToken [] FONTSIZE_TOKENS = copyTokens(token -> token.getFontSize() != null, CSSToken.INTEGER);
	
	private void parseFontSize(LISTENER_CONTEXT context) throws NumberFormatException, IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(FONTSIZE_TOKENS);
		
		switch (token) {
		case INTEGER:
			// parse as size
			CSSParserHelperSizeToSemicolon.parseSizeValueAfterInt(lexer, null, Integer.parseInt(lexer.get()), (value, unit) -> listener.onFontSize(context, value, unit, null));
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			listener.onFontSize(context, 0, null, token.getFontSize());
			break;
		}
	}

	private static final CSSToken [] FONTWEIGHT_TOKENS = copyTokens(token -> token.getFontWeight() != null, CSSToken.INTEGER);
	
	private void parseFontWeight(LISTENER_CONTEXT context) throws NumberFormatException, IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(FONTWEIGHT_TOKENS);
		
		switch (token) {
		case INTEGER:
			// parse as size
			final int value = Integer.parseInt(lexer.get());
			listener.onFontWeight(context, value, null);
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			listener.onFontWeight(context, 0, token.getFontWeight());
			break;
		}
	}

	private static final CSSToken [] POSITION_TOKENS = copyTokens(token -> token.getPosition() != null);

	private void parsePosition(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseEnum(POSITION_TOKENS, token -> listener.onPosition(context, token.getPosition()));
	}

	private static final CSSToken [] FLOAT_TOKENS = copyTokens(token -> token.getFloat() != null);

	private void parseFloat(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseEnum(FLOAT_TOKENS, token -> listener.onFloat(context, token.getFloat()));
	}

	private static final CSSToken [] CLEAR_TOKENS = copyTokens(token -> token.getClear() != null);

	private void parseClear(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseEnum(CLEAR_TOKENS, token -> listener.onClear(context, token.getClear()));
	}

	private static final CSSToken [] TEXT_ALIGN_TOKENS = copyTokens(token -> token.getTextAlign() != null);

	private void parseTextAlign(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseEnum(TEXT_ALIGN_TOKENS, token -> listener.onTextAlign(context, token.getTextAlign()));
	}

	private static final CSSToken [] TEXT_DECORATION_TOKENS = copyTokens(token -> token.getTextDecoration() != null);

	private void parseTextDecoration(LISTENER_CONTEXT context) throws IOException, ParserException {
		parseEnum(TEXT_DECORATION_TOKENS, token -> listener.onTextDecoration(context, token.getTextDecoration()));
	}

	private static final CSSToken [] FILTER_TOKENS = copyTokens(token -> token.getFilter() != null,
			CSSToken.FUNCTION_BLUR, CSSToken.FUNCTION_BRIGHTNESS, CSSToken.FUNCTION_CONTRAST, CSSToken.FUNCTION_DROP_SHADOW,
			CSSToken.FUNCTION_GRAYSCALE, CSSToken.FUNCTION_HUE_ROTATE, CSSToken.FUNCTION_INVERT, CSSToken.FUNCTION_OPACITY,
			CSSToken.FUNCTION_SATURATE, CSSToken.FUNCTION_SEPIA, CSSToken.FUNCTION_URL,
			
			CSSToken.MS_PROGID_FUNCTION); // MS specific

	private void parseFilter(LISTENER_CONTEXT context) throws IOException, ParserException {
		CSSToken token;
		
		boolean done = false;
		
		do {
			boolean gotFilterEnum = false;
			
			token = lexSkipWSAndComment(FILTER_TOKENS);
			
			switch (token) {
			
				case MS_PROGID_FUNCTION:
					// Skip MS specific function
					skipFunctionParamsAfterParenthesis();
					done = true;
					break;
			
				case FUNCTION_BLUR:
					listener.onBlur(context, parsePxFunction());
					break;
					
				case FUNCTION_BRIGHTNESS:
					listener.onBrightness(context, parsePctFunction());
					break;
				
				case FUNCTION_CONTRAST:
					listener.onContrast(context, parsePctFunction());
					break;

				case FUNCTION_DROP_SHADOW:
					parseDropShadow(context);
					break;

				case FUNCTION_GRAYSCALE:
					listener.onGrayscale(context, parsePctFunction());
					break;

				case FUNCTION_HUE_ROTATE:
					listener.onHueRotate(context, parseDegFunction());
					break;

				case FUNCTION_INVERT:
					listener.onInvert(context, parsePctFunction());
					break;

				case FUNCTION_OPACITY:
					listener.onOpacity(context, parsePctFunction());
					break;
				
				case FUNCTION_SATURATE:
					listener.onSaturate(context, parsePctFunction());
					break;

				case FUNCTION_SEPIA:
					listener.onSepia(context, parsePctFunction());
					break;

				case FUNCTION_URL:
					listener.onUrl(context, parseURLFunction());
					break;

				case NONE:
					throw lexer.unexpectedToken();
					
				default:
					if (token.getFilter() == null) {
						throw new IllegalStateException("Expected filter token: " + token);
					}
					listener.onFilter(context, token.getFilter());
					gotFilterEnum = false;
					break;
			}

			if (!done) {
				token = lexSkipWSAndComment(CSSToken.COMMA);
				
				switch (token) {
				case COMMA:
					if (gotFilterEnum) {
						throw new ParserException("filter enum followed by comma");
					}
					break;
				
				case NONE:
					// end of list
					done = true;
					break;
					
				default:
					throw lexer.unexpectedToken();
				}
			}
		}
		while (!done);
	}
	
	private int parsePxFunction() throws IOException, ParserException {
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_START);

		assureTokenSkipWSAndComment(CSSToken.INTEGER);
		
		final int ret = Integer.parseInt(lexer.get());
		
		assureToken(CSSToken.UNIT_PX);
		
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_END);
		
		return ret;
	}

	private int parsePctFunction() throws IOException, ParserException {
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_START);

		assureTokenSkipWSAndComment(CSSToken.INTEGER);
		
		final int ret =  DecimalSize.encodeAsInt(Integer.parseInt(lexer.get()));
		
		assureToken(CSSToken.UNIT_PCT);
		
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_END);

		return ret;
	}

	private int parseDegFunction() throws IOException, ParserException {
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_START);

		assureTokenSkipWSAndComment(CSSToken.INTEGER);
		
		final int ret = DecimalSize.encodeAsInt(Integer.parseInt(lexer.get()));
		
		assureToken(CSSToken.RADIX_DEG);
		
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_END);

		return ret;
	}

	private String parseURLFunction() throws IOException, ParserException {
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_START);
		
		final String ret = parseQuotedString();

		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_END);

		return ret;
	}

	private static final CSSToken [] COLOR_OR_SIZE_TOKENS = TokenMergeHelper.merge(COLOR_TOKENS, CSSToken.INTEGER);

	private static final CSSToken [] COLOR_OR_PARENTHESIS_TOKENS = TokenMergeHelper.merge(COLOR_TOKENS, CSSToken.PARENTHESIS_END);

	private static final CSSToken [] COLOR_OR_SIZE_OR_PARENTHESIS_TOKENS = TokenMergeHelper.merge(COLOR_OR_SIZE_TOKENS, CSSToken.PARENTHESIS_END);

	
	private void parseDropShadow(LISTENER_CONTEXT context) throws IOException, ParserException {
		// First two mandatory pixel values, then two optional ones and optional color value
		boolean parenthesisRead = false;
		
		final CSSUnit defaultUnit = CSSUnit.PX;
		
		final CachedSize cachedSize1 = new CachedSize();
		final CachedSize cachedSize2 = new CachedSize();
		final CachedSize cachedSize3 = new CachedSize();
		final CachedSize cachedSize4 = new CachedSize();
		
		assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_START);
		
		CSSParserHelperSize.parseDecimalSizeValue(lexer, defaultUnit, (value, unit) -> cachedSize1.init(value, defaultUnit));
		CSSParserHelperSize.parseDecimalSizeValue(lexer, defaultUnit, (value, unit) -> cachedSize2.init(value, defaultUnit));
		
		// Now to the optional values, here we might have size or color which makes this a bit more difficult
		// color may start with '#' or a function or a name
		
		int dropShadowBlur = -1;
		int dropShadowSpread = -1;
		
		final ParseColorOrSizeStatus colorOrSizeStatus = new ParseColorOrSizeStatus();
		
		final CachedRGBA cachedRGBA = new CachedRGBA();
		
		final Value<CSSColor> cachedColor = new Value<>();
		
		parenthesisRead = parseColorOrSizeOrOtherToParenthesis(
				defaultUnit, COLOR_OR_SIZE_OR_PARENTHESIS_TOKENS, colorOrSizeStatus,
				(r, g, b, a) -> cachedRGBA.init(r, g, b, a),
				cssColor -> cachedColor.set(cssColor),
				(value, unit) -> cachedSize3.init(value, defaultUnit));
		
		if (colorOrSizeStatus.sizeFound) {
			dropShadowBlur = cachedSize3.getValue();
			
			if (cachedSize3.getUnit() != CSSUnit.PX) {
				throw new ParserException("Expected blur to be in pixels");
			}
			
			if (!parenthesisRead) {
				//not at end of line, look for spread or color
				parenthesisRead = parseColorOrSizeOrOtherToParenthesis(
						defaultUnit, COLOR_OR_SIZE_OR_PARENTHESIS_TOKENS, colorOrSizeStatus,
						(r, g, b, a) -> cachedRGBA.init(r, g, b, a),
						cssColor -> cachedColor.set(cssColor),
						(value, unit) -> cachedSize4.init(value, defaultUnit));
				
				if (colorOrSizeStatus.sizeFound) {
					// found spread
					dropShadowSpread = cachedSize4.getValue();
					
					if (cachedSize4.getUnit() != CSSUnit.PX) {
						throw new ParserException("Expected spread to be in pixels");
					}
					
					if (!parenthesisRead) {
						// might still have color
						CSSToken colorToken = CSSParserHelperColor.parseColor(
								lexer,
								(r, g, b, a) -> cachedRGBA.init(r, g, b, a),
								cssColor -> cachedColor.set(cssColor),
								COLOR_OR_PARENTHESIS_TOKENS);
						
						parenthesisRead = colorToken != null && colorToken == CSSToken.PARENTHESIS_END;
					}
				}
				else {
					// color is the last part so nothing more to do
				}
			}
		}
		else if (colorOrSizeStatus.colorFound) {
			// color is the last part so nothing more to do
		}

		if (cachedRGBA.isInitialized()) {
			// call RGBA callback
			listener.onDropShadow(context,
					cachedSize1.getValue(), cachedSize1.getUnit(),
					cachedSize2.getValue(), cachedSize2.getUnit(), dropShadowBlur, dropShadowSpread,
					cachedRGBA.getR(), cachedRGBA.getG(), cachedRGBA.getB(), cachedRGBA.getA());
		}
		else {
			// Use CSS color variant event if color is null
			listener.onDropShadow(context,
					cachedSize1.getValue(), cachedSize1.getUnit(),
					cachedSize2.getValue(), cachedSize2.getUnit(), dropShadowBlur, dropShadowSpread, cachedColor.get());
		}
		
		if (!parenthesisRead) {
			assureTokenSkipWSAndComment(CSSToken.PARENTHESIS_END);
		}
	}
	
	private static class ParseColorOrSizeStatus {
		private boolean sizeFound;
		private boolean colorFound;
		
		void clear() {
			this.sizeFound = false;
			this.colorFound = false;
		}
	}
	
	boolean parseColorOrSizeOrOtherToParenthesis(CSSUnit defaultUnit, CSSToken [] tokens, ParseColorOrSizeStatus status, IColorRGBFunction rgbFunction, ICSSColorFunction cssColor, BiConsumer<Integer, CSSUnit> sizeFunction) throws IOException, ParserException {
		
		boolean parenthesisRead = false;
		
		if (!TokenMergeHelper.has(tokens, CSSToken.PARENTHESIS_END)) {
			throw new IllegalArgumentException("input tokens must contain ')'");
		}
		
		CSSToken token = lexSkipWSAndComment(tokens);
		
		status.clear();
		
		switch (token) {
		
		case INTEGER:
			// size, delegate
			parenthesisRead = CSSParserHelperSizeAsOnlyFunctionParam.parseSizeValueAfterInt(
					lexer,
					defaultUnit,
					Integer.parseInt(lexer.get()),
					(value, unit) -> {
						// should never be a decimal number and always px
						sizeFunction.accept(DecimalSize.decodeToInt(value), unit);
					}
			);
			status.sizeFound = true;
			break;
		
		case FUNCTION_RGB:
			CSSParserHelperColor.parseRGBFunction(lexer, rgbFunction);
			status.colorFound = true;
			break;
			
		case FUNCTION_RGBA:
			CSSParserHelperColor.parseRGBAFunction(lexer, rgbFunction);
			status.colorFound = true;
			break;
			
		case COLOR_MARKER:
			CSSParserHelperColor.parseHexColor(lexer, rgbFunction);
			break;
			
		case PARENTHESIS_END:
			parenthesisRead = true;
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			if (token.getColor() != null) {
				cssColor.onColor(token.getColor());
				status.colorFound = true;
			}
			else {
				throw lexer.unexpectedToken();
			}
			break;
		}

		return parenthesisRead;
	}
	
	private void parseEnum(CSSToken [] tokens, Consumer<CSSToken> onToken) throws IOException, ParserException {
		
		CSSToken token = lexSkipWSAndComment(tokens);
		
		if (token == CSSToken.NONE) {
			throw lexer.unexpectedToken();
		}
		
		onToken.accept(token);
	}
}
