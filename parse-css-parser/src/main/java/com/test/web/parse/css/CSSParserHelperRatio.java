package com.test.web.parse.css;

import java.io.IOException;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;
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
