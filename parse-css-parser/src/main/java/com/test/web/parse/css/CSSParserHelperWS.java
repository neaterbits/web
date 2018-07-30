package com.test.web.parse.css;

import java.io.IOException;

import com.test.web.io.common.CharInput;
import com.test.web.parse.common.Lexer;
import com.test.web.parse.common.ParserException;
import com.test.web.parse.common.TokenMergeHelper;

class CSSParserHelperWS {
	private static final CSSToken [] wsAndComment = new CSSToken [] { CSSToken.WS, CSSToken.COMMENT };

	@SafeVarargs
	static CSSToken lexSkipWSAndComment(Lexer<CSSToken, CharInput> lexer, CSSToken ... tokens) throws IOException {

		// TODO avoid allocation
		CSSToken token;
		
		final CSSToken [] mergedTokens = TokenMergeHelper.merge(wsAndComment, tokens);
		
		for (;;) {
			token = lexer.lex(mergedTokens);

			if (token != CSSToken.WS && token != CSSToken.COMMENT) {
				break;
			}
		}
		
		return token;
	}


	static void skipAnyWS(Lexer<CSSToken, CharInput> lexer) throws IOException {
		lexer.lex(CSSToken.WS);
	}

	static void assureTokenSkipWSAndComment(Lexer<CSSToken, CharInput> lexer, CSSToken expected) throws IOException, ParserException {
		CSSToken token = lexSkipWSAndComment(lexer, expected);
		
		if (token != expected) {
			throw lexer.unexpectedToken();
		}
	}
}
