package com.test.web.parse.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

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
	protected final TOKEN [] merge(TOKEN token, TOKEN ... otherTokens) {
		final TOKEN [] copy = Arrays.copyOf(otherTokens, otherTokens.length + 1);

		// token first since is most likely to find?
		copy[otherTokens.length] = token;
		
		return copy;
	}

	@SafeVarargs
	protected final TOKEN [] merge(TOKEN [] tokens, TOKEN ... otherTokens) {
		final TOKEN [] copy = Arrays.copyOf(tokens, tokens.length + otherTokens.length);

		// token first since is most likely to find?
		System.arraycopy(otherTokens, 0, copy, tokens.length, otherTokens.length);

		return copy;
	}

	protected final TOKEN [] merge(Collection<TOKEN []> tokens, TOKEN ... otherTokens) {

		int num = 0;
		
		for (TOKEN [] tokArray : tokens) {
			num += tokArray.length;
		}
		
		if (num == 0) {
			throw new IllegalStateException("no tokens");
		}
		
		num += otherTokens.length;
		
		// Just make copy of intial array
		final TOKEN[] copy = Arrays.copyOf(tokens.iterator().next(), num);
		
		int dstIdx = 0;
		
		for (TOKEN [] tokArray : tokens) {
			for (TOKEN t : tokArray) {
				copy[dstIdx ++] = t;
			}
		}
		
		for (TOKEN t : otherTokens) {
			copy[dstIdx ++] = t;
		}
		
		return copy;
	}

	@SafeVarargs
	protected final TOKEN lexSkipWS(TOKEN ... tokens) throws IOException {

		TOKEN token = lexer.lex(merge(wsToken, tokens));

		if (token == wsToken) {
			token = lexer.lex(tokens);
		}
		
		return token;
	}
}
