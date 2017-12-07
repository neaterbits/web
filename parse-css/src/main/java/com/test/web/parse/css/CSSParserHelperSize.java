package com.test.web.parse.css;

import java.io.IOException;
import java.util.function.BiConsumer;

import com.test.web.css.common.enums.CSSUnit;
import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.common.TokenMergeHelper;
import com.test.web.types.DecimalSize;

public class CSSParserHelperSize {
	
	static  CSSToken parsePossiblyDecimalSizeValue(
			Lexer<CSSToken, CharInput> lexer,
			CSSUnit defaultUnit,
			CSSToken [] widthInitialIntTokens,
			CSSToken [] afterInitialIntTokens,
			CSSToken [] afterSecondIntTokens,
			BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		
		// Number followed by possibly units
		CSSToken token;
		
		int value = -1;
		
		boolean done = false;
		boolean hasIntValue = false;
		
		do { // loop to skip any WS or comments
			token = lexer.lex(widthInitialIntTokens);
			
			switch (token) {
			case INTEGER:
				value = Integer.parseInt(lexer.get());
				done = true;
				hasIntValue = true;
				break;
				
			case DOT:
				done = true;
				value = 0;
				hasIntValue = true;
				break;
				
			case WS:
			case COMMENT:
				// loop
				break;
				
			case NONE:
				throw lexer.unexpectedToken();
				
			default:
				// eg. semicolon, comma or other character after size
				done = true;
				break;
			}
		} while (!done);
		
		final CSSToken ret;
		
		// After this we might have '.', unit (px, em, ..) or end of expression (';' in some cases, ws comment, or ',' or ')' if these are in function calls)

		if (hasIntValue) {
			ret = parseSizeValueAfterInt(lexer, defaultUnit, value, afterInitialIntTokens, afterSecondIntTokens, toCall);
		}
		else {
			// eg. semicolon or comma
			ret = token;
		}

		return ret;
	}
	
	static CSSToken parseSizeValueAfterInt(
			Lexer<CSSToken, CharInput> lexer,
			CSSUnit defaultUnit,
			int value,
			CSSToken [] afterIntTokens,
			CSSToken [] afterDotTokens,
			BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		
		// Now may be decimal-point or unit token or semicolon
		
		boolean gotDot = false;
		
		CSSToken endTokenRead = null; // end token would typically be anything that is not WS or unit or a number
		
		CSSToken token = lexer.lex(afterIntTokens);
		
		CSSUnit unit = defaultUnit;
		
		switch (token) {
			
		case DOT:
			endTokenRead = parseDecimalAfterDot(lexer, unit, value, afterDotTokens, toCall);
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
			if (token.getUnit() != null) { // unit token
				unit = token.getUnit();
			}
			else {
				endTokenRead = token;
			}
			break;
		}
		
		if (!gotDot) {
			toCall.accept(DecimalSize.encodeAsInt(value), unit);
		}
		
		return endTokenRead;
	}

	static CSSToken parseDecimalAfterDot(
			Lexer<CSSToken, CharInput> lexer,
			CSSUnit defaultUnit,
			int beforeDecimal,
			CSSToken [] unitTokens,
			BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		
		CSSToken token;
		CSSToken endTokenRead = null;
		
		CSSUnit unit = defaultUnit;
		
		// Decimal number, should be integer
		token = CSSParserHelperWS.lexSkipWSAndComment(lexer, CSSToken.INTEGER);
		
		if (token != CSSToken.INTEGER) {
			throw lexer.unexpectedToken();
		}
		
		final DecimalSize value = new DecimalSize(beforeDecimal, lexer.get());
		
		// Parse unit
		token = lexer.lex(unitTokens);
		
		switch (token) {

		case NONE:
			throw lexer.unexpectedToken();
			
		default:
			// Unit token
			if (token.getUnit() != null) {
				unit = token.getUnit();
			}
			else {
				endTokenRead = token;
			}
			break;
		}
		
		toCall.accept(value.encodeAsInt(), unit);
		
		return endTokenRead;
	}


	private static final CSSToken [] UNIT_TOKENS = TokenMergeHelper.copyTokens(
			CSSToken.class,
			token -> token.getUnit() != null);

	private static final CSSToken [] UNIT_OR_DOT_OR_WS_OR_COMMENT_TOKENS = TokenMergeHelper.merge(UNIT_TOKENS,
			CSSToken.DOT,
			CSSToken.WS,
			CSSToken.COMMENT);

	private static final CSSToken [] INTEGER_OR_UNIT_OR_DOT_OR_WS_OR_COMMENT_TOKENS = TokenMergeHelper.merge(UNIT_OR_DOT_OR_WS_OR_COMMENT_TOKENS,
			CSSToken.INTEGER);

	// parse size and unit including possibility for decimal or until hits semicolon (if unit not specified)
	static boolean parseDecimalSizeValue(Lexer<CSSToken, CharInput> lexer, CSSUnit defaultUnit, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		
		final CSSToken endToken = CSSParserHelperSize.parsePossiblyDecimalSizeValue(
				lexer,
				defaultUnit,
				INTEGER_OR_UNIT_OR_DOT_OR_WS_OR_COMMENT_TOKENS,
				UNIT_OR_DOT_OR_WS_OR_COMMENT_TOKENS,
				UNIT_TOKENS,
				toCall);

		return endToken != null && endToken == CSSToken.SEMICOLON;
	}
	
	static boolean parseSizeValueAfterInt(Lexer<CSSToken, CharInput> lexer, CSSUnit defaultUnit, int value, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		final CSSToken endToken = CSSParserHelperSize.parseSizeValueAfterInt(lexer, defaultUnit, value, UNIT_OR_DOT_OR_WS_OR_COMMENT_TOKENS, UNIT_TOKENS, toCall);

		return endToken != null && endToken == CSSToken.SEMICOLON;
	}
}
