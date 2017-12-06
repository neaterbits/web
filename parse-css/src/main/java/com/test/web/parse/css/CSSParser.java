package com.test.web.parse.css;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.test.web.css.common.enums.CSSBackgroundAttachment;
import com.test.web.css.common.enums.CSSBackgroundColor;
import com.test.web.css.common.enums.CSSBackgroundImage;
import com.test.web.css.common.enums.CSSBackgroundOrigin;
import com.test.web.css.common.enums.CSSBackgroundPosition;
import com.test.web.css.common.enums.CSSBackgroundRepeat;
import com.test.web.css.common.enums.CSSBackgroundSize;
import com.test.web.css.common.enums.CSSForeground;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSPositionComponent;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.BaseParser;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.LexerMatch;
import com.test.web.parse.common.ParserException;
import com.test.web.types.DecimalSize;
import com.test.web.types.Value;

/**
 * For parsing CSS from file or from a style attribute
 * @author nhl
 *
 */
public class CSSParser<TOKENIZER extends Tokenizer, LISTENER_CONTEXT> extends BaseParser<CSSToken, CharInput> {
	
	private final Lexer<CSSToken, CharInput> lexer; 
	private final CSSParserListener<TOKENIZER, LISTENER_CONTEXT> listener;

	public CSSParser(CharInput input, CSSParserListener<TOKENIZER, LISTENER_CONTEXT> listener) {
		super(new Lexer<CSSToken, CharInput>(input, CSSToken.class, CSSToken.NONE, CSSToken.EOF), CSSToken.WS);
		
		this.lexer = getLexer();
		this.listener = listener;
	}
	
	private static final CSSToken [] wsAndComment = new CSSToken [] { CSSToken.WS, CSSToken.COMMENT };
	
	@SafeVarargs
	private final CSSToken lexSkipWSAndComment(CSSToken ... tokens) throws IOException {

		// TODO avoid allocation
		CSSToken token;
		
		final CSSToken [] mergedTokens = merge(wsAndComment, tokens);
		
		for (;;) {
			token = lexer.lex(mergedTokens);

			if (token != CSSToken.WS && token != CSSToken.COMMENT) {
				break;
			}
		}
		
		return token;
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
			CSSToken token = lexer.lex(CSSToken.WS, CSSToken.ID_MARKER, CSSToken.CLASS_MARKER, CSSToken.TAG, CSSToken.COMMENT, CSSToken.BRACKET_START);
			
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
	
	private static final CSSToken [] STYLE_TOKENS = copyTokens(token -> token.getElement() != null);
	
	private static final CSSToken [] BLOCK_TOKENS = copyTokens(
			token -> token.getElement() != null, 
			CSSToken.WS,
			CSSToken.COMMENT,
			CSSToken.BRACKET_END);

	private static final CSSToken [] UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS = copyTokens(
			token -> token.getUnit() != null, 
			CSSToken.DOT,
			CSSToken.SEMICOLON,
			CSSToken.WS,
			CSSToken.COMMENT);

	private static final CSSToken [] UNIT_OR_SEMICOLON_TOKENS = copyTokens(
			token -> token.getUnit() != null, 
			CSSToken.SEMICOLON);

	private static CSSToken [] copyTokens(Predicate<CSSToken> test, CSSToken ... extra) {
		
		final CSSToken [] copy;
		
		int numTokens = 0;
		
		for (CSSToken token : CSSToken.values()) {
			
			if (test.test(token)) {
				++ numTokens;
			}
		}
		
		numTokens += extra.length;
		
		copy = new CSSToken[numTokens];
		
		int idx = 0;
		for (CSSToken token : CSSToken.values()) {
			if (test.test(token)) {
				copy[idx ++] = token;
			}
		}

		// May be WS or bracket end as well
		for (int i = 0; i < extra.length; ++ i) {
			copy[idx ++] = extra[i];
		}
		
		return copy;
	}
	
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
	

	private void skipAnyWS() throws IOException {
		lexer.lex(CSSToken.WS);
	}

	public boolean parseElement(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		final CSSToken token = lexSkipWS(STYLE_TOKENS);
		
		if (token == CSSToken.NONE) {
			throw new ParserException("No CSS style token found");
		}
		
		return parseElementWithoutCheckingForSemiColon(context, token.getElement());
	}
	
	private boolean parseElementWithoutCheckingForSemiColon(LISTENER_CONTEXT context, CSStyle element) throws IOException, ParserException {
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
		
		skipAnyWS();

		// Now read value, this depends on input style
		final boolean semiColonRead;
		
		switch (element) {
		case WIDTH:
			semiColonRead = parseSizeValue((size, unit) -> listener.onWidth(context, size, unit));
			break;
			
		case HEIGHT:
			semiColonRead = parseSizeValue((size, unit) -> listener.onHeight(context, size, unit));
			break;

		case COLOR:
			semiColonRead = parseFgColor(
					(r, g, b, a) -> listener.onColor(context, r, g, b, a),
					cssColor -> listener.onColor(context, cssColor),
					type -> listener.onColor(context, type));
			break;
			
		case BACKGROUND_COLOR:
			semiColonRead = parseBgColor(
					(r, g, b, a) -> listener.onBgColor(context, r, g, b, a),
					cssColor -> listener.onBgColor(context, cssColor),
					type -> listener.onBgColor(context, type));
			break;
			
		case BACKGROUND_IMAGE:
			semiColonRead = parseBgImage(context);
			break;

		case BACKGROUND_POSITION:
			semiColonRead = parseBgPosition(context);
			break;
			
		case BACKGROUND_SIZE:
			semiColonRead = parseBgSize(context);
			break;

		case BACKGROUND_REPEAT:
			semiColonRead = parseBgRepeat(context);
			break;

		case BACKGROUND_ATTACHMENT:
			semiColonRead = parseBgAttachment(context);
			break;

		case BACKGROUND_ORIGIN:
			semiColonRead = parseBgOrigin(context);
			break;

		case BACKGROUND_CLIP:
			semiColonRead = parseBgClip(context);
			break;
			
		case BACKGROUND:
			semiColonRead = parseBackground(context);
			break;

		case MARGIN_LEFT:
			semiColonRead = parseSizeOrAutoOrInitialOrInherit((size, unit, justify) -> listener.onMarginLeft(context, size, unit, justify));
			break;
			
		case MARGIN_RIGHT:
			semiColonRead = parseSizeOrAutoOrInitialOrInherit((size, unit, justify) -> listener.onMarginRight(context, size, unit, justify));
			break;

		case MARGIN_TOP:
			semiColonRead = parseSizeOrAutoOrInitialOrInherit((size, unit, justify) -> listener.onMarginTop(context, size, unit, justify));
			break;

		case MARGIN_BOTTOM:
			semiColonRead = parseSizeOrAutoOrInitialOrInherit((size, unit, justify) -> listener.onMarginBottom(context, size, unit, justify));
			break;
			
		case MARGIN:
			semiColonRead = parseMargin(context);
			break;
			
		case PADDING_LEFT:
			semiColonRead = parseSizeOrInitialOrInherit((size, unit, justify) -> listener.onPaddingLeft(context, size, unit, justify));
			break;
			
		case PADDING_RIGHT:
			semiColonRead = parseSizeOrInitialOrInherit((size, unit, justify) -> listener.onPaddingRight(context, size, unit, justify));
			break;

		case PADDING_TOP:
			semiColonRead = parseSizeOrInitialOrInherit((size, unit, justify) -> listener.onPaddingTop(context, size, unit, justify));
			break;

		case PADDING_BOTTOM:
			semiColonRead = parseSizeOrInitialOrInherit((size, unit, justify) -> listener.onPaddingBottom(context, size, unit, justify));
			break;
			
		case PADDING:
			semiColonRead = parsePadding(context);
			break;

		case FLOAT:
			semiColonRead = parseFloat(context);
			break;
	
		case CLEAR:
			semiColonRead = parseClear(context);
			break;
			
		case POSITION:
			semiColonRead = parsePosition(context);
			break;
			
		case TEXT_ALIGN:
			semiColonRead = parseTextAlign(context);
			break;
			
		case MAX_WIDTH:
			semiColonRead = parseMax((size, unit, type) -> listener.onMaxWidth(context, size, unit, type));
			break;
			
		case MAX_HEIGHT:
			semiColonRead = parseMax((size, unit, type) -> listener.onMaxHeight(context, size, unit, type));
			break;
			
		case MIN_WIDTH:
			semiColonRead = parseMin((size, unit, type) -> listener.onMinWidth(context, size, unit, type));
			break;
			
		case MIN_HEIGHT:
			semiColonRead = parseMin((size, unit, type) -> listener.onMinHeight(context, size, unit, type));
			break;
			
		case FONT_SIZE:
			semiColonRead = parseFontSize(context);
			break;
			
		case FONT_WEIGHT:
			semiColonRead = parseFontWeight(context);
			break;
			
		case TEXT_DECORATION:
			semiColonRead = parseTextDecoration(context);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown element " + element);
		}
		
		return semiColonRead;
	}

	private void parseElement(LISTENER_CONTEXT context, CSStyle element) throws IOException, ParserException {

		final boolean semiColonRead = parseElementWithoutCheckingForSemiColon(context, element);

		if (!semiColonRead) {
			final CSSToken token = lexer.lex(CSSToken.SEMICOLON);

			if (token != CSSToken.SEMICOLON) {
				throw lexer.unexpectedToken();
			}
		}
	}
	
	private static class MarginPart {
		private int size;
		private CSSUnit unit;
		private CSSJustify justify;
		private boolean initialized;
		
		void init(int size, CSSUnit unit, CSSJustify justify) {
			this.size = size;
			this.unit = unit;
			this.justify = justify;
			this.initialized = true;
		}
	}
	
	@FunctionalInterface
	interface InitialMarginOrPaddingParser {
		boolean parse(IJustifyFunction justifyFunction) throws IOException, ParserException;
	}
	
	private boolean parseMargin(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseMarginOrPadding(context, listener::onMargin, this::parseSizeOrAutoOrInitialOrInherit);
	}

	private boolean parsePadding(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseMarginOrPadding(context, listener::onPadding, this::parseSizeOrInitialOrInherit);
	}

	private boolean parseMarginOrPadding(LISTENER_CONTEXT context, IWrapping<LISTENER_CONTEXT> callback, InitialMarginOrPaddingParser parseInitial) throws IOException, ParserException {
		
		// TODO perhaps cache if parser is singlethreaded
		final MarginPart part1 = new MarginPart();
		
		// first parse one size or auto
		//boolean semiColonRead = parseSizeOrAuto((size, unit, justify) -> part1.init(size, unit, justify));
		boolean semiColonRead = parseInitial.parse((size, unit, justify) -> part1.init(size, unit, justify));
		
		if (part1.justify != CSSJustify.NONE && part1.justify != CSSJustify.SIZE) {
			// margin: auto which is special-case
			callback.onWrapping(context,
					0, null, part1.justify,
					0, null, part1.justify,
					0, null, part1.justify,
					0, null, part1.justify);
		}
		else {
			
			// We have more than one part that we have to read, which is which depends on the number of sizes found
			// there ought to be max 4
			// TODO perhaps cache if parser is singlethreaded
			final MarginPart part2 = new MarginPart();
			final MarginPart part3 = new MarginPart();
			final MarginPart part4 = new MarginPart();
			
			if (!semiColonRead) {
				semiColonRead = parseSizeValueOrSemicolon((size, unit) -> part2.init(size, unit, CSSJustify.SIZE));
				if (part2.initialized && !semiColonRead) {
					// read a value, try part3
					semiColonRead = parseSizeValueOrSemicolon((size, unit) -> part3.init(size, unit, CSSJustify.SIZE));
					
					if (part3.initialized && !semiColonRead) {
						semiColonRead = parseSizeValueOrSemicolon((size, unit) -> part4.init(size, unit, CSSJustify.SIZE));
					}
				}
			}
			
			if (part4.initialized) {
				// got 4 parts, pass them all
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part3.size, part3.unit, part3.justify,
						part4.size, part4.unit, part4.justify);
			}
			else if (part3.initialized) {
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part3.size, part3.unit, part3.justify,
						part2.size, part2.unit, part2.justify);
			}
			else if (part2.initialized) {
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify);
			}
			else if (part1.initialized) {
				callback.onWrapping(context,
						part1.size, part1.unit, part1.justify,
						part1.size, part1.unit, part1.justify,
						part1.size, part1.unit, part1.justify,
						part1.size, part1.unit, part1.justify);
			}
			else {
				throw new IllegalStateException("Should have at least one margin part");
			}
		}
			
		return semiColonRead;
	}
	
	private static final CSSToken [] autoOrInitialOrInheritTokens = new CSSToken[] { CSSToken.INTEGER, CSSToken.AUTO, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT };
	private static final CSSToken [] initialOrInheritTokens 			   = new CSSToken[] { CSSToken.INTEGER, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT };

	private boolean parseSizeOrAutoOrInitialOrInherit(IJustifyFunction toCall) throws IOException, ParserException {
		return parseSizeOrAutoOrInitialOrInherit(toCall, autoOrInitialOrInheritTokens);
	}

	private boolean parseSizeOrInitialOrInherit(IJustifyFunction toCall) throws IOException, ParserException {
		return parseSizeOrAutoOrInitialOrInherit(toCall, initialOrInheritTokens);
	}
	
	private boolean parseSizeOrAutoOrInitialOrInherit(IJustifyFunction toCall, CSSToken [] tokens) throws IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(tokens);
		
		final boolean semiColonRead;
		
		final BiConsumer<Integer, CSSUnit> sizeCallback = (size, unit) -> toCall.onJustify(size, unit, CSSJustify.SIZE);
		
		switch (token) {
		case INTEGER:
			final int intValue = Integer.parseInt(lexer.get());
			semiColonRead = parseSizeValueAfterInt(intValue, sizeCallback);
			break;
			
		case AUTO:
			toCall.onJustify(DecimalSize.encodeAsInt(0), null, CSSJustify.AUTO);
			semiColonRead = false;
			break;

		case INITIAL:
			toCall.onJustify(DecimalSize.encodeAsInt(0), null, CSSJustify.INITIAL);
			semiColonRead = false;
			break;
			
		case INHERIT:
			toCall.onJustify(DecimalSize.encodeAsInt(0), null, CSSJustify.INHERIT);
			semiColonRead = false;
			break;

		case DOT:
			semiColonRead = parseDecimalAfterDot(0, defaultUnit, sizeCallback);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}

		return semiColonRead;
	}
	
	private boolean parseMax(IMaxFunction toCall) throws IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER, CSSToken.CSS_NONE, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT);
		
		final boolean semiColonRead;
		
		final BiConsumer<Integer, CSSUnit> sizeCallback = (size, unit) -> toCall.onMax(size, unit, CSSMax.SIZE);
		
		switch (token) {
		case INTEGER:
			final int intValue = Integer.parseInt(lexer.get());
			semiColonRead = parseSizeValueAfterInt(intValue, sizeCallback);
			break;
			
		case NONE:
			toCall.onMax(0, null, CSSMax.NONE);
			semiColonRead = false;
			break;
	
		case INITIAL:
			toCall.onMax(0, null, CSSMax.INITIAL);
			semiColonRead = false;
			break;
			
		case INHERIT:
			toCall.onMax(0, null, CSSMax.INHERIT);
			semiColonRead = false;
			break;
			
		case DOT:
			semiColonRead = parseDecimalAfterDot(0, defaultUnit, sizeCallback);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}

		return semiColonRead;
	}

	private boolean parseMin(IMinFunction toCall) throws IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER, CSSToken.INITIAL, CSSToken.INHERIT, CSSToken.DOT);
		
		final boolean semiColonRead;
		
		final BiConsumer<Integer, CSSUnit> sizeCallback = (size, unit) -> toCall.onMin(size, unit, CSSMin.SIZE);
		
		switch (token) {
		case INTEGER:
			final int intValue = Integer.parseInt(lexer.get());
			semiColonRead = parseSizeValueAfterInt(intValue, sizeCallback);
			break;
			
		case INITIAL:
			toCall.onMin(0, null, CSSMin.INITIAL);
			semiColonRead = false;
			break;
			
		case INHERIT:
			toCall.onMin(0, null, CSSMin.INHERIT);
			semiColonRead = false;
			break;
			
		case DOT:
			semiColonRead = parseDecimalAfterDot(0, defaultUnit, sizeCallback);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}

		return semiColonRead;
	}

	private boolean parseSizeValueOrSemicolon(BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		// Number followed by possibly units
		CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER, CSSToken.SEMICOLON);
		
		final boolean semiColonRead;
		
		switch (token) {
		case INTEGER:
			int value = Integer.parseInt(lexer.get());
			
			semiColonRead = parseSizeValueAfterInt(value, toCall);
			break;
			
		case SEMICOLON:
			semiColonRead = true;
			break;
			
		default:
			throw lexer.unexpectedToken();
		}

		return semiColonRead;
	}
	
	private boolean parseSizeValue(BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		// Number followed by possibly units
		CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER);
		
		if (token != CSSToken.INTEGER) {
			throw lexer.unexpectedToken();
		}
		
		int value = Integer.parseInt(lexer.get());
		
		return parseSizeValueAfterInt(value, toCall);
	}
	
	private static final CSSUnit defaultUnit = CSSUnit.PX;
	
	private boolean parseSizeValueAfterInt(int value, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
			
		// Now may be decimal-point or unit token or semicolon
		
		boolean gotDot = false;
		
		boolean semiColonRead = false;
		CSSUnit unit = defaultUnit; // default to pixels
		
		CSSToken token = lexer.lex(UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS);
		
		switch (token) {
		case SEMICOLON:
			semiColonRead = true;
			break;
			
		case DOT:
			semiColonRead = parseDecimalAfterDot(value, unit, toCall);
			gotDot = true;
			break;
			
		case WS:
		case COMMENT:
			// no '.' so not a comma-number
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			// Unit token
			unit = token.getUnit();
			break;
		}
		
		if (!gotDot) {
			toCall.accept(DecimalSize.encodeAsInt(value), unit);
		}
		
		return semiColonRead;
	}
	
	private boolean parseDecimalAfterDot(int beforeDecimal, CSSUnit unit, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		CSSToken token;
		boolean semiColonRead = false;
		
		// Decimal number, should be integer
		token = lexer.lex(CSSToken.INTEGER);
		if (token != CSSToken.INTEGER) {
			throw lexer.unexpectedToken();
		}
		
		final DecimalSize value = new DecimalSize(beforeDecimal, lexer.get());
		
		// Parse unit
		token = lexer.lex(UNIT_OR_SEMICOLON_TOKENS);
		
		switch (token) {
		case SEMICOLON:
			semiColonRead = true;
			break;

		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			// Unit token
			unit = token.getUnit();
			break;
		}
		
		toCall.accept(value.encodeAsInt(), unit);
		
		return semiColonRead;
	}
	
	private static final CSSToken [] COLOR_TOKENS = copyTokens(token -> token.getColor() != null, CSSToken.COLOR_MARKER, CSSToken.FUNCTION_RGB, CSSToken.FUNCTION_RGBA);

	private static final CSSToken [] FG_COLOR_TOKENS = copyTokens(token -> token.getForeground() != null, COLOR_TOKENS);

	private static final CSSToken [] BG_COLOR_TOKENS = copyTokens(token -> token.getBackground() != null, COLOR_TOKENS);

	private boolean parseFgColor(IColorRGBFunction rgbColor, ICSSColorFunction cssColor, Consumer<CSSForeground> colorType) throws IOException, ParserException {
		final CSSToken token = parseColor(rgbColor, cssColor, FG_COLOR_TOKENS);
		
		if (token.getForeground() != null) {
			colorType.accept(token.getForeground());
		}
		
		return false;
	}

	private boolean parseBgColor(IColorRGBFunction rgbColor, ICSSColorFunction cssColor, Consumer<CSSBackgroundColor> colorType) throws IOException, ParserException {
		final CSSToken token = parseColor(rgbColor, cssColor, BG_COLOR_TOKENS);
		
		if (token.getBackground() != null) {
			// special type, call callback
			colorType.accept(token.getBackground());
		}
		
		return false;
	}

	private CSSToken parseColor(IColorRGBFunction rgbColor, ICSSColorFunction cssColor, CSSToken [] tokens) throws IOException, ParserException {

		CSSToken token = lexSkipWSAndComment(tokens);
		
		switch (token) {
		case COLOR_MARKER:
			// Read color string
			token = parseHexColor(rgbColor);
			break;

		case FUNCTION_RGB:
			parseRGBFunction(rgbColor);
			break;
			
		case FUNCTION_RGBA:
			parseRGBAFunction(rgbColor);
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			// a CSS standard color
			if (token.getColor() != null) {
				cssColor.onColor(token.getColor());
			}
			else {
				// One of the special enum values, like initial or inherit
				// handle this in the calling function
			}
			break;
		}
		
		return token;
	}
	
	private CSSToken parseHexColor(IColorRGBFunction rgbColor) throws IOException, ParserException {
		CSSToken token = lexer.lex(CSSToken.HEXDIGITS);
		if (token != CSSToken.HEXDIGITS) {
			throw lexer.unexpectedToken();
		}
		
		final String hexString = lexer.get();
		
		if (hexString.length() != 6) {
			throw new ParserException("Unexpected length: " + hexString.length());
		}
		
		// Parse into hex values
		rgbColor.onColor(
				hexValue(hexString, 0, 2),
				hexValue(hexString, 2, 2),
				hexValue(hexString, 4, 2),
				DecimalSize.NONE
		);
		
		return token;
	}

	private static final CSSToken [] BG_IMAGE_TOKENS = copyTokens(token -> token.getBgImage() != null,
				CSSToken.FUNCTION_URL,
				CSSToken.BROWSER_SPECIFIC_FUNCTION); // eg -moz-linear-gradient
	
	private boolean parseBgImage(LISTENER_CONTEXT context) throws IOException, ParserException {
	
		int bgLayer = 0;
		boolean semiColonRead = false;
		
		do {
			CSSToken token = lexSkipWSAndComment(BG_IMAGE_TOKENS);
			
			switch (token) {
			case FUNCTION_URL:
				// parse function params
				final String url = parseImageURL();;
				
				listener.onBgImageURL(context, bgLayer, url);
				
				semiColonRead = readCommaOrSemiColon();
				break;
				
			case NONE:
				throw lexer.unexpectedToken();
				
			default:
				if (token.getBgImage() == null) {
					throw new IllegalStateException("Expected bg image");
				}
				listener.onBgImage(context, bgLayer,  token.getBgImage());

				semiColonRead = readCommaOrSemiColon();
				break;
			}
			
			++ bgLayer;
			
		} while (!semiColonRead);
	
		return semiColonRead;
	}
	
	private String parseImageURL() throws IOException, ParserException {
		
		final Object [] values = parseFunctionParams(1, paramIdx -> parseQuotedString());
		
		final String url = (String)values[0];

		return url;
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
					CSSToken.COMMA,
					CSSToken.SEMICOLON);

	private static class CachedSize {
		private int value;
		private CSSUnit unit;
		
		void init(int value, CSSUnit unit) {
			this.value = value;
			this.unit = unit;
		}
	}
	
	private void parseBgPositionAfterInt(LISTENER_CONTEXT context, int bgLayer, CachedSize cachedSize) throws IOException, ParserException {
		// we got integer so a regular size specification
		parseSizeValueAfterInt(Integer.parseInt(lexer.get()), (value, unit) -> cachedSize.init(value, unit));
		
		final int bgl = bgLayer;
		// Now should be another size value
		parseSizeValue((value, unit) -> listener.onBgPosition(context, bgl, cachedSize.value, cachedSize.unit, value, unit));
	}
	
	private boolean parseBgPosition(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		int bgLayer = 0;
		boolean semiColonRead = false;
		
		final CachedSize cachedSize = new CachedSize();
		
		do {
			CSSToken token = lexSkipWSAndComment(BG_POSITION_TOKENS);
			
			switch (token) {
			case INTEGER:
				parseBgPositionAfterInt(context, bgLayer, cachedSize);
				
				// next should be comma for new layer or a semicolon
				semiColonRead = readCommaOrSemiColon();
				break;
				
			case INITIAL:
			case INHERIT:
				listener.onBgPosition(context, bgLayer, tokenToPosition(token));
				
				// next should be comma for new layer or a semicolon
				semiColonRead = readCommaOrSemiColon();
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
					break;
					
				case SEMICOLON:
					notifyPosition(context, bgLayer, token.getPositionComponent());
					semiColonRead = true;
					break;
					
				case NONE:
					throw lexer.unexpectedToken();
					
				default:
					// Position component
					if (secondPosToken.getPositionComponent() == null) {
						throw new IllegalStateException("Expected position component");
					}
					notifyPosition(context, bgLayer, token.getPositionComponent(), secondPosToken.getPositionComponent());
					
					semiColonRead = readCommaOrSemiColon();
					break;
				}
				break;
			}
			
			++ bgLayer;
			
		} while (!semiColonRead);
		
		return semiColonRead;
	}

	private static final CSSToken [] BG_SIZE_TOKENS = copyTokens(token -> token.getBgSize() != null, CSSToken.INTEGER);

	private boolean parseBgSize(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		int bgLayer = 0;
		boolean semiColonRead = false;
		
		final CachedSize cachedSize = new CachedSize();
		
		do {
			
			semiColonRead = parseBgSize(context, bgLayer, cachedSize, true);
		
			++ bgLayer;
			
		} while (!semiColonRead);
	
		return semiColonRead;
	}
	
	private boolean parseBgSize(LISTENER_CONTEXT context, int bgLayer, CachedSize cachedSize, boolean readCommaOrSemiColons) throws IOException, ParserException  {
		CSSToken token = lexSkipWSAndComment(BG_SIZE_TOKENS);
		
		boolean semiColonRead = false;
		
		switch (token) {
		case INTEGER:
			// we got integer so a regular size specification
			parseSizeValueAfterInt(Integer.parseInt(lexer.get()), (value, unit) -> cachedSize.init(value, unit));
			
			final int bgl = bgLayer;
			// Now should be another size value
			parseSizeValue((value, unit) -> listener.onBgSize(context, bgl, cachedSize.value, cachedSize.unit, value, unit));
			
			if (readCommaOrSemiColons) {
				// next should be comma for new layer or a semicolon
				semiColonRead = readCommaOrSemiColon();
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
			
			if (readCommaOrSemiColons) {
				semiColonRead = readCommaOrSemiColon();
			}
			break;
		}
		
		return semiColonRead;
	}
	
	private static final CSSToken [] BG_REPEAT_TOKENS = copyTokens(token -> token.getBgRepeat() != null);

	private boolean parseBgRepeat(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseBgEnum(context, BG_REPEAT_TOKENS, CSSToken::getBgRepeat, listener::onBgRepeat);
	}

	private static final CSSToken [] BG_ATTACHMENT_TOKENS = copyTokens(token -> token.getBgAttachment() != null);

	private boolean parseBgAttachment(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseBgEnum(context, BG_ATTACHMENT_TOKENS, CSSToken::getBgAttachment, listener::onBgAttachment);
	}

	private static final CSSToken [] BG_ORIGIN_TOKENS = copyTokens(token -> token.getBgOrigin() != null);

	private boolean parseBgOrigin(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseBgEnum(context, BG_ORIGIN_TOKENS, CSSToken::getBgOrigin, listener::onBgOrigin);
	}

	private boolean parseBgClip(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseBgEnum(context, BG_ORIGIN_TOKENS, CSSToken::getBgOrigin, listener::onBgClip);
	}

	@FunctionalInterface
	interface ParseBgEnumResult<CONTEXT, E extends Enum<E>> {
		void onResult(CONTEXT context, int bgLayer, E value);
	}
	
	private <E extends Enum<E>> boolean parseBgEnum(
			LISTENER_CONTEXT context,
			CSSToken [] tokens,
			Function<CSSToken, E> getValue,
			ParseBgEnumResult<LISTENER_CONTEXT, E> processResult) throws IOException, ParserException {
	
		int bgLayer = 0;
		boolean semiColonRead = false;
		
		do {
			CSSToken token = lexSkipWSAndComment(tokens);
			
			final E value = getValue.apply(token);
			
			if (value == null) {
				throw lexer.unexpectedToken();
			}
			
			processResult.onResult(context, bgLayer, value);
			semiColonRead = readCommaOrSemiColon();
			
			++ bgLayer;
			
		} while (!semiColonRead);
	
		return semiColonRead;
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
	private boolean parseBackground(LISTENER_CONTEXT context) throws IOException, ParserException {
		
		
		int bgLayer = 0;
		boolean semiColonRead = false;
		
		do {
			semiColonRead = parseOneLayer(context, bgLayer);
		
			++ bgLayer;
		} while (!semiColonRead);
	
		return semiColonRead;

	}
	
	private boolean parseOneLayer(LISTENER_CONTEXT context, int bgLayer) throws IOException, ParserException {

		// Maintain a map of which tokens to still try
		final Map<CSStyle, CSSToken[]> tokenMap = new HashMap<>(BG_INITIAL_TOKENS);

		boolean layerDone = false;
		boolean semiColonRead = false;
		
		final Value<Integer> numOrigin = new Value<>(0);
		
		do {
			// parse for next until reaches comma or semicolon
			final CSSToken lastToken = parseOneLayerIteration(context, bgLayer, tokenMap, numOrigin);
			
			if (lastToken == CSSToken.COMMA) {
				layerDone = true;
			}
			else if (lastToken == CSSToken.SEMICOLON) {
				layerDone = true;
				semiColonRead = true;
			}
			
		} while (!layerDone);
		
		return semiColonRead;
	}
	
	private CSSToken parseOneLayerIteration(LISTENER_CONTEXT context, int bgLayer, Map<CSStyle, CSSToken[]> tokenMap, Value<Integer> numOrigin) throws IOException, ParserException {
		
		final CSSToken [] tokens = merge(tokenMap.values(), CSSToken.COMMA, CSSToken.SEMICOLON);
		
		CSSToken token = lexSkipWSAndComment(tokens);
		
		if (token == CSSToken.NONE) {
			throw lexer.unexpectedToken();
		}
		
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
		else if (token == CSSToken.COMMA || token == CSSToken.SEMICOLON) {
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

			token = parseHexColor((r, g, b, a) -> listener.onBgColor(context, r, g, b, a));
			
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
			
			parseRGBFunction((r, g, b, a) -> listener.onBgColor(context, r, g, b, a));
			
			tokenMap.remove(CSStyle.BACKGROUND_COLOR);
		}
		else if (token == CSSToken.FUNCTION_RGBA) {

			parseRGBAFunction((r, g, b, a) -> listener.onBgColor(context, r, g, b, a));

			tokenMap.remove(CSStyle.BACKGROUND_COLOR);
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


	private boolean readCommaOrSemiColon() throws IOException, ParserException {
		boolean semiColonRead = false;
		
		switch(lexSkipWSAndComment(CSSToken.COMMA, CSSToken.SEMICOLON)) {
		case COMMA: break; // continue next iteration
		case SEMICOLON: semiColonRead = true; break;
		default: throw lexer.unexpectedToken();
		}

		return semiColonRead;
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
		
		CSSBackgroundPosition found = null;
		
		for (CSSBackgroundPosition pos : CSSBackgroundPosition.values()) {
			if ( (pos.getFirst() == pos1 && pos.getSecond() == pos2) || pos.getFirst() == pos2 && pos.getSecond() == pos1) {
				found = pos;
				break;
			}
		}
		
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
		CSSToken token = lexSkipWSAndComment(expected);
		
		if (token != expected) {
			throw lexer.unexpectedToken();
		}
	}
	
	private int parseInt() throws IOException, ParserException {
		assureTokenSkipWSAndComment(CSSToken.INTEGER);
		
		return Integer.parseInt(lexer.get());
	}

	private int parseDecimal() throws IOException, ParserException {
		
		// Allow to start with '.'
		
		CSSToken token = lexer.lex(CSSToken.INTEGER, CSSToken.DOT);
		
		int beforeDot;
		String afterDot;
		
		switch (token) {
		case INTEGER:
			beforeDot = Integer.parseInt(lexer.get());
			
			// may or may not be a dot here
			token = lexSkipWSAndComment(CSSToken.DOT);
			if (token == CSSToken.DOT) {
				assureTokenSkipWSAndComment(CSSToken.INTEGER);
				afterDot = lexer.get();
			}
			else {
				afterDot = "";
			}
			break;
			
		case DOT:
			beforeDot = 0;
			assureTokenSkipWSAndComment(CSSToken.INTEGER);
			afterDot = lexer.get();
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
		
		return new DecimalSize(beforeDot, afterDot).encodeAsInt();
	}

	private void parseRGBFunction(IColorRGBFunction rgbFunction) throws IOException, ParserException {
		final Object [] vals = parseFunctionParams(3, paramIdx -> parseInt());
		
		rgbFunction.onColor((int)vals[0], (int)vals[1], (int)vals[2], DecimalSize.NONE);
	}

	private void parseRGBAFunction(IColorRGBFunction rgbFunction) throws IOException, ParserException {
		
		final Object [] vals = parseFunctionParams(4, paramIdx -> paramIdx == 3 ? parseDecimal() : parseInt());
		
		rgbFunction.onColor((int)vals[0], (int)vals[1], (int)vals[2], (int)vals[3]);
	}

	@FunctionalInterface
	interface IParseParam {
		Object parse(int paramIdx) throws IOException, ParserException;
	}
	
	private static final Object [] EMPTY_ARRAY = new Object[0];
	
	private Object [] parseFunctionParams(int numParams, IParseParam parseParam) throws IOException, ParserException {
		// Parsed the function name already, parse the parameters
		CSSToken token = lexSkipWSAndComment(CSSToken.PARENTHESIS_START);
	
		if (token != CSSToken.PARENTHESIS_START) {
			throw lexer.unexpectedToken();
		}
		
		final Object [] ret;
		
		if (numParams == 0) {
			ret = EMPTY_ARRAY;
		}
		else {
			ret = new Object[numParams];
			
			for (int i = 0; i < numParams; ++ i) {
				
				skipAnyWS();
				
				ret[i] = parseParam.parse(i);

				// Comma or end parenthesis
				final CSSToken nextToken = i == numParams - 1
						? CSSToken.PARENTHESIS_END
						: CSSToken.COMMA;
				
				token = lexSkipWSAndComment(nextToken);

				if (token != nextToken) {
					throw lexer.unexpectedToken();
				}
			}
		}
		
		return ret;
	}

	
	private static final CSSToken [] FONTSIZE_TOKENS = copyTokens(token -> token.getFontSize() != null, CSSToken.INTEGER);
	
	private boolean parseFontSize(LISTENER_CONTEXT context) throws NumberFormatException, IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(FONTSIZE_TOKENS);
		
		final boolean semiColonRead;
		
		switch (token) {
		case INTEGER:
			// parse as size
			semiColonRead = parseSizeValueAfterInt(Integer.parseInt(lexer.get()), (value, unit) -> listener.onFontSize(context, value, unit, null));
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			listener.onFontSize(context, 0, null, token.getFontSize());
			semiColonRead = false;
			break;
		}
		
		return semiColonRead;
	}

	private static final CSSToken [] FONTWEIGHT_TOKENS = copyTokens(token -> token.getFontWeight() != null, CSSToken.INTEGER);
	
	private boolean parseFontWeight(LISTENER_CONTEXT context) throws NumberFormatException, IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(FONTWEIGHT_TOKENS);
		
		final boolean semiColonRead;
		
		switch (token) {
		case INTEGER:
			// parse as size
			final int value = Integer.parseInt(lexer.get());
			listener.onFontWeight(context, value, null);
			semiColonRead = false;
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			listener.onFontWeight(context, 0, token.getFontWeight());
			semiColonRead = false;
			break;
		}
		
		return semiColonRead;
	}

	private static final CSSToken [] POSITION_TOKENS = copyTokens(token -> token.getPosition() != null);

	private boolean parsePosition(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(POSITION_TOKENS, token -> listener.onPosition(context, token.getPosition()));
	}

	private static final CSSToken [] FLOAT_TOKENS = copyTokens(token -> token.getFloat() != null);

	private boolean parseFloat(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(FLOAT_TOKENS, token -> listener.onFloat(context, token.getFloat()));
	}

	private static final CSSToken [] CLEAR_TOKENS = copyTokens(token -> token.getClear() != null);

	private boolean parseClear(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(CLEAR_TOKENS, token -> listener.onClear(context, token.getClear()));
	}

	private static final CSSToken [] TEXT_ALIGN_TOKENS = copyTokens(token -> token.getTextAlign() != null);

	private boolean parseTextAlign(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(TEXT_ALIGN_TOKENS, token -> listener.onTextAlign(context, token.getTextAlign()));
	}

	private static final CSSToken [] TEXT_DECORATION_TOKENS = copyTokens(token -> token.getTextDecoration() != null);

	private boolean parseTextDecoration(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(TEXT_DECORATION_TOKENS, token -> listener.onTextDecoration(context, token.getTextDecoration()));
	}

	private boolean parseEnum(CSSToken [] tokens, Consumer<CSSToken> onToken) throws IOException, ParserException {
		
		CSSToken token = lexSkipWSAndComment(tokens);
		
		if (token == CSSToken.NONE) {
			throw lexer.unexpectedToken();
		}
		
		onToken.accept(token);
		
		return false;
	}
	
	private static int hexValue(String s, int start, int length) {
		
		int num = 0;
		
		for (int i = 0; i < length; ++ i) {
			final char c = s.charAt(start + i);
			
			final int digit;
			
			if (Character.isDigit(c)) {
				digit = c - '0';
			}
			else if (c >= 'A' && c <= 'F') {
				digit = c - 'A' + 10;
			}
			else if (c >= 'a' && c <= 'f') {
				digit = c - 'A' + 10;
			}
			else {
				throw new IllegalArgumentException("Unexpected character " + c);
			}

			num = num * 16 + digit;
		}
		
		return num;
	}
}
