package com.test.web.parse.css;

class CachedRGBA {
	private int r, g, b, a;
	private boolean initialized;
	
	void init(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		
		this.initialized = true;
	}

	int getR() {
		return r;
	}

	int getG() {
		return g;
	}

	int getB() {
		return b;
	}

	int getA() {
		return a;
	}

	boolean isInitialized() {
		return initialized;
	}
}
