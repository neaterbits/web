package com.test.web.parse.css;

import java.io.IOException;

import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.types.DecimalSize;

public class CSSParseHelperNumbers extends CSSParserHelperBase {

	static int parseInt(Lexer<CSSToken, CharInput> lexer) throws IOException, ParserException {
		CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, CSSToken.INTEGER);

		return parseInt(lexer);
	}

	static int parseDecimal(Lexer<CSSToken, CharInput> lexer) throws IOException, ParserException {

		// Allow to start with '.'

		CSSToken token = lexer.lex(CSSToken.INTEGER, CSSToken.DOT);

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
