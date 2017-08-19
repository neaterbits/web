package com.test.web.parse.css;

import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;
import com.test.web.parse.common.Lexer;

/**
 * For parsing CSS from file or from a style attribute
 * @author nhl
 *
 */
public class CSSParser<TOKENIZER extends Tokenizer> {
	
	private final CharInput input;
	private final Lexer<CSSToken, CharInput> lexer; 

	public CSSParser(CharInput input) {
		this.input = input;
		this.lexer = new Lexer<CSSToken, CharInput>(input, CSSToken.class, CSSToken.NONE);
	}
	
}
