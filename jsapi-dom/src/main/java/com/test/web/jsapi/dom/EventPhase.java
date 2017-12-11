package com.test.web.jsapi.dom;

public enum EventPhase {

	NONE(0),
	CAPTURING_PHASE(1),
	AT_TARGET(2),
	BUBBLING_PHASE(3);

	private final int jsValue;

	private EventPhase(int jsValue) {
		this.jsValue = jsValue;
	}

	public int getJsValue() {
		return jsValue;
	}
}
