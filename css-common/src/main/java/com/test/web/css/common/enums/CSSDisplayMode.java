package com.test.web.css.common.enums;

import com.test.web.types.IEnum;

public enum CSSDisplayMode implements IEnum {

	BROWSER("browser", null),
	MINIMAL_UI("minimal-ui", CSSDisplayMode.BROWSER),
	STANDALONE("standalone", CSSDisplayMode.MINIMAL_UI),
	FULLSCREEN("fullscreen", CSSDisplayMode.STANDALONE);
	
	private final String name;
	private final CSSDisplayMode fallback;

	private CSSDisplayMode(String name, CSSDisplayMode fallback) {
		this.name = name;
		this.fallback = fallback;
	}

	@Override
	public String getName() {
		return name;
	}

	public CSSDisplayMode getFallback() {
		return fallback;
	}
}
