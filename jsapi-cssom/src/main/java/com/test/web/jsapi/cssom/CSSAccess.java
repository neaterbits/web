package com.test.web.jsapi.cssom;

import com.test.web.css.common.ICSSStyleSheet;

// Base class for CSS document access
public abstract class CSSAccess<RULE> {

	private final ICSSStyleSheet<RULE> styleSheet;

	protected CSSAccess(ICSSStyleSheet<RULE> styleSheet) {
		
		if (styleSheet == null) {
			throw new IllegalArgumentException("styleSheet == null");
		}

		this.styleSheet = styleSheet;
	}

	protected final ICSSStyleSheet<RULE> getAccessStyleSheet() {
		return styleSheet;
	}
}
