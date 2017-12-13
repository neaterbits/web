package com.test.web.io.common;

import java.io.IOException;

public interface CharInput {

	int readNext() throws IOException;

	// for tokenizer
	boolean markSupported();
	
	void mark();
	
	// when reading one char extra in lexer so that tokenizer position in buffer reflects
	// where the lexer is at, so that the next mark() is correct
	// TODO fix this in better way? lexer keeps token in StringBuilder anyway, might as well get string from there?
	// TODO or perhaps lexer should use tokenizer to avoid copying, and remove its own StringBuilder?
	// TODO though for debugging, there should be a mode for the lexer to cache even more characters so can easily see context
	
	// val param is just to verify that matches with last param
	void rewindOneCharacter(int val);
	
	// For debug, peek the number of characters, or whatever is left in the stream
	String peek(int num) throws IOException;
}
