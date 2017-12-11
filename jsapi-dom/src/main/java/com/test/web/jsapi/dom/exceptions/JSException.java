package com.test.web.jsapi.dom.exceptions;

public abstract class JSException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JSException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSException(String message) {
		super(message);
	}
}
