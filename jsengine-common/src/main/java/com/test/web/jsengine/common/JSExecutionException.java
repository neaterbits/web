package com.test.web.jsengine.common;

public class JSExecutionException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public JSExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSExecutionException(String message) {
		super(message);
	}
}
