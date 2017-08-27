package com.test.web.io._long;

import com.test.web.io.common.Tokenizer;

public interface LongTokenizer extends Tokenizer {

	/**
	 * Get current token as a long
	 * 
	 * @return
	 */
	
	long get(int startOffset, int endSkip);
	
}
