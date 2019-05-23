package com.test.web.parse.css;

import java.io.IOException;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;
import com.test.web.types.DecimalSize;

public class CSSParseHelperNumbers extends CSSParserHelperBase {

	static int parseInt(Lexer<CSSToken, CharInput> lexer) throws IOException, ParserException {
		CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, CSSToken.INTEGER);

		return parseInt(lexer);
	}

	private static CSSToken [] DECIMAL_TOKENS = new CSSToken [] { CSSToken.INTEGER, CSSToken.DOT };
	
	static int parseDecimal(Lexer<CSSToken, CharInput> lexer) throws IOException, ParserException {

		// Allow to start with '.'

		CSSToken token = lexer.lex(DECIMAL_TOKENS);

		int beforeDot;
		String afterDot;

		switch (token) {
		case INTEGER:
			beforeDot = parseInt(lexer);

			// may or may not be a dot here
			token = CSSParserHelperWS.lexSkipWSAndComment(lexer, CSSToken.DOT);
			if (token == CSSToken.DOT) {
				CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, CSSToken.INTEGER);
				afterDot = lexer.get();
			} else {
				afterDot = "";
			}
			break;

		case DOT:
			beforeDot = 0;
			CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, CSSToken.INTEGER);
			afterDot = lexer.get();
			break;

		default:
			throw lexer.unexpectedToken();
		}

		return new DecimalSize(beforeDot, afterDot).encodeAsInt();
	}
}
