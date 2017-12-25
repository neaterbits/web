package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.CSSStyleDeclaration;

public final class CSSPageRule<RULE> extends CSSRule<RULE> {
	
	private final CSSStyleDeclaration<RULE> style;

	public CSSPageRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
		
		this.style = new CSSStyleDeclaration<>(getAccessStyleSheet(), rule, this);
	}

	public String getSelectorText() {
		return getAccessStyleSheet().getPageSelectorText(getRule());
	}
	
	public void setSelectorText(String selectorText) {
		getAccessStyleSheet().setPageSelectorText(getRule());
	}
	
	public CSSStyleDeclaration<RULE> getStyle() {
		return style;
	}
}
