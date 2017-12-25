package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.CSSStyleDeclaration;

public final class CSSViewportRule<RULE> extends CSSRule<RULE> {

	private final CSSStyleDeclaration<RULE> style;
	
	public CSSViewportRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
		
		this.style = new CSSStyleDeclaration<>(getAccessStyleSheet(), rule, this);
	}

	public CSSStyleDeclaration<RULE> getStyle() {
		return style;
	}
}
