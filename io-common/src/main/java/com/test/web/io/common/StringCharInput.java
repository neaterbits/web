package com.test.web.io.common;

import java.io.IOException;

// Simple CharInput that operates on a single String
public class StringCharInput implements CharInput {

	private final String string;
	private int pos;
	
	public StringCharInput(String string) {
		this.string = string;
		this.pos = 0;
	}

	@Override
	public int readNext() throws IOException {
		final int next;
		
		if (pos == string.length()) {
			next = -1;
		}
		else {
			 next = string.charAt(pos);
			 ++ pos;
		}

		return next;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void mark() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void rewindOneCharacter(int val) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String peek(int num) throws IOException {
		return string.substring(pos, Math.min(string.length(), pos + num));
	}
}
