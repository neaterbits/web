package com.test.web.io.oo;

import com.test.web.io.common.Tokenizer;

public interface OOTokenizer extends Tokenizer {

	/**
	 * Get current token as a long
	 * 
	 * @return
	 */
	
	String getString(int startOffset, int endSkip);
	
}
