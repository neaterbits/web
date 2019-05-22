package com.test.web.parse.css;

import java.io.IOException;
import java.util.function.BiConsumer;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;
import com.neaterbits.util.parse.TokenMergeHelper;
import com.test.web.css.common.enums.CSSUnit;

class CSSParserHelperSizeAsOnlyFunctionParam {
	private static final CSSToken [] UNIT_OR_PARENTHESIS_TOKENS = TokenMergeHelper.copyTokens(
			CSSToken.class,
			token -> token.getUnit() != null, 
			CSSToken.PARENTHESIS_END);

	private static final CSSToken [] UNIT_OR_DOT_OR_PARENTHESIS_OR_WS_OR_COMMENT_TOKENS = TokenMergeHelper.merge(
			UNIT_OR_PARENTHESIS_TOKENS,
			
			CSSToken.DOT,
			CSSToken.WS,
			CSSToken.COMMENT);

	private static final CSSToken [] INTEGER_OR_UNIT_OR_DOT_OR_PARENTHESIS_OR_WS_OR_COMMENT_TOKENS = TokenMergeHelper.merge(
			UNIT_OR_DOT_OR_PARENTHESIS_OR_WS_OR_COMMENT_TOKENS,
			
			CSSToken.INTEGER);


	// parse size and unit including possibility for decimal or until hits comma or end parenthesis (if unit not specified). This is for funcion parameters
	static boolean parsePossiblyDecimalSizeValue(Lexer<CSSToken, CharInput> lexer, CSSUnit defaultUnit, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		final CSSToken token = CSSParserHelperSize.parsePossiblyDecimalSizeValue(
				lexer,
				defaultUnit,
				INTEGER_OR_UNIT_OR_DOT_OR_PARENTHESIS_OR_WS_OR_COMMENT_TOKENS,
				UNIT_OR_DOT_OR_PARENTHESIS_OR_WS_OR_COMMENT_TOKENS,
				UNIT_OR_PARENTHESIS_TOKENS,
				toCall);
		
		return token != null && token == CSSToken.PARENTHESIS_END;
	}

	// we have found first int, eg 25 in "25.3em" or 25px, parse any decimal part and unit
	static boolean parseSizeValueAfterInt(Lexer<CSSToken, CharInput> lexer, CSSUnit defaultUnit, int value, BiConsumer<Integer, CSSUnit> toCall) throws IOException, ParserException {
		final CSSToken endToken = CSSParserHelperSize.parseSizeValueAfterInt(lexer, defaultUnit, value, UNIT_OR_DOT_OR_PARENTHESIS_OR_WS_OR_COMMENT_TOKENS, UNIT_OR_PARENTHESIS_TOKENS, toCall);

		return endToken != null && endToken == CSSToken.PARENTHESIS_END;
	}
}
