package com.test.web.parse.css;

import java.io.IOException;

import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;

abstract class CSSParserHelperBase {

	static int parseInt(Lexer<CSSToken, CharInput> lexer) throws IOException, ParserException {
		// TODO move to lexer to get rid of String intermediate step?
		return Integer.parseInt(lexer.get());
	}
}
