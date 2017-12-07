package com.test.web.parse.css;

import java.io.IOException;
import java.util.function.BiConsumer;

import com.test.web.css.common.enums.CSSUnit;
import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.common.TokenMergeHelper;

/**
 * Helper class for parsing sizes like 100px where it might be followed by a semicolon
 * (eg size can be 0; without px and then we will have read the semicolon token)
 *
 */
class CSSParserHelperSizeToSemicolon {

	private static final CSSToken [] UNIT_OR_SEMICOLON_TOKENS = TokenMergeHelper.copyTokens(
			CSSToken.class,
			token -> token.getUnit() != null, 
			CSSToken.SEMICOLON);

	private static final CSSToken [] UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS = TokenMergeHelper.merge(
			UNIT_OR_SEMICOLON_TOKENS,
			
			CSSToken.DOT,
			CSSToken.SEMICOLON,
			CSSToken.WS,
			CSSToken.COMMENT);
	
	private static final CSSToken [] INTEGER_OR_UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS = TokenMergeHelper.merge(
			UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS,
			
			CSSToken.INTEGER);

	// parse size and unit including possibility for decimal or until hits semicolon (if unit not specified)
	static boolean parsePossiblyDecimalSizeValue(Lexer<CSSToken, CharInput> lexer, CSSUnit defaultUnit, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		
		final CSSToken endToken = CSSParserHelperSize.parsePossiblyDecimalSizeValue(
				lexer,
				defaultUnit,
				INTEGER_OR_UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS,
				UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS,
				UNIT_OR_SEMICOLON_TOKENS,
				toCall);

		return endToken != null && endToken == CSSToken.SEMICOLON;
	}
	
	// we have found first int, eg 25 in "25.3em" or 25px, parse any decimal part and unit
	static boolean parseSizeValueAfterInt(Lexer<CSSToken, CharInput> lexer, CSSUnit defaultUnit, int value, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		final CSSToken endToken = CSSParserHelperSize.parseSizeValueAfterInt(lexer, defaultUnit, value, UNIT_OR_DOT_OR_SEMICOLON_OR_WS_OR_COMMENT_TOKENS, UNIT_OR_SEMICOLON_TOKENS, toCall);

		return endToken != null && endToken == CSSToken.SEMICOLON;
	}

	// we have found decimal '.', parse int after '.' and unit
	static boolean parseDecimalAfterDot(Lexer<CSSToken, CharInput> lexer, CSSUnit defaultUnit, int beforeDecimal, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		final CSSToken token = CSSParserHelperSize.parseDecimalAfterDot(lexer, defaultUnit,beforeDecimal,  UNIT_OR_SEMICOLON_TOKENS, toCall);
		
		return token != null && token == CSSToken.SEMICOLON;
	}
}
