package com.test.web.parse.css;

import java.io.IOException;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

class CSSParserHelperWS {

	static void assureTokenSkipWSAndComment(Lexer<CSSToken, CharInput> lexer, CSSToken expected) throws IOException, ParserException {
		CSSToken token = lexer.lexSkipWSAndComment(expected);
		
		if (token != expected) {
			throw lexer.unexpectedToken();
		}
	}
}
