package com.test.web.parse.css;

import java.io.IOException;

import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.types.Ratio;

final class CSSParserHelperRatio extends CSSParserHelperBase {

	static Ratio parseRatio(Lexer<CSSToken, CharInput> lexer) throws IOException, ParserException {
		final int antecendent;
		final int consequent;

		CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, CSSToken.INTEGER);
		antecendent = parseInt(lexer);

		CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, CSSToken.SLASH);
		
		CSSParserHelperWS.assureTokenSkipWSAndComment(lexer, CSSToken.INTEGER);
		consequent = parseInt(lexer);

		return new Ratio(antecendent, consequent);
	}
}
