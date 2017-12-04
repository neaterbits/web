package com.test.web.loadqueue.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleStreamFactory implements IStreamFactory {

	private final String string;
	
	public SimpleStreamFactory(String s) {
		this.string = s;
	}

	@Override
	public InputStream getStream(String url) throws IOException {
		return new ByteArrayInputStream(string.getBytes());
	}
}
