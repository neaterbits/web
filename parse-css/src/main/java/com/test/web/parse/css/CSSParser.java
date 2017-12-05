package com.test.web.parse.css;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.test.web.css.common.enums.CSSJustify;
import com.test.web.css.common.enums.CSSMax;
import com.test.web.css.common.enums.CSSMin;
import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.BaseParser;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.types.DecimalSize;

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
			
			if (token == CSSToken.WS) {
				// Just continue in case of WS
				continue;
			}
			
			switch (token) {
			case ID_MARKER:
			case CLASS_MARKER:
			case TAG:
				final LISTENER_CONTEXT context = listener.onBlockStart();
				parseMarkedBlock(token, context);
				listener.onBlockEnd(context);
				break;
				
			case COMMENT:
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
		parseMarker(initialMarker, context);
		
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
				parseMarker(token, context);
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
	
	private void parseMarker(CSSToken marker, LISTENER_CONTEXT context) throws IOException, ParserException {
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

	private static final CSSToken [] UNIT_OR_DOT_OR_SEMICOLON_TOKENS = copyTokens(
			token -> token.getUnit() != null, 
			CSSToken.DOT,
			CSSToken.SEMICOLON);

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
		
		// Skip any WS
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
			
		case BACKGROUND_COLOR:
			semiColonRead = parseColor((r, g, b) -> listener.onBackgroundColor(context, r, g, b));
			break;
			
		case MARGIN_LEFT:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onMarginLeft(context, size, unit, justify));
			break;
			
		case MARGIN_RIGHT:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onMarginRight(context, size, unit, justify));
			break;

		case MARGIN_TOP:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onMarginTop(context, size, unit, justify));
			break;

		case MARGIN_BOTTOM:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onMarginBottom(context, size, unit, justify));
			break;
			
		case MARGIN:
			semiColonRead = parseMargin(context);
			break;
			
		case PADDING_LEFT:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onPaddingLeft(context, size, unit, justify));
			break;
			
		case PADDING_RIGHT:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onPaddingRight(context, size, unit, justify));
			break;

		case PADDING_TOP:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onPaddingTop(context, size, unit, justify));
			break;

		case PADDING_BOTTOM:
			semiColonRead = parseSizeOrAuto((size, unit, justify) -> listener.onPaddingBottom(context, size, unit, justify));
			break;

		case FLOAT:
			semiColonRead = parseFloat(context);
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
	private boolean parseMargin(LISTENER_CONTEXT context) throws IOException, ParserException {
		// margin has several combinations and number of entries
		// margin : auto;
		// margin: 1, 2, 3 or 4 sizes
		
		// TODO perhaps cache if parser is singlethreaded
		final MarginPart part1 = new MarginPart();
		
		// first parse one size or auto
		boolean semiColonRead = parseSizeOrAuto((size, unit, justify) -> part1.init(size, unit, justify));
		
		if (part1.justify == CSSJustify.AUTO) {
			// margin: auto which is special-case
			listener.onMargin(context,
					0, null, CSSJustify.AUTO,
					0, null, CSSJustify.AUTO,
					0, null, CSSJustify.AUTO,
					0, null, CSSJustify.AUTO);
		}
		else {
			// We have more than one part that we have to read, which is which depends on the number of sizes found
			// there ought to be max 4
			final MarginPart part2 = new MarginPart();
			final MarginPart part3 = new MarginPart();
			final MarginPart part4 = new MarginPart();
			
			semiColonRead = parseSizeValueOrSemicolon((size, unit) -> part2.init(size, unit, CSSJustify.SIZE));
			if (part2.initialized && !semiColonRead) {
				// read a value, try part3
				semiColonRead = parseSizeValueOrSemicolon((size, unit) -> part3.init(size, unit, CSSJustify.SIZE));
				
				if (part3.initialized && !semiColonRead) {
					semiColonRead = parseSizeValueOrSemicolon((size, unit) -> part4.init(size, unit, CSSJustify.SIZE));
				}
			}
			
			if (part4.initialized) {
				// got 4 parts, pass them all
				listener.onMargin(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part3.size, part3.unit, part3.justify,
						part4.size, part4.unit, part4.justify);
			}
			else if (part3.initialized) {
				listener.onMargin(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part3.size, part3.unit, part3.justify,
						part2.size, part2.unit, part2.justify);
			}
			else if (part2.initialized) {
				listener.onMargin(context,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify,
						part1.size, part1.unit, part1.justify,
						part2.size, part2.unit, part2.justify);
			}
			else if (part1.initialized) {
				listener.onMargin(context,
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
	
	private boolean parseSizeOrAuto(IJustifyFunction toCall) throws IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(CSSToken.INTEGER, CSSToken.AUTO, CSSToken.DOT);
		
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
		
		CSSToken token = lexer.lex(UNIT_OR_DOT_OR_SEMICOLON_TOKENS);
		
		switch (token) {
		case SEMICOLON:
			semiColonRead = true;
			break;
			
		case DOT:
			semiColonRead = parseDecimalAfterDot(value, unit, toCall);
			gotDot = true;
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
		
		final int afterDecimal = Integer.parseInt(lexer.get());
		final DecimalSize value = new DecimalSize(beforeDecimal, afterDecimal);
		
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
	
	private boolean parseColor(ColorFunction function) throws IOException, ParserException {

		CSSToken token = lexSkipWSAndComment(CSSToken.COLOR_MARKER);
		
		switch (token) {
		case COLOR_MARKER:
			// Read color string
			token = lexer.lex(CSSToken.HEXDIGITS);
			if (token != CSSToken.HEXDIGITS) {
				throw lexer.unexpectedToken();
			}
			
			final String hexString = lexer.get();
			
			if (hexString.length() != 6) {
				throw new ParserException("Unexpected length: " + hexString.length());
			}
			
			// Parse into hex values
			function.onColor(
					hexValue(hexString, 0, 2),
					hexValue(hexString, 0, 2),
					hexValue(hexString, 4, 2)
			);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
		
		return false;
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

	private static final CSSToken [] POSITION_TOKENS = copyTokens(token -> token.getPosition() != null);

	private boolean parsePosition(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(POSITION_TOKENS, token -> listener.onPosition(context, token.getPosition()));
	}

	private static final CSSToken [] FLOAT_TOKENS = copyTokens(token -> token.getFloat() != null);

	private boolean parseFloat(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(FLOAT_TOKENS, token -> listener.onFloat(context, token.getFloat()));
	}

	private static final CSSToken [] TEXT_ALIGN_TOKENS = copyTokens(token -> token.getTextAlign() != null);

	private boolean parseTextAlign(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(TEXT_ALIGN_TOKENS, token -> listener.onTextAlign(context, token.getTextAlign()));
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
