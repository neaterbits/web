package com.test.web.jsengine.common;

public class JSCompileException extends Exception {

	private static final long serialVersionUID = 1L;

	public JSCompileException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSCompileException(String message) {
		super(message);
	}
}
