package com.test.web.parse.common;

import java.io.IOException;

import com.test.web.io.common.CharInput;
import com.test.web.io.common.Tokenizer;

@FunctionalInterface
public interface IParse<DOCUMENT> {
	DOCUMENT parse(CharInput charInput, Tokenizer tokenizer) throws IOException, ParserException;
}
