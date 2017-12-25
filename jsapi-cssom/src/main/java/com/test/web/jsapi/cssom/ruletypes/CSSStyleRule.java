package com.test.web.jsapi.cssom.ruletypes;

import com.test.web.css.common.ICSSStyleSheet;
import com.test.web.jsapi.cssom.CSSRule;
import com.test.web.jsapi.cssom.CSSStyleDeclaration;

public final class CSSStyleRule<RULE> extends CSSRule<RULE> {

	private final CSSStyleDeclaration<RULE> style;
	public CSSStyleRule(ICSSStyleSheet<RULE> styleSheet, RULE rule) {
		super(styleSheet, rule);
		
		this.style = new CSSStyleDeclaration<>(styleSheet, rule, this);
	}
	
	public String getSelectorText() {
		return getAccessStyleSheet().getStyleSelectorText(getRule());
	}
	
	public void setSelectorText(String selectorText) {
		getAccessStyleSheet().setStyleSelectorText(getRule(), selectorText);
	}
	
	public CSSStyleDeclaration<RULE> getStyle() {
		return style;
	}
}
