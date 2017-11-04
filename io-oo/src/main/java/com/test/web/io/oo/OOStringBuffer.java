package com.test.web.io.oo;

import com.test.web.io._long.StringBuffers;
import com.test.web.io.common.LoadStream;

public class OOStringBuffer extends StringBuffers implements OOTokenizer {

	public OOStringBuffer(LoadStream inputStream) {
		super(inputStream);
	}

	@Override
	public String getString(int startOffset, int endSkip) {
		return super.asString(startOffset, endSkip);
	}
}
