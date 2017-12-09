package com.test.web.layout;

// Base class for layout cases
abstract class BaseLayoutCase {

	void onElementStart(StackElement container, StackElement sub) {
		
	}

	void onElementEnd(StackElement container, StackElement sub) {
		
	}
	
	final void renderCurrentTextLine() {
		throw new UnsupportedOperationException("TODO");
	}
}
