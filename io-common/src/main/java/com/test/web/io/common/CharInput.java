package com.test.web.io.common;

import java.io.IOException;

public interface CharInput {

	int readNext() throws IOException;

	// for tokenizer
	void mark();
}
