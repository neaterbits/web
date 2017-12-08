package com.test.web.parse.css;

import com.test.web.types.ColorAlpha;
import com.test.web.types.ColorRGB;

class CachedRGBA {
	private int r, g, b, a;
	private boolean initialized;
	
	CachedRGBA() {
		clear();
	}
	
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
	
	void clear() {
		this.r = ColorRGB.NONE;
		this.g = ColorRGB.NONE;
		this.b = ColorRGB.NONE;
		this.a = ColorAlpha.NONE;

		this.initialized = false;
	}
}
