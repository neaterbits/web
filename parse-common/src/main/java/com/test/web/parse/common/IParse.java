package com.test.web.parse.common;

import java.io.IOException;

import com.test.web.io.common.CharInput;

@FunctionalInterface
public interface IParse<DOCUMENT> {
	DOCUMENT parse(CharInput charInput) throws IOException, ParserException;
}
