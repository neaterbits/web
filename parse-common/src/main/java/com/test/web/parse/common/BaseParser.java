package com.test.web.parse.common;

import java.io.IOException;

import com.test.web.io.common.CharInput;

public abstract class BaseParser<TOKEN extends Enum<TOKEN> & IToken, INPUT extends CharInput> {

	private final Lexer<TOKEN, CharInput> lexer;
	private final TOKEN wsToken;

	protected BaseParser(Lexer<TOKEN, CharInput> lexer, TOKEN wsToken) {

		if (lexer == null) {
			throw new IllegalArgumentException("lexer == null");
		}

		if (wsToken == null) {
			throw new IllegalArgumentException("wsToken == null");
		}

		this.lexer = lexer;
		this.wsToken = wsToken;
	}

	protected final Lexer<TOKEN, CharInput> getLexer() {
		return lexer;
	}

	@SafeVarargs
	protected final TOKEN lexSkipWS(TOKEN ... tokens) throws IOException {

		TOKEN token = lexer.lex(TokenMergeHelper.merge(wsToken, tokens));

		if (token == wsToken) {
			token = lexer.lex(tokens);
		}
		
		return token;
	}
}
