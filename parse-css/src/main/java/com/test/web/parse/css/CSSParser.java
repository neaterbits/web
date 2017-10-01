package com.test.web.parse.css;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.test.web.css.common.enums.CSSTarget;
import com.test.web.css.common.enums.CSSUnit;
import com.test.web.css.common.enums.CSStyle;
import com.test.web.css.common.enums.CSSJustify;
import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.BaseParser;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;

/**
 * For parsing CSS from file or from a style attribute
 * @author nhl
 *
 */
public class CSSParser<TOKENIZER extends Tokenizer, LISTENER_CONTEXT> extends BaseParser<CSSToken, CharInput> {
	
	private final CharInput input;
	private final Lexer<CSSToken, CharInput> lexer; 
	private final CSSParserListener<TOKENIZER, LISTENER_CONTEXT> listener;

	public CSSParser(CharInput input, CSSParserListener<TOKENIZER, LISTENER_CONTEXT> listener) {
		super(new Lexer<CSSToken, CharInput>(input, CSSToken.class, CSSToken.NONE, CSSToken.EOF), CSSToken.WS);
		
		this.input = input;
		this.lexer = getLexer();
		this.listener = listener;
	}
	
	public void parseCSS() throws IOException, ParserException {
		
		boolean done = false;
		
		do {
			CSSToken token = lexer.lex(CSSToken.WS, CSSToken.ID_MARKER, CSSToken.CLASS_MARKER, CSSToken.TAG, CSSToken.EOF);
			
			if (token == CSSToken.WS) {
				// Just continue in case of WS
				continue;
			}
			
			switch (token) {
			case ID_MARKER:
				parseIdBlock();
				break;
				
			case CLASS_MARKER:
				parseClassBlock();
				break;

			case TAG:
				parseTagBlock(lexer.get());
				break;
				
			case EOF:
				done = true;
				break;
				
			default:
				throw lexer.unexpectedToken();
			}
			
		} while (!done);
		
	}
	
	private void parseIdBlock() throws IOException, ParserException {
		final CSSToken token = lexer.lex(CSSToken.ID);
		
		switch (token) {
		case ID:
			final LISTENER_CONTEXT context = listener.onEntityStart(CSSTarget.ID, lexer.get());
			
			parseBlock(context);
			
			listener.onEntityEnd(context);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
	}
	
	private void parseClassBlock() throws IOException, ParserException {
		final CSSToken token = lexer.lex(CSSToken.CLASS);
		
		switch (token) {
		case CLASS:
			final LISTENER_CONTEXT context = listener.onEntityStart(CSSTarget.CLASS, lexer.get());
			
			parseBlock(context);
			
			listener.onEntityEnd(context);
			break;
			
		default:
			throw lexer.unexpectedToken();
		}
	}

	private void parseTagBlock(String tag) throws IOException, ParserException {
		final LISTENER_CONTEXT context = listener.onEntityStart(CSSTarget.TAG, tag);
		
		parseBlock(context);
		
		listener.onEntityEnd(context);
	}
	
	private static final CSSToken [] BLOCK_TOKENS = copyTokens(
			token -> token.getElement() != null, 
			CSSToken.WS,
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
		
		CSSToken token = lexSkipWS(CSSToken.BRACKET_START);
		
		if (token == CSSToken.WS) {
			token = lexer.lex(CSSToken.BRACKET_START);
		}

		if (token != CSSToken.BRACKET_START) {
			throw lexer.unexpectedToken();
		}
		
		// Now parse all fields in CSS until end-bracket
		
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
	
	private void parseElement(LISTENER_CONTEXT context, CSStyle element) throws IOException, ParserException {
		CSSToken token = lexSkipWS(CSSToken.COLON);
		
		if (token != CSSToken.COLON) {
			throw lexer.unexpectedToken();
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
			
		default:
			throw new UnsupportedOperationException("Unknown element " + element);
		}
		
		if (!semiColonRead) {
			token = lexer.lex(CSSToken.SEMICOLON);

			if (token != CSSToken.SEMICOLON) {
				throw lexer.unexpectedToken();
			}
		}
	}
	
	private boolean parseSizeOrAuto(IJustifyFunction toCall) throws IOException, ParserException {
		CSSToken token = lexer.lex(CSSToken.INTEGER, CSSToken.AUTO);
		
		final boolean semiColonRead;
		
		switch (token) {
		case INTEGER:
			final int intValue = Integer.parseInt(lexer.get());
			semiColonRead = parseSizeValueAfterInt(intValue, (size, unit) -> toCall.onJustify(size, unit, CSSJustify.SIZE));
			break;
			
		case AUTO:
			toCall.onJustify(9, null, CSSJustify.AUTO);
			semiColonRead = false;
			break;
			
		default:
			throw lexer.unexpectedToken();
		}

		return semiColonRead;
	}
	
	private boolean parseSizeValue(BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		// Number followed by possibly units
		CSSToken token = lexer.lex(CSSToken.INTEGER);
		
		if (token != CSSToken.INTEGER) {
			throw lexer.unexpectedToken();
		}
		
		int value = Integer.parseInt(lexer.get());
		
		
		return parseSizeValueAfterInt(value, toCall);
	}
	
	private boolean parseSizeValueAfterInt(int value, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
			
		// Now may be decimal-point or unit token or semicolon
		
		boolean semiColonRead = false;
		CSSUnit unit = CSSUnit.PX; // default to pixels
		
		CSSToken token = lexer.lex(UNIT_OR_DOT_OR_SEMICOLON_TOKENS);
		
		switch (token) {
		case SEMICOLON:
			semiColonRead = true;
			break;
			
		case DOT:
			// Decimal number, should be integer
			token = lexer.lex(CSSToken.INTEGER);
			if (token != CSSToken.INTEGER) {
				throw lexer.unexpectedToken();
			}
			
			final int afterDecimal = Integer.parseInt(lexer.get());
			
			// TODO: Shift up value by a number of decimal places
			
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
			break;
			
		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			// Unit token
			unit = token.getUnit();
			break;
		}
		
		toCall.accept(value, unit);
		
		return semiColonRead;
	}
	
	private boolean parseColor(ColorFunction function) throws IOException, ParserException {

		CSSToken token = lexer.lex(CSSToken.COLOR_MARKER);
		
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

	private static final CSSToken [] POSITION_TOKENS = copyTokens(token -> token.getPosition() != null);

	private boolean parsePosition(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(POSITION_TOKENS, token -> listener.onPosition(context, token.getPosition()));
	}

	private static final CSSToken [] FLOAT_TOKENS = copyTokens(token -> token.getFloat() != null);

	private boolean parseFloat(LISTENER_CONTEXT context) throws IOException, ParserException {
		return parseEnum(FLOAT_TOKENS, token -> listener.onFloat(context, token.getFloat()));
	}
	
	private boolean parseEnum(CSSToken [] tokens, Consumer<CSSToken> onToken) throws IOException, ParserException {
		
		CSSToken token = lexer.lex(tokens);
		
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
