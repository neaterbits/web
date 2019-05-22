package com.test.web.parse.css;

import java.io.IOException;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class CSSParserHelperBase {

	static int parseInt(Lexer<CSSToken, CharInput> lexer) throws IOException, ParserException {
		// TODO move to lexer to get rid of String intermediate step?
		return Integer.parseInt(lexer.get());
	}
}
